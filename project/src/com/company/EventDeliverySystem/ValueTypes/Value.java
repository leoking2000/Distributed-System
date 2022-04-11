package com.company.EventDeliverySystem.ValueTypes;

import java.io.Serializable;
import java.util.ArrayList;

public interface Value extends Serializable
{
    String toString();
    ArrayList<FileChunk> GenerateChunks();
    MetaData GetMetaData();

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
