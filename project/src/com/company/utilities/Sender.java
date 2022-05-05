package com.company.utilities;

import com.company.EventDeliverySystem.Address;
import com.company.EventDeliverySystem.Logger;

import java.io.*;
import java.net.*;

/*
    A Sender can Send stuff to an Address.
 */
public class Sender extends Thread
{
    private final Address receiverAddress;
    private final SenderAction action;
    private final Object value;

    private Socket requestSocket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    public Sender(Object value, Address receiver, SenderAction action)
    {
        this.receiverAddress = receiver;
        this.action = action;
        this.value = value;

        Logger.LogInfo("Sender to send " + value + " to " + receiver + " was created.");
    }

    public void run()
    {
        try
        {
            // create the connection
            requestSocket = new Socket(receiverAddress.getIp(), receiverAddress.getPort());
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            //Logger.LogInfo("Sending to address <" + receiverAddress + ">~ " + value);
            action.Send(value, in, out);
            //Logger.LogInfo( value + " was Send to " + receiverAddress);

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
