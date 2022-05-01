package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.Value;
import com.company.utilities.Logger;

import java.util.*;

/*
a chat or a topic
 */
public class Chat
{
    private final String name;
    private final List<Value> data;

    public Chat(String name)
    {
        this.name = name;
        // we get a synchronizedList because ActionsForClients thread modifies it
        data = Collections.synchronizedList( new ArrayList<Value>() );
    }

    void addPost(Value v)
    {
        data.add(v);
    }

    void printChat()
    {
        for(Value v : data)
        {
            Logger.LogInfo(v.toString());
        }

    }

    public String getName() {
        return name;
    }

}
