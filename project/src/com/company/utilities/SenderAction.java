package com.company.utilities;

import com.company.EventDeliverySystem.ValueTypes.Value;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface SenderAction
{
    void Send(Value v, ObjectInputStream in, ObjectOutputStream out);
}
