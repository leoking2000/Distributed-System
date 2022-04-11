package com.company.EventDeliverySystem.ValueTypes;

import java.io.Serializable;
import java.util.Date;

public class MetaData implements Serializable
{
    private final String UserName;
    private final String TopicName;
    private final String DataTypeClassName;
    private final Date dateCreated;
    private final int numberOfChunks;

    public MetaData(String userName, String topicName, String dataTypeClassName, int numberOfChunks)
    {
        UserName = userName;
        TopicName = topicName;
        DataTypeClassName = dataTypeClassName;
        dateCreated = new Date();
        this.numberOfChunks = numberOfChunks;
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

}
