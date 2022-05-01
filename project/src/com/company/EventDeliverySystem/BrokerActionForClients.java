package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.FileChunk;
import com.company.EventDeliverySystem.ValueTypes.MetaData;
import com.company.EventDeliverySystem.ValueTypes.Value;
import com.company.utilities.Logger;
import com.company.utilities.ReceiverAction;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
handles a broker request
 */
public class BrokerActionForClients extends Thread implements ReceiverAction
{
    private final Broker broker;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public BrokerActionForClients(Broker broker)
    {
       this.broker = broker;
    }

    public void DebugLog(Value value)
    {
        Logger.LogInfo(value.toString());
    }

    @Override
    public void HandleConnection(Socket s)
    {
        try
        {
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            // read the request
            String request = (String) in.readObject();

            switch (request) {
                case "Accept Value" -> AcceptValue();
                case "pull" -> {
                    String topic = (String) in.readObject();
                    SendChat(topic);
                }
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
    }


    private void SendChat(String topic)
    {

    }


    private void AcceptValue() throws IOException, ClassNotFoundException
    {
        // read metadata
        MetaData metaData = (MetaData) in.readObject();

        // read the file chunks
        ArrayList<FileChunk> chunks = new ArrayList<>();
        for(int i = 0; i < metaData.getNumberOfChunks(); i++)
        {
            chunks.add((FileChunk) in.readObject());
        }

        // recreate value
        Value v = Value.ReCreate(chunks, metaData);

        if(v == null)
        {
            Logger.LogError("Could not recreate value");

            out.writeObject("F");
            out.flush();
            return;
        }

        DebugLog(v);

        // store value in the correct chat
        List<Chat> chats = broker.getChats();

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
