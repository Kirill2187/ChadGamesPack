package com.chadgames.gamespack.games;

public interface GameState {
    ActionsSequence makeMove(int userId, MoveData moveData);
    boolean checkMove(int userId, MoveData moveData);
}
