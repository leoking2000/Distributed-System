package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;
import com.company.utilities.Logger;
import com.company.utilities.Sender;
import com.company.utilities.SenderAction;

import java.util.ArrayList;
import java.io.*;

public class Publisher
{
    public void push(Value value)
    {
        // get topic name
        String topic = value.GetMetaData().getTopicName();
        // get the address of the broker that is responsible for this topic
        Address broker = GetBrokerAddress(topic);

        Sender sender = new Sender(value, broker, new PublisherAction());
        sender.start();
    }

    private class PublisherAction implements SenderAction
    {
        @Override
        public void Send(Object v, ObjectInputStream in, ObjectOutputStream out)
        {
            try {
                // tells the broker that it is going to receive a value
                out.writeObject("Accept Value");
                out.flush();

                // send metadata
                //out.writeObject(v.GetMetaData());
                out.flush();

                // send chunks
                //ArrayList<FileChunk> chunks = v.GenerateChunks();
                //for (FileChunk chunk : chunks) {
                    //out.writeObject(chunk);
                    //out.flush();
                //}

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
