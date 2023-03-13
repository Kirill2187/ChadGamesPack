package com.chadgames.gamespack.server;

import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.MoveData;

import java.util.ArrayList;

public class Room {
    private GameType gameType;
    private ArrayList<User> users;
    private boolean isActive;
    private GameState gameState;

    public ArrayList<User> getUsers() {
        return users;
    }

    public boolean hasUser(int userId) {
        for (User user : users) {
            if (user.getPlayerId() == userId) {
                return true;
            }
        }
        return false;
    }

    public void leave(int userId) {
        for (int i = 0; i < users.size(); ++i) {
            if (users.get(i).getPlayerId() == i) {
                users.remove(i);
                return;
            }
        }
    }

    public int size() {
        return users.size();
    }

    public void join(User user) {
        users.add(user);
    }

    public void makeMove(int userId, Object data) {
        gameState.makeMove(userId, (MoveData)data);
    }
}
