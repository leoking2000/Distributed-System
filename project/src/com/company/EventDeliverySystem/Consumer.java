package com.company.EventDeliverySystem;

import com.company.utilities.*;

import java.net.Socket;

public class Consumer extends Thread
{
    public Receiver receiver;

    public Consumer()
    {
        receiver = new Receiver(5555, new ConsumerAction());
    }

    public void Register(String topic)
    {

    }

    public void Disconnect(String topic)
    {

    }

    @Override
    public void run()
    {
        receiver.run();
    }


    private class ConsumerAction implements ReceiverAction
    {
        @Override
        public void HandleConnection(Socket s)
        {

        }
    }

}
