package com.company.main;

import java.util.List;

public interface Node
{
    List<Broker> brokers = null;

    void connect();
    void init(int k);
    void updateNodes();
}
