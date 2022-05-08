package com.company.EventDeliverySystem.ValueTypes;

import java.io.Serializable;
import java.util.Date;

/*
the metadata of a Value
 */
public class MetaData implements Serializable
{
    private final String UserName; // who send this?
    private final String TopicName; // the chat name
    private final String DataTypeClassName; // what type the Value is?
    private final Date dateCreated; // when was this Created
    private final int numberOfChunks; // how many chunks this value has
    private final String filename;

    public MetaData(String userName, String topicName, String dataTypeClassName, int numberOfChunks)
    {
        UserName = userName;
        TopicName = topicName;
        DataTypeClassName = dataTypeClassName;
        dateCreated = new Date();
        this.numberOfChunks = numberOfChunks;
        filename = "";
    }

    public MetaData(String userName, String topicName, String dataTypeClassName, int numberOfChunks, String filename)
    {
        UserName = userName;
        TopicName = topicName;
        DataTypeClassName = dataTypeClassName;
        dateCreated = new Date();
        this.numberOfChunks = numberOfChunks;
        this.filename = filename;
    }


    public String getUserName() {
        return UserName;
    }

    public String getTopicName() {
        return TopicName;
    }

    public String getDataTypeClassName() {
        return DataTypeClassName;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public int getNumberOfChunks() {
        return numberOfChunks;
    }

    public String getFilename() {
        return filename;
    }

    public String toString()
    {
        return "Metadata: \n" +
                "UserName: " + UserName + "\n" +
                "Topic: " + TopicName + "\n" +
                "DataType " + DataTypeClassName + "\n" +
                "number of chunks " + numberOfChunks + "\n" +
                "filename: " + filename + "\n";
    }

}
