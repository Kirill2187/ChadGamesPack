package com.chadgames.gamespack.games.ticktacktoe;

import com.chadgames.gamespack.games.ActionsSequence;

public class TickTackToeAction implements ActionsSequence {
    public int x;
    public int y;
    public Symbol symbol;
    public TickTackToeAction(int x, int y, Symbol symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }
}
