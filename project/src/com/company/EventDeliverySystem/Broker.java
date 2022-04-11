package com.company.EventDeliverySystem;

import java.io.*;
import java.net.*;

public class Broker
{
    Address broker_address;
    ServerSocket providerSocket;
    Socket connection = null;

    public Broker(Address address)
    {
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

                Thread t = new ActionsForClients(connection);
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
