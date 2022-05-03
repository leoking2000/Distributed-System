package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.Value;
import com.company.utilities.Logger;
import com.company.utilities.SenderAction;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Topic
{
    public final String name;
    public final List<Value> values;
    public final List<Address> registeredUsers;

    public Topic(String name)
    {
        this.name = name;
        // we get a synchronizedList because BrokerActionsForClients thread modifies it
        values = Collections.synchronizedList( new ArrayList<Value>() );
        registeredUsers = Collections.synchronizedList(new ArrayList<Address>());
    }

    void addPost(Value v)
    {
        values.add(v);
    }

    void SendToRegisterUsers()
    {

    }

    void printChat()
    {
        for(Value v : values)
        {
            Logger.LogInfo(v.toString());
        }

    }

    private class TopicSenderAction implements SenderAction
    {
        @Override
        public void Send(Object v, ObjectInputStream in, ObjectOutputStream out) {

        }
    }
}
