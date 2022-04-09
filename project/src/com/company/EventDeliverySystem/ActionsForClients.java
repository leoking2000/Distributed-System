package com.company.EventDeliverySystem;

import com.company.utilities.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ActionsForClients extends Thread
{
    ObjectInputStream in;
    ObjectOutputStream out;

    public ActionsForClients(Socket connection)
    {
        try
        {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
            System.out.println("ActionsForClients| created");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            int numberOfChunks = in.readInt();
            String name = (String)in.readObject();

            ArrayList<FileChunk> chunks = new ArrayList<>();

            for(int i = 0; i < numberOfChunks; i++)
            {
                chunks.add((FileChunk) in.readObject());
            }

            FileChunk.Store("../" + name, chunks);
            System.out.println("Store " + name);

            //out.writeObject("name");
            //out.flush();
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
}
