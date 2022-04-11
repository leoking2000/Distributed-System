package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.*;
import com.company.utilities.Logger;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Publisher extends Thread
{
    Address address;
    Socket requestSocket = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;

    Value value;

    public Publisher(Value value)
    {
        this.address = new Address("127.0.0.1", 8989);
        this.value = value;
    }


    public void Send(Value value) throws IOException, ClassNotFoundException {
        Logger.LogInfo("Sending... " + value.toString());

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
            String responed = (String) in.readObject();
            Logger.LogInfo("Server responed " + responed);
        }
        catch (EOFException ignored)
        {

        }
    }


    public void run()
    {
        try
        {
            requestSocket = new Socket(address.getIp(), address.getPort());
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            Send(value);

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                in.close();	out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
