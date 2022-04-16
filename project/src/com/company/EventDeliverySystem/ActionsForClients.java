package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.FileChunk;
import com.company.EventDeliverySystem.ValueTypes.MetaData;
import com.company.EventDeliverySystem.ValueTypes.Value;
import com.company.utilities.Logger;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActionsForClients extends Thread
{
    ObjectInputStream in;
    ObjectOutputStream out;
    List<Chat> chats;

    public ActionsForClients(Socket connection, List<Chat> chats)
    {
        try
        {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());

            this.chats = chats;

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
            String request = (String) in.readObject();

            switch (request)
            {
                case "Accept Value":
                    AcceptValue();
                    break;
                case "pull":
                    String topic = (String) in.readObject();
                    SendChat(topic);
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
                out.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }// run


    private void SendChat(String topic)
    {

    }


    private void AcceptValue() throws IOException, ClassNotFoundException {
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

            out.writeObject("F");
            out.flush();
            return;
        }

        DebugLog(v);

        String topic = v.GetMetaData().getTopicName();

        Optional<Chat> chat = chats.stream().
                filter(p -> p.getName().equals(topic)).
                findFirst();

        if(chat.isPresent()) {
            chat.get().addPost(v);
        }
        else
        {
            Chat c = new Chat(topic);
            c.addPost(v);
            chats.add(c);
        }

        out.writeObject("OK");
        out.flush();
    }
}
