package com.company.EventDeliverySystem;

import com.company.utilities.FileChunk;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Publisher
{

    public void run()
    {
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try
        {
            requestSocket = new Socket("127.0.0.1", 4321);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            ArrayList<FileChunk> chunks = FileChunk.Load("CG_Project.mp4");

            out.writeInt(chunks.size());
            out.writeObject("CG_Project_copy.mp4");

            for(int i = 0; i < chunks.size(); i++)
            {
                out.writeObject(chunks.get(i));
                out.flush();
            }

            //System.out.println("Server>" + in.read);

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                in.close();	out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
