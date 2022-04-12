package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.Value;
import com.company.utilities.Logger;

import java.util.ArrayList;

public class Chat
{
    ArrayList<Value> data;
    char[] name; // ??? giati oxi
    byte BrokerId;


    Chat(){
        data = new ArrayList<Value>();
        BrokerId=(byte)(this.hashCode()%10);
        name=new char[11];
        name[0]=(char) BrokerId;
    }

    void addPost(Value v){
        data.add(v);
    }

    void printChat(){

        for(Value v : data)
        {
            Logger.LogInfo(v.toString());
        }

    }
}
