package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.FileChunk;
import com.company.EventDeliverySystem.ValueTypes.MetaData;
import com.company.EventDeliverySystem.ValueTypes.Value;
import com.company.utilities.Logger;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ActionsForClients extends Thread
{
    ObjectInputStream in;
    ObjectOutputStream out;
    Broker b;

    public ActionsForClients(Socket connection, Broker b)
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

    public void DebugLog(Value value)
    {
        Logger.LogInfo(value.toString());
    }


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

            Value v = Value.ReCreate(chunks, metaData);

            if(v == null)
            {
                Logger.LogError("Could not recreate value");
            }
            else {
                DebugLog(v);

                String topic = v.GetMetaData().getTopicName();
                b.GetChat(topic).addPost(v);
            }

            out.writeObject("OK");
            out.flush();

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
