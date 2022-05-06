package com.company.EventDeliverySystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/*
    A Sender can Send stuff to an Address.
*/
public class Sender extends Thread
{
    private final Address receiverAddress;
    private final SenderAction action;

    private Socket requestSocket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    public Sender(Address receiver, SenderAction action)
    {
        this.receiverAddress = receiver;
        this.action = action;

        Logger.LogInfo("Sender to send to " + receiver + " was created.");
    }

    public void run()
    {
        try
        {
            // create the connection
            requestSocket = new Socket(receiverAddress.getIp(), receiverAddress.getPort());
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            action.Send(in, out);

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
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
