package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Consumer extends Thread
{
    private Configuration config;

    public List<ConsumerTopic> topics;

    public Consumer(Configuration config)
    {
        this.config = config;
        topics = Collections.synchronizedList(new ArrayList<>());
    }

    public void run()
    {
        ServerSocket providerSocket = null;

        Logger.LogInfo("Consumer is Running.");

        try
        {
            Socket connection = null;
            providerSocket = new ServerSocket(2251, 10);

            while (true)
            {
                connection = providerSocket.accept();

                Logger.LogInfo("Broker with port has Accepted connection.");
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
        RegisterToTopic action = new RegisterToTopic(topic);

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

        private Consumer consumer;

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

                ArrayList<FileChunk> chunks = new ArrayList<>();

                for(int i = 0; i < metaData.getNumberOfChunks(); i++)
                {
                    chunks.add((FileChunk) in.readObject());
                }

                consumer.FindTopic(metaData.getTopicName()).values.add(Value.ReCreate(chunks, metaData));

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

        public RegisterToTopic(String topic)
        {
            this.topic = topic;
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
                }

            }
            catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }


        }
    }



}
