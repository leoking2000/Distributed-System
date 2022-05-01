package com.company.utilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Logger
{
    public static void Init(String logFile)
    {
        try {
            Get().writer = new PrintWriter(logFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LogInfo(String msg)
    {
        Get().Log("[Info]", msg);
    }

    public static void LogError(String msg)
    {
        Get().Log("[Error]", msg);
    }

    public static void Close()
    {
        Get().writer.close();
    }

    public void Log(String tag, String msg)
    {
        String output = tag + ": " + msg;
        writer.println(output);
        System.err.println(output);
    }

    private PrintWriter writer = null;

    private Logger()
    {

    }

    private static Logger theLogger;
    private static Logger Get()
    {
        if (theLogger == null) {
            theLogger = new Logger();
        }

        return theLogger;
    }
}
