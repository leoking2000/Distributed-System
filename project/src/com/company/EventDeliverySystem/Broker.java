package com.company.EventDeliverySystem;

import com.company.utilities.Logger;
import com.company.utilities.Receiver;

import java.io.*;
import java.net.*;
import java.util.*;

public class Broker
{
    private final Address broker_address;
    private List<Chat> chats; // store all the chats

    public Broker(Address address)
    {
        // we get a synchronizedList because ActionsForClients thread modifies it
        chats = Collections.synchronizedList( new ArrayList<Chat>() );
        broker_address = address;

        Logger.LogInfo("Broker with address " + address + " was created.");
    }

    public void start()
    {
        Receiver receiver = new Receiver(broker_address.getPort(), new BrokerActionForClients(this));
        receiver.run();
    }

    public List<Chat> getChats()
    {
        return chats;
    }

}
