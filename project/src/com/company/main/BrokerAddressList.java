package com.company.main;

import com.company.EventDeliverySystem.Address;

public class BrokerAddressList
{
    private final Address[] list = new Address[3];

    private BrokerAddressList()
    {
        list[0] = new Address("127.0.0.1", 2341);
        list[1] = new Address("127.0.0.1", 2342);
        list[2] = new Address("127.0.0.1", 2343);
    }

    public static Address GetBrokerAddress(String topic)
    {
        // get the hash code of the topic
        int h = topic.hashCode();
        // mod the hash to get the index
        int index = h % BrokerAddressList.NumberOfBrokers();

        // return the address
        return BrokerAddressList.Get(index);
    }

    private static BrokerAddressList instance = null;

    public static Address Get(int i)
    {
        if(instance == null){ instance = new BrokerAddressList(); }

        return instance.list[i];
    }

    public static int NumberOfBrokers()
    {
        if(instance == null){ instance = new BrokerAddressList(); }

        return instance.list.length;
    }


}
