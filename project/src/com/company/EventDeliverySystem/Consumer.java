package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Consumer extends Thread
{
    private Configuration config;

    public List<ConsumerTopic> topics;

    private final int port;

    public Consumer(Configuration config, int id)
    {
        this.config = config;
        topics = Collections.synchronizedList(new ArrayList<>());

        port = 2250 + id;
    }

    public void run()
    {
        ServerSocket providerSocket = null;

        try
        {
            Socket connection = null;
            providerSocket = new ServerSocket(port, 10);

            while (true)
            {
                connection = providerSocket.accept();

                Thread t = new ActionNewValue(connection, this);
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

    public void Register(String topic)
    {
        Address broker = config.GetBrokerAddress(topic);
        RegisterToTopic action = new RegisterToTopic(topic, port);

        Sender s = new Sender(broker, action);
        s.start();

        try {
            s.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        topics.add(new ConsumerTopic(topic, new ArrayList<Value>(List.of(action.values))));

    }

    public ConsumerTopic FindTopic(String name)
    {
        return topics.stream().filter(t -> t.name.equals(name)).findFirst().get();
    }

    public static class ConsumerTopic
    {
        public String name;
        public ArrayList<Value> values;

        ConsumerTopic(String name, ArrayList<Value> values)
        {
            this.name = name;
            this.values = values;
        }
    }

    private static class ActionNewValue extends Thread
    {
        Socket socket;
        private ObjectInputStream in;

        private final Consumer consumer;

        public ActionNewValue(Socket s, Consumer c)
        {
            try
            {
                socket = s;
                in = new ObjectInputStream(s.getInputStream());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            consumer = c;
        }

        @Override
        public void run()
        {
            try
            {
                MetaData metaData = (MetaData) in.readObject();

                Logger.Log("Consumer", "has accepted new value with \n" + metaData.toString());

                ArrayList<FileChunk> chunks = new ArrayList<>();

                for(int i = 0; i < metaData.getNumberOfChunks(); i++)
                {
                    chunks.add((FileChunk) in.readObject());
                }

                Value v = Value.ReCreate(chunks, metaData);
                consumer.FindTopic(metaData.getTopicName()).values.add(v);

                // if the value is a MultiMediaFile
                if(v instanceof MultiMediaFile)
                {
                    // store the file
                    FileChunk.Store(metaData.getFilename(), chunks);
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
                }
                catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }
            }

        }

    }

    private static class RegisterToTopic implements SenderAction
    {
        public String topic;

        public Value[] values;

        public final int port;

        public RegisterToTopic(String topic, int port)
        {
            this.topic = topic;
            this.port = port;
        }

        @Override
        public void Send(ObjectInputStream in, ObjectOutputStream out)
        {
            try
            {
                out.writeObject("register topic");
                out.flush();

                out.writeObject(topic);
                out.flush();

                out.writeInt(port);
                out.flush();

                int numberOfValues = in.readInt();
                values = new Value[numberOfValues];

                for(int i = 0; i < numberOfValues; i++)
                {
                    MetaData metaData = (MetaData) in.readObject();

                    ArrayList<FileChunk> chunks = new ArrayList<>();

                    for(int c = 0; c < metaData.getNumberOfChunks(); c++)
                    {
                        chunks.add((FileChunk) in.readObject());
                    }

                    values[i] = Value.ReCreate(chunks, metaData);

                    // if the value is a MultiMediaFile
                    if(values[i] instanceof MultiMediaFile)
                    {
                        // store the file
                        FileChunk.Store(metaData.getFilename(), chunks);
                    }
                }

            }
            catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }


        }
    }



}
