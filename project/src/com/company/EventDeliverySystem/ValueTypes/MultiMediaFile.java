package com.company.EventDeliverySystem.ValueTypes;

import com.company.EventDeliverySystem.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/*
 this can be a photo or a video
 */
public class MultiMediaFile implements Value
{
    private byte[] data;
    private MetaData metadata;

    public MultiMediaFile(ArrayList<FileChunk> chunks, MetaData metaData)
    {
        metadata = metaData;
        data = FileChunk.GetData(chunks);
    }

    public MultiMediaFile(String filepath, String user, String topic)
    {
        String[] path = filepath.split("/");

        try
        {
            File file = new File(filepath);
            data = Files.readAllBytes(file.toPath());

            int numberOfChunks = GenerateChunks().size();
            metadata = new MetaData(user, topic, "MultiMediaFile", numberOfChunks, path[path.length - 1]);

            Logger.Log("INFO", metadata.toString());
        }
        catch (IOException e)
        {
            Logger.Log("ERROR","MultiMediaFile " + filepath + "Failed to load!!!");
            data = null;
            metadata = null;
            e.printStackTrace();
            assert true;
        }
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
