package com.chadgames.gamespack.server;

import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.network.User;

import java.util.ArrayList;

public class Room {
    private GameType gameType;
    private ArrayList<User> users;
    private boolean isActive;
    private GameState gameState;

    public ArrayList<User> getUsers() {
        return users;
    }
}
