package com.company.main;

import com.company.EventDeliverySystem.UserNode;

public class UserMain
{

    public static void main(String[] args)
    {

        Logger.Init("user_log.txt");

        UserNode u = new UserNode("Leoking2000");
        u.start();

        Logger.Close();

    }
}