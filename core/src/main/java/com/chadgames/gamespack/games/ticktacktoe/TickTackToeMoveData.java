package com.chadgames.gamespack.games.ticktacktoe;

import com.chadgames.gamespack.games.MoveData;

public class TickTackToeMoveData extends MoveData {
    public int x;
    public int y;
    public TickTackToeMoveData(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public TickTackToeMoveData() {}
}
