package com.company.EventDeliverySystem;

public class Logger
{
    public static void LogInfo(String msg)
    {
        Get().Log("[Info]", msg);
    }

    public static void LogError(String msg)
    {
        Get().Log("[Error]", msg);
    }

    public void Log(String tag, String msg)
    {
        String output = tag + ": " + msg;
        //System.err.println(output);
    }

    private Logger()
    {

    }

    private static Logger theLogger = null;
    private static Logger Get()
    {
        if (theLogger == null) {
            theLogger = new Logger();
        }

        return theLogger;
    }
}
