package com.company.utilities;

public class Address
{
    private String ip;
    private int port;

    public Address(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    public String getIp()
    {
        return ip;
    }

    public int getPort()
    {
        return port;
    }

    public String toString()
    {
        return "Address: " + ip + ":" + port;
    }
}
