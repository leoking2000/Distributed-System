package com.company.main;

import com.company.EventDeliverySystem.ValueTypes.MultiMediaFile;
import com.company.EventDeliverySystem.ValueTypes.Text;
import com.company.utilities.*;
import com.company.EventDeliverySystem.*;

public class Main
{

    public static void main(String[] args)
    {
        if(args[0].equals("Broker"))
        {
            Logger.Init("Broker_log.txt");

            Broker b = new Broker(new Address("127.0.0.2", 8989));
            b.start();

        }
        else if (args[0].equals("user"))
        {
            Logger.Init("user_log.txt");

            new Publisher(new Text("Hello", "leoking2000", "aueb")).start();
            new Publisher(new Text("aueb is the best university in the whole world!!!!", "leoking2000", "aueb")).start();
            new Publisher(new Text("Sending video", "leoking2000", "aueb")).start();
            //new Publisher(new MultiMediaFile("CG_Project.mp4", "leoking2000", "aueb")).start();
            new Publisher(new Text("lalalal lalala lalalala", "leoking2000", "aueb")).start();
        }
        else
        {
            throw new RuntimeException("Please specify role (server/user)");
        }

        Logger.Close();

    }
}
