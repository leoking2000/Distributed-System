package com.company.main;

import com.company.EventDeliverySystem.Broker;

public class BrokerMain
{
    public static void main(String[] args)
    {
        Logger.Init("Broker_log.txt");

        Broker b = new Broker(BrokerAddressList.Get(Integer.parseInt(args[0])));
        b.start();


        Logger.Close();
    }
}