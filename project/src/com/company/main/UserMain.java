package com.company.main;

import com.company.EventDeliverySystem.*;

public class UserMain
{

    public static void main(String[] args)
    {
        String userName = args[0];
        int id = Integer.parseInt(args[1]);
        String ip_broker = args[2];
        int port = Integer.parseInt(args[3]);

        UserNode u = new UserNode(id, userName, new Address(ip_broker, port));
        u.run();


    }
}
