package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;
import com.company.utilities.*;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class Broker
{
    private final int id;
    private final Configuration config;

    // store all the topics this broker is responsible.
    private final List<Topic> topics;

    public Broker(int broker_id)
    {
        id = broker_id;
        config = new Configuration("config/config.txt");

        // we get a synchronizedList because BrokerActionsForClients thread modifies it
        topics = Collections.synchronizedList( new ArrayList<Topic>() );

        // create the Topics
        ArrayList<String> topics_names = config.GetTopicsOfBroker(id);
        for(String name : topics_names)
        {
            topics.add(new Topic(name));
        }

        Logger.LogInfo("Broker " + id + " was created.");
    }

    public void run()
    {
        Address broker_address = config.GetAddressOfBroker(id);

        Receiver receiver = new Receiver(broker_address.getPort(), new BrokerReceiverAction(this));
        receiver.run();
    }

    public List<Topic> getChats()
    {
        return topics;
    }


    private class ActionForClients extends Thread
    {
        private ObjectInputStream in;
        private ObjectOutputStream out;

        private Broker broker;

        public ActionForClients(Socket s, Broker b)
        {
            try
            {
                out = new ObjectOutputStream(s.getOutputStream());
                in = new ObjectInputStream(s.getInputStream());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            broker = b;
        }

        @Override
        public void run()
        {
            try
            {
                // read the request
                String request = (String) in.readObject();

                switch (request)
                {
                    case "send config" -> SendConfig();
                    case "accept value" -> AcceptValue();
                    case "send topic" -> {
                        String topic = (String) in.readObject();
                        SendChat(topic);
                    }
                }

            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    in.close();
                    out.close();
                }
                catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }
            }

        }

        private void SendConfig() throws IOException
        {
            out.writeObject(config);
            out.flush();
        }


        private void AcceptValue() throws IOException, ClassNotFoundException
        {
            // read metadata
            MetaData metaData = (MetaData) in.readObject();



            // read the file chunks
            ArrayList<FileChunk> chunks = new ArrayList<>();
            for(int i = 0; i < metaData.getNumberOfChunks(); i++)
            {
                chunks.add((FileChunk) in.readObject());
            }

            // recreate value
            Value v = Value.ReCreate(chunks, metaData);

            if(v == null)
            {
                Logger.LogError("Could not recreate value");

                out.writeObject("F");
                out.flush();
                return;
            }

            Logger.LogInfo(v.toString());

            // store value in the correct chat
            List<Topic> topics = broker.getChats();

            String topic = v.GetMetaData().getTopicName();
            Optional<Topic> chat = topics.stream().
                    filter(p -> p.name.equals(topic)).
                    findFirst();

            if(chat.isPresent()) {
                chat.get().addPost(v);
            }

            out.writeObject("OK");
            out.flush();
        }

        private void SendChat(String topic)
        {

        }


    }

    private class BrokerReceiverAction implements ReceiverAction
    {
        private final Broker broker;

        public BrokerReceiverAction(Broker broker)
        {
            this.broker = broker;
        }

        @Override
        public void HandleConnection(Socket s)
        {
            Thread t = new ActionForClients(s, broker);
            t.start();
        }

    }

}
