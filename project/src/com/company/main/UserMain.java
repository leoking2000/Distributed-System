package com.company.main;

import com.company.EventDeliverySystem.ValueTypes.Text;
import com.company.utilities.*;
import com.company.EventDeliverySystem.*;

public class UserMain
{

    public static void main(String[] args)
    {

        Logger.Init("user_log.txt");

        int port1 = BrokerAddressList.Get(0).getPort();
        int port2 = BrokerAddressList.Get(1).getPort();

        new Publisher(new Text("Hello", "leoking2000", "aueb"), port1).start();
        new Publisher(new Text("aueb is the best university in the whole world!!!!", "leoking2000", "aueb"), port1).start();
        new Publisher(new Text("Sending video", "leoking2000", "aueb"), port2).start();
        //new Publisher(new MultiMediaFile("CG_Project.mp4", "leoking2000", "aueb")).start();
        new Publisher(new Text("lalalal lalala lalalala", "leoking2000", "aueb"), port2).start();

        Logger.Close();

    }
}
