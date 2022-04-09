package com.company.main;

import com.company.utilities.*;
import com.company.EventDeliverySystem.*;

public class Main
{

    public static void main(String[] args)
    {
        if(args[0].equals("Broker"))
        {
            Logger.Init("Broker_log.txt");

            Broker b = new Broker(new Address("127.0.0.1", 4321));

            b.start();

        }
        else if (args[0].equals("user"))
        {
            Logger.Init("user_log.txt");

            new Publisher().run();
        }
        else
        {
            throw new RuntimeException("Please specify role (server/user)");
        }

        Logger.Close();

    }
}
