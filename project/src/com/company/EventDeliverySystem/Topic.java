package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.Value;

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

    List<Address> GetRegisterUsers()
    {
        return registeredUsers;
    }

    void printChat()
    {
        for(Value v : values)
        {
            Logger.LogInfo(v.toString());
        }

    }

}
