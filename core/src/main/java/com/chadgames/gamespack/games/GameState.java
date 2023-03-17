package com.chadgames.gamespack.games;

public interface GameState {
    ActionsSequence makeMove(MoveData moveData);
    boolean checkMove(MoveData moveData);
}
