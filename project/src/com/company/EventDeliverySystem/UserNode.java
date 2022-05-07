package com.company.EventDeliverySystem;

import com.company.EventDeliverySystem.ValueTypes.Text;
import com.company.EventDeliverySystem.ValueTypes.Value;

import java.util.Scanner;

public class UserNode
{
    public final String user_name;
    public Configuration config = null;

    private final Publisher publisher;
    private final Consumer consumer;

    public UserNode(int id, String name, Address broker_address)
    {
        user_name = name;

        publisher = new Publisher();

        config = publisher.getConfiguration(broker_address);
        consumer = new Consumer(config, id);
        consumer.start();

        Logger.LogInfo("UserNode for user " + name + " was created.");
    }

    public void run()
    {
        Scanner in = new Scanner(System.in);

        System.out.print("Topics: ");
        for(String name : config.GetTopics())
        {
            System.out.println(name);
        }

        System.out.print("Enter topic: ");
        String topicname = in.nextLine();
        consumer.Register(topicname);

        Consumer.ConsumerTopic topic = consumer.FindTopic(topicname);

        while (true)
        {
            for(Value v : topic.values)
            {
                System.out.println(v);
            }

            System.out.print("Enter text: ");
            String text = in.nextLine();

            publisher.Send(new Text(text, user_name, topicname));

            //in.nextLine();
            // clear the console
            //System.out.print("\033[H\033[2J");
            //System.out.flush();
        }

    }

}
