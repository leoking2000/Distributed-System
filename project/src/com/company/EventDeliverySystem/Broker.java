package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;
import java.io.*;
import java.net.*;
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
        config = new Configuration("config.txt");

        Logger.Log("INFO","Broker " + id + " was created.");

        // we get a synchronizedList because ActionsForClients thread modifies it
        topics = Collections.synchronizedList( new ArrayList<Topic>() );

        // create the Topics
        ArrayList<String> topics_names = config.GetTopicsOfBroker(id);
        for(String name : topics_names)
        {
            topics.add(new Topic(name));
            Logger.Log("INFO","Topic: " + name);
        }
    }

    public void run()
    {
        Address broker_address = config.GetAddressOfBroker(id);
        ServerSocket providerSocket = null;

        Logger.Log("INFO","Broker is Running.");

        try
        {
            Socket connection = null;
            providerSocket = new ServerSocket(broker_address.getPort(), 10);

            while (true)
            {
                connection = providerSocket.accept();

                Thread t = new ActionForClients(connection, this);
                t.start();
            }
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            try
            {
                providerSocket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }


    }

    public List<Topic> getTopicsList()
    {
        return topics;
    }


    private class ActionForClients extends Thread
    {
        Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        private Broker broker;

        public ActionForClients(Socket s, Broker b)
        {
            try
            {
                socket = s;
                out = new ObjectOutputStream(s.getOutputStream());
                in = new ObjectInputStream(s.getInputStream());
                Logger.Log("INFO","Broker has Opened connection.");
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
                    case "send config":
                        SendConfig();
                        break;
                    case "accept value":
                        AcceptValue();
                        break;
                    case "register topic":
                        {
                            String topic = (String) in.readObject();
                            RegisterToTopic(topic);
                        }
                        break;
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
            Logger.Log("INFO", "Broker Has Send config!!!");
        }


        private void AcceptValue() throws IOException, ClassNotFoundException
        {
            Logger.Log("INFO", "AcceptValue");

            // read metadata
            MetaData metaData = (MetaData) in.readObject();

            Logger.Log("INFO", metaData.toString());

            // get the topic
            Topic topic = GetTopic(metaData.getTopicName());

            ArrayList<FileChunk> chunks = new ArrayList<>();
            Socket[] consumerConnections = new Socket[topic.registeredUsers.size()];
            ObjectOutputStream[] out_streams = new ObjectOutputStream[topic.registeredUsers.size()];

            try
            {
                // create the connection to the consumers
                Logger.Log("INFO", "Sending data to " + topic.registeredUsers.size() + "users");
                for(int c = 0; c < topic.registeredUsers.size(); c++)
                {
                    Address a = topic.registeredUsers.get(c);
                    Logger.Log("INFO", "Sending to address" + a.toString());

                    consumerConnections[c] = new Socket(a.getIp(), a.getPort());
                    out_streams[c] = new ObjectOutputStream(consumerConnections[c].getOutputStream());

                    // send metadata
                    out_streams[c].writeObject(metaData);
                    out_streams[c].flush();
                }

                // read, store and send the file chunks
                for(int i = 0; i < metaData.getNumberOfChunks(); i++)
                {
                    // read the fileChunk
                    FileChunk chunk = (FileChunk) in.readObject();
                    chunks.add(chunk); // store the chunk

                    // send the fileChunk
                    for(int c = 0; c < topic.registeredUsers.size(); c++)
                    {
                        out_streams[c].writeObject(chunk);
                        out_streams[c].flush();
                    }
                }


            } catch (UnknownHostException unknownHost) {
                System.err.println("You are trying to connect to an unknown host!");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            // close the connection to consumer
            try {
                for(int i = 0; i < topic.registeredUsers.size(); i++)
                {
                    out_streams[i].close();
                    consumerConnections[i].close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            // recreate value
            Value v = Value.ReCreate(chunks, metaData);
            topic.addPost(v);

            Logger.Log("INFO", "Value has been handle!!!");
        }

        private void RegisterToTopic(String topicName) throws IOException, ClassNotFoundException
        {

            Topic topic = GetTopic(topicName);

            InetAddress addr = socket.getInetAddress();
            int         port = socket.getPort();
            Address user_address = new Address(addr.getHostAddress(), in.readInt());

            Logger.Log("INFO", "user " + user_address.toString() + "wand to be registered");

            if(topic.registeredUsers.stream().noneMatch(a -> a.equals(user_address)))
            {
                topic.registeredUsers.add(user_address);
            }


            out.writeInt(topic.values.size());
            out.flush();

            for(Value v : topic.values)
            {
                out.writeObject(v.GetMetaData());
                out.flush();

                ArrayList<FileChunk> chunks = v.GenerateChunks();

                for(int i = 0; i < chunks.size(); i++)
                {
                    out.writeObject(chunks.get(i));
                    out.flush();
                }

            }

            Logger.Log("INFO", "Register to topic has been handle!!!");
        }

        private Topic GetTopic(String topicName)
        {
            // get the topic
            List<Topic> topics = broker.getTopicsList();
            Topic topic;
            synchronized (topics)
            {
                topic = (topics.stream().
                        filter(p -> p.name.equals(topicName)).
                        findFirst()).get();
            }

            return topic;
        }

    }
}
