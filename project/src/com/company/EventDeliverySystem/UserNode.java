package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.Text;

import java.util.Scanner;

public class UserNode
{
    private final String user_name;

    private Publisher publisher;

    public UserNode(String name)
    {
        user_name = name;
        publisher = new Publisher(this);
    }

    public void start()
    {
        Scanner in = new Scanner(System.in);

        while (true)
        {
            System.out.print("Enter topic: ");
            String topic = in.nextLine();

            System.out.print("Enter text: ");
            String text = in.nextLine();

            publisher.push(new Text(text, user_name, topic));

            in.nextLine();
        }

    }

    String getUserName()
    {
        return user_name;
    }

}
