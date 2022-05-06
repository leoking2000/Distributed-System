package com.company.EventDeliverySystem;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface SenderAction
{
    void Send(ObjectInputStream in, ObjectOutputStream out);
}