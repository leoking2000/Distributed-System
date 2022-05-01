package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;
import com.company.main.BrokerAddressList;
import com.company.utilities.Logger;
import com.company.utilities.Sender;
import com.company.utilities.SenderAction;

import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Publisher
{
    private final UserNode user;

    public Publisher(UserNode user)
    {
        this.user = user;
        Logger.LogInfo("Publisher for user " + user.getUserName() + " was created.");
    }

    private Address GetBrokerAddress(String topic)
    {
        // TODO make this not need BrokerAddressList!!!
        // get the hash code of the topic
        int h = topic.hashCode();
        // mod the hash to get the index
        int index = h % BrokerAddressList.NumberOfBrokers();

        // return the address
        return BrokerAddressList.Get(index);
    }

    public void push(Value value)
    {
        // get topic name
        String topic = value.GetMetaData().getTopicName();
        // get the address of the broker that is responsible for this topic
        Address broker = GetBrokerAddress(topic);

        Sender sender = new Sender(value, broker, new PublisherAction());
        sender.start();
    }

    private class PublisherAction extends Thread implements SenderAction
    {
        private Value v;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        @Override
        public void Send(Value v, ObjectInputStream in, ObjectOutputStream out)
        {
            this.v = v;
            this.in = in;
            this.out = out;
            // start this thread
            this.start();
        }
        @Override
        public void run()
        {
            try {
                // tells the broker that it is going to receive a value
                out.writeObject("Accept Value");
                out.flush();

                // send metadata
                out.writeObject(v.GetMetaData());
                out.flush();

                // send chunks
                ArrayList<FileChunk> chunks = v.GenerateChunks();
                for (FileChunk chunk : chunks) {
                    out.writeObject(chunk);
                    out.flush();
                }

                try {
                    String respond = (String) in.readObject();
                    Logger.LogInfo("Server respond " + respond);
                } catch (EOFException ignored) {
                    Logger.LogError("Server did not give ");
                }
            }catch (IOException | ClassNotFoundException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }

}
