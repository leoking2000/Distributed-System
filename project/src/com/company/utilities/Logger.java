package com.company.utilities;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

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

    public static void Close()
    {
        Get().writer.close();
    }

    public void Log(String tag, String msg)
    {
        String output = Calendar.getInstance().getTime().toString() + " " + tag + ": " + msg;
        writer.println(output);
        System.out.println(output);
    }

    private PrintWriter writer;

    private Logger()
    {
        try {
            writer = new PrintWriter("log.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
