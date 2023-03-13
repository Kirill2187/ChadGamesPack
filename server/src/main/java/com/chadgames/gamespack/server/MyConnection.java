package com.chadgames.gamespack.server;

import com.esotericsoftware.kryonet.Connection;

public class MyConnection extends Connection {
    public int userId;
    public boolean registered = false;
    public String username;
}
