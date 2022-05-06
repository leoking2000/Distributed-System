package com.company.EventDeliverySystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Configuration implements Serializable
{
    private Address[] brokersAddress;
    private String[] topics;

    public Configuration(String configFile_path)
    {
        File configFile = new File(configFile_path);
        Scanner in;
        try {
            in = new Scanner(configFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int broker_index = 0;
        int topic_index = 0;

        while (in.hasNextLine())
        {
            String line = in.nextLine();

            if(line.contains("Number of Brokers:"))
            {
                int numberOfBrokers = Integer.parseInt(line.split(" ")[3]);
                brokersAddress = new Address[numberOfBrokers];
            }
            else if(line.contains("Number of Topics:"))
            {
                int numberOfTopics = Integer.parseInt(line.split(" ")[3]);
                topics = new String[numberOfTopics];
            }
            else if (line.contains("Broker:"))
            {
                String[] broker = line.split(" ");
                String ip = broker[1];
                int port = Integer.parseInt(broker[2]);
                brokersAddress[broker_index] = new Address(ip, port);
                broker_index++;
            }
            else if(line.contains("Topics:"))
            {
                topics[topic_index] = line.split(" ")[1];
                topic_index++;
            }

        }
    }

    public Address GetBrokerAddress(String topic)
    {
        if(!exists(topic)) return null;

        // get the hash code of the topic
        int h = Math.abs(topic.hashCode());
        // mod the hash to get the index
        int index = h % brokersAddress.length;

        // return the address
        return brokersAddress[index];
    }

    public Address GetAddressOfBroker(int id)
    {
        return brokersAddress[id];
    }

    public String[] GetTopics()
    {
        return topics;
    }

    public ArrayList<String> GetTopicsOfBroker(int id)
    {
        Address b = brokersAddress[id];

        ArrayList<String> t = new ArrayList<String>();

        for (String s : topics)
        {
            if (GetBrokerAddress(s) == b)
            {
                t.add(s);
            }
        }

        return t;
    }

    public boolean exists(String topic)
    {
        for (String s : topics)
        {
            if (s.equals(topic)) return true;
        }

        return false;
    }

}
