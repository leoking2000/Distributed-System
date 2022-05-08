package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Publisher
{
    private Configuration config;


    public Configuration getConfiguration(Address broker)
    {
        GetConfiguration action = new GetConfiguration();
        Sender s = new Sender(broker, action);
        s.start();

        try {
            s.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        config = action.config;

        return action.config;
    }

    public void Send(Value value)
    {
        // get topic name
        String topic = value.GetMetaData().getTopicName();
        // get the address of the broker that is responsible for this topic
        Address broker = config.GetBrokerAddress(topic);

        SendValue action = new SendValue(value);
        Sender s = new Sender(broker, action);
        s.start();

        try {
            s.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    private static class GetConfiguration implements SenderAction
    {
        public Configuration config = null;

        @Override
        public void Send(ObjectInputStream in, ObjectOutputStream out)
        {
            try
            {
                out.writeObject("send config");
                out.flush();

                config = (Configuration) in.readObject();
            }
            catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    private static class SendValue implements SenderAction
    {
        private final Value value;

        public SendValue(Value v)
        {
            value = v;
        }

        @Override
        public void Send(ObjectInputStream in, ObjectOutputStream out)
        {
            try {
                // tells the broker that it is going to receive a value
                out.writeObject("accept value");
                out.flush();

                // send metadata
                out.writeObject(value.GetMetaData());
                out.flush();

                // send chunks
                ArrayList<FileChunk> chunks = value.GenerateChunks();
                for (int i = 0; i < value.GetMetaData().getNumberOfChunks(); i++) {
                    out.writeObject(chunks.get(i));
                    out.flush();
                }

            }catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }

}
