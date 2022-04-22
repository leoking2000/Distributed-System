package com.company.EventDeliverySystem.ValueTypes;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*
this is a text msg
 */
public class Text implements Value
{
    private final String theText;
    private final MetaData metadata;

    public Text(ArrayList<FileChunk> chunks, MetaData metaData)
    {
        metadata = metaData;
        byte[] str = FileChunk.GetData(chunks);
        theText = new String(str, StandardCharsets.UTF_8);
    }

    public Text(String text, String user, String topic)
    {
        theText = text;
        int numberOfChunks = ((text.getBytes(StandardCharsets.UTF_8).length -1) / FileChunk.MAX_CHUNK_SIZE) + 1;
        metadata = new MetaData(user, topic, "Text", numberOfChunks);
    }

    @Override
    public ArrayList<FileChunk> GenerateChunks()
    {
        byte[] data = theText.getBytes(StandardCharsets.UTF_8);
        return FileChunk.Generate(data);
    }

    @Override
    public MetaData GetMetaData() {
        return metadata;
    }

    public String getTheText() {
        return theText;
    }

    @Override
    public String toString()
    {
        return "[" + metadata.getTopicName() + "]" + metadata.getUserName() + ": " + theText;
    }
}
