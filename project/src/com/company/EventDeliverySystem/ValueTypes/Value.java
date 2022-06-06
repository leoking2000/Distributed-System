package com.company.EventDeliverySystem.ValueTypes;

import java.io.Serializable;
import java.util.ArrayList;

/*
    a Value is something that we post in a topic(chat)
 */
public interface Value extends Serializable
{
    String toString();
    ArrayList<FileChunk> GenerateChunks();
    MetaData GetMetaData();

    // the broker needs this to reCreate the value
    static Value ReCreate(ArrayList<FileChunk> chunks, MetaData metaData)
    {
        switch (metaData.getDataTypeClassName())
        {
            case "Text":
                return new Text(chunks, metaData);
            case "MultiMediaFile":
                return new MultiMediaFile(chunks, metaData);
            default:
                return null;
        }
    }
}
