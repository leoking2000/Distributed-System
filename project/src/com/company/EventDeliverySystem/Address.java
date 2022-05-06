package com.company.EventDeliverySystem;

import java.io.Serializable;

/*
ip + post Address
 */
public class Address implements Serializable
{
    private String ip;
    private int port;

    public Address(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    public boolean equals(Object o)
    {
        if(o == null) { return false; }

        if(o == this) { return true; }

        if(!(o instanceof Address)) { return false; }

        Address a = (Address) o;
        return ip.equals(a.ip) && a.port == port;
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
