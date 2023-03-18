package com.chadgames.gamespack.server;

import com.chadgames.gamespack.games.GameGenerator;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.MoveData;
import com.chadgames.gamespack.network.Response;
import com.chadgames.gamespack.utils.Player;

import java.util.ArrayList;

public class Room {
    private GameType gameType;
    private ArrayList<User> users = new ArrayList<>();
    private boolean isActive;
    private GameState gameState;
    private final int minMembers;
    private final int maxMembers;
    private int curPlayerId = 0;

    public ArrayList<User> getUsers() {
        return users;
    }

    public Room(GameType type, int min, int max) {
        gameType = type;
        minMembers = min;
        maxMembers = max;
        gameState = GameGenerator.generateState(gameType);
    }

    public boolean hasUser(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    public void leave(int userId) {
        for (int i = 0; i < users.size(); ++i) {
            if (users.get(i).getUserId() == userId) {
                User user = users.get(i);
                users.remove(i);

                gameState.removePlayer(user.getPlayerId());
                gameState.playerLeft(user.player);

                return;
            }
        }
    }

    public int size() {
        return users.size();
    }

    public void join(User user) {
        users.add(user);

        user.player = new Player();
        user.player.id = curPlayerId++;
        user.player.username = user.getUsername();

        gameState.addPlayer(user.player);
        gameState.playerJoined(user.player);
    }

    public boolean makeMove(MoveData data) {
        if (!gameState.checkMove(data)) return false;
        gameState.makeMove(data);
        return true;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean full() {
        return users.size() >= maxMembers;
    }

    public void sendToAll(Response response) {
        sendToAllExcept(response, -1);
    }

    public void sendToAllExcept(Response response, int userId) {
        for (User user : users) {
            if (user.getUserId() != userId) {
                user.getConnection().sendTCP(response);
            }
        }
    }

    public GameState getGameState() {
        return gameState;
    }
}
