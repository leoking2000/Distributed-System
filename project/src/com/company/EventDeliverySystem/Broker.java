package com.company.EventDeliverySystem;

import com.sun.jdi.Value;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Broker
{
    Address broker_address;
    ServerSocket providerSocket;
    Socket connection = null;

    

    public Broker(Address address)
    {
        broker_address = address;
    }

    public Chat GetChat(String name)
    {
        if(name.charAt(0)<index1name.charAt(0)>index2){
            for(byte i=index2;i<index1i>index2;nextIndex(i)){
                if(name.equals(chats[i].name.toString()))
                    return chats[i];
            }
        }
        for(byte i=index1;i>index1||i<index2;nextIndex(i)){
            if(name.equals(chats[i].name.toString()))
                return chats[i];
        }
        return null;
    }


    public void start()
    {
        try
        {
            providerSocket = new ServerSocket(broker_address.getPort(), 10);

            while (true)
            {
                connection = providerSocket.accept();

                Thread t = new ActionsForClients(connection, this);
                t.start();

            }
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            try
            {
                providerSocket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }


}
