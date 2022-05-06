package com.company.main;

import com.company.EventDeliverySystem.*;

public class UserMain
{

    public static void main(String[] args)
    {
        String userName = args[0];
        String ip_broker = args[1];
        int port = Integer.parseInt(args[2]);

        UserNode u = new UserNode(userName, new Address(ip_broker, port));
        u.run();


    }
}
