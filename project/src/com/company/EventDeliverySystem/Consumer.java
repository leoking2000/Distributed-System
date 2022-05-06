package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Consumer extends Thread
{
    private Configuration config;

    public ArrayList<ConsumerTopic> topics;

    public Consumer(Configuration config)
    {
        this.config = config;
        topics = new ArrayList<>();
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
