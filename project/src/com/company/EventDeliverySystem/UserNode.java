package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.Text;

import java.util.Scanner;

public class UserNode
{
    private final String user_name;

    public UserNode(String name)
    {
        user_name = name;

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

            Publisher p = new Publisher(new Text(text, user_name, topic));
            p.start();
            try {
                p.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //in.nextLine();
        }

    }



}
