package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.Value;
import com.company.main.Logger;

import java.util.*;

public class Chat
{
    private String name;
    private List<Value> data;

    public Chat(String name)
    {
        this.name = name;
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

    public List<Value> getData() {
        return data;
    }
}
