package com.chadgames.gamespack.games;

import com.chadgames.gamespack.utils.Player;

import java.util.HashMap;

public abstract class GameState {

    protected HashMap<Integer, Player> players = new HashMap<>();
    protected boolean gameStarted = false;
    protected boolean gameFinished = false;

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

    public abstract Actions playerJoined(Player player);
    public abstract Actions playerLeft(Player player);
    public abstract Actions makeMove(MoveData moveData);
    public boolean isGameStarted() {
        return gameStarted;
    }
    public void startGame() {
        gameStarted = true;
    }
    public void finishGame() {
        gameFinished = true;
    }
    public abstract boolean checkMove(MoveData moveData);

    public boolean isGameFinished() {
        return gameFinished;
    }

    public int getWinner() {
        return -1;
    }

    public void updateWinner() {
        if (gameFinished || !gameStarted) return;
        if (getWinner() != -1) {
            finishGame();
        }
    }

    public void reset() {
        gameStarted = false;
        gameFinished = false;
        players = new HashMap<>();
    }
}
