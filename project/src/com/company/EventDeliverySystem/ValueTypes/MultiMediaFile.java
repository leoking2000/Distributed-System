package com.company.EventDeliverySystem.ValueTypes;

import com.company.utilities.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class MultiMediaFile implements Value
{
    private byte[] data;
    private final MetaData metadata;

    public MultiMediaFile(ArrayList<FileChunk> chunks, MetaData metaData)
    {
        metadata = metaData;
        data = FileChunk.GetData(chunks);
    }

    public MultiMediaFile(String filepath, String user, String topic)
    {
        int numberOfChunks;

        try
        {
            File file = new File(filepath);
            data = Files.readAllBytes(file.toPath());
            numberOfChunks = data.length;
        }
        catch (IOException e)
        {
            Logger.LogError("MultiMediaFile " + filepath + "Failed to load!!!");
            data = null;
            numberOfChunks = 0;
        }

        metadata = new MetaData(user, topic, "MultiMediaFile", numberOfChunks);
    }

    @Override
    public ArrayList<FileChunk> GenerateChunks() {
        return FileChunk.Generate(data);
    }

    @Override
    public MetaData GetMetaData() {
        return metadata;
    }

    @Override
    public String toString()
    {
        return "[" + metadata.getTopicName() + "]" + metadata.getUserName() + "Send a " + metadata.getDataTypeClassName();
    }
}
