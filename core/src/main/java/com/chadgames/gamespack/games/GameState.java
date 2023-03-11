package com.chadgames.gamespack.games;

public interface GameState {
    void makeMove(int userId, MoveData moveData);
    boolean checkMove();
}
