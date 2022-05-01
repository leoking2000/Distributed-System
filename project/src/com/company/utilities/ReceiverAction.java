package com.company.utilities;

import java.net.Socket;

public interface ReceiverAction
{
    void HandleConnection(Socket s);
}
