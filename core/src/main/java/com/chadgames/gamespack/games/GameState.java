package com.chadgames.gamespack.games;

import com.chadgames.gamespack.utils.Player;

import java.util.HashMap;

public abstract class GameState {

    protected HashMap<Integer, Player> players = new HashMap<>();
    protected boolean gameStarted = false;

    public Player getPlayerById(int id) {
        assert players.containsKey(id);
        return players.get(id);
    }
    public void addPlayer(Player player) {
        players.put(player.id, player);
    }
    public void removePlayer(int id) {
        players.remove(id);
    }

    public abstract ActionsSequence playerJoined(Player player);
    public abstract ActionsSequence playerLeft(Player player);
    public abstract ActionsSequence makeMove(MoveData moveData);
    public void startGame() {
        gameStarted = true;
    }
    public abstract boolean checkMove(MoveData moveData);
}
