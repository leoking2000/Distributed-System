package com.company.main;

import com.company.EventDeliverySystem.Broker;
import com.company.EventDeliverySystem.Configuration;

public class BrokerMain
{
    public static void main(String[] args)
    {
        Broker b = new Broker(Integer.parseInt(args[0]));
        b.run();
    }
}
