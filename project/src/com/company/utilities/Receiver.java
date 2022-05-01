package com.company.utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
    A Receiver WAITS until it receives a connection from a port and calls the ReceiverAction.
 */
public class Receiver
{
    private final int port;
    private ServerSocket providerSocket;

    private final ReceiverAction action;

    public Receiver(int port, ReceiverAction action)
    {
        this.port = port;
        this.action = action;
        Logger.LogInfo("Receiver with port " + port + " was created.");
    }

    public void run()
    {
        Logger.LogInfo("Receiver with port " + port + " is Running.");
        try
        {
            Socket connection = null;
            providerSocket = new ServerSocket(port, 10);

            while (true)
            {
                connection = providerSocket.accept();

                Logger.LogInfo("Receiver with port " + port + " has Accepted connection.");
                action.HandleConnection(connection);
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
