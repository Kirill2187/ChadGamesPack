package com.chadgames.gamespack.games.tictactoe;

import com.chadgames.gamespack.games.MoveData;

public class TicTacToeMoveData extends MoveData {
    public int x;
    public int y;
    public TicTacToeMoveData(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public TicTacToeMoveData() {}
}
