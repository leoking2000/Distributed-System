package com.company.main;

import com.company.utilities.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main
{

    public static void main(String[] args)
    {
        Logger.LogInfo("Start info");
        Logger.LogError("aaaaaaaaAAAAA");
        Logger.Close();

        try
        {
            ArrayList<FileChunk> log_file = FileChunk.Load("CG_Project.mp4");
            FileChunk.Store("CG_Project_copy.mp4",log_file);

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
