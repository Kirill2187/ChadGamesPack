package com.chadgames.gamespack.server;

import com.chadgames.gamespack.utils.Player;

public class User {
    Player player;
    MyConnection connection;

    public User(MyConnection connection) {
        this.connection = connection;
        player = null;
    }

    public int getPlayerId() { return player.id; }
    public int getUserId() { return connection.userId; }
    public MyConnection getConnection() { return connection; }
}
