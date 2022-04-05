package com.company.main;

import com.company.utilities.FileChunk;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

public class Publisher implements Node
{
    @Override
    public void connect() {

    }

    @Override
    public void init(int k) {

    }

    @Override
    public void updateNodes() {

    }

    ArrayList<FileChunk> generateChunks(String filename)
    {
        return null;
    }

    Broker HashTopic(String topic)
    {
        return null;
    }

    void push(String topic, ArrayList<FileChunk> value)
    {

    }

}
