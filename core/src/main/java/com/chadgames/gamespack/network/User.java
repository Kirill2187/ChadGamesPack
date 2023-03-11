package com.chadgames.gamespack.network;

import com.chadgames.gamespack.utils.Player;

public class User {
    // TODO: IP Address
    Player player;

    public User() {
        player = null;
    }

    public int getId() { return player.id; }
}
