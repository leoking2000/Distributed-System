package com.company.EventDeliverySystem;

public class Logger
{
    public static void Log(String tag, String msg)
    {
        String output = tag + ": " + msg;
        System.out.println(output);
    }
}
