package com.chadgames.gamespack.games.reversi;

import com.chadgames.gamespack.games.MoveData;

public class ReversiMoveData extends MoveData {

    public int x;
    public int y;
    public ReversiMoveData(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public ReversiMoveData() {}
}
