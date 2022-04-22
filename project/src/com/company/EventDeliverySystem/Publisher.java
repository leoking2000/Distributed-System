package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;
import com.company.main.BrokerAddressList;
import com.company.main.Logger;

import java.util.ArrayList;
import java.io.*;
import java.net.*;


/*
posts something to a topic
 */
public class Publisher extends Thread
{

    // variables to make the connection to the broker
    private Socket requestSocket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    // the value we want to send
    private final Value value;

    public Publisher(Value value)
    {
        this.value = value;
    }


    // sends the value to a broker
    private void Send(Value value) throws IOException, ClassNotFoundException
    {
        // tells the broker that it is going to receive a value
        out.writeObject("Accept Value");
        out.flush();

        // send metadata
        out.writeObject(value.GetMetaData());
        out.flush();

        // send chunks
        ArrayList<FileChunk> chunks = value.GenerateChunks();
        for (FileChunk chunk : chunks)
        {
            out.writeObject(chunk);
            out.flush();
        }

        try{
            String respond = (String) in.readObject();
            Logger.LogInfo("Server respond " + respond);
        }
        catch (EOFException ignored)
        {
            Logger.LogError("Server did not give ");
        }
    }

    private Address GetBrokerAddress(String topic)
    {
        // get the hash code of the topic
        int h = topic.hashCode();
        // mod the hash to get the index
        int index = h % BrokerAddressList.NumberOfBrokers();

        // return the address
        return BrokerAddressList.Get(index);
    }

    public void run()
    {
        try
        {
            // get topic name
            String topic = value.GetMetaData().getTopicName();
            // get the address of the broker that is responsible for this topic
            Address broker = GetBrokerAddress(topic);

            // get the connection
            requestSocket = new Socket(broker.getIp(), broker.getPort());
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            Logger.LogInfo("Sending to address <" + broker + ">~ " + value);

            Send(value);

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        try {
            in.close();	out.close();
            requestSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
