package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.Text;

import java.util.Scanner;

public class UserNode
{
    private final String user_name;
    private final Publisher publisher;
    private final Consumer consumer;

    public UserNode(String name)
    {
        Logger.LogInfo("UserNode for user " + name + " was created.");
        user_name = name;
        publisher = new Publisher();
        consumer = new Consumer();


    }

    public void start()
    {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter topic: ");
        String topic = in.nextLine();

        while (true)
        {
            System.out.print("Enter text: ");
            String text = in.nextLine();

            publisher.push(new Text(text, user_name, topic));
        }

    }

    String getUserName()
    {
        return user_name;
    }

}
