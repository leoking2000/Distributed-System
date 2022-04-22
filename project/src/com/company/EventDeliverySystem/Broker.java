package com.company.EventDeliverySystem;

import java.io.*;
import java.net.*;
import java.util.*;

public class Broker
{
    Address broker_address;
    ServerSocket providerSocket;
    Socket connection = null;

    List<Chat> chats; // store all the chats

    public Broker(Address address)
    {
        // we get a synchronizedList because ActionsForClients thread modifies it
        chats = Collections.synchronizedList( new ArrayList<Chat>() );
        broker_address = address;
    }

    public void start()
    {
        try
        {
            providerSocket = new ServerSocket(broker_address.getPort(), 10);

            while (true)
            {
                connection = providerSocket.accept();

                Thread t = new ActionsForClients(connection, chats);
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
