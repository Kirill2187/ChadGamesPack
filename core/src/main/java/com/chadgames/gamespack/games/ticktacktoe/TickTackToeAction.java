package com.chadgames.gamespack.games.ticktacktoe;

import com.chadgames.gamespack.games.ActionsSequence;

public class TickTackToeAction implements ActionsSequence {
    public int x;
    public int y;
    public TickTackToeAction(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
