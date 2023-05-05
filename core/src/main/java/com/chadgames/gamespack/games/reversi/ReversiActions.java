package com.chadgames.gamespack.games.reversi;

import com.chadgames.gamespack.games.Actions;

public class ReversiActions implements Actions {
    public int x;
    public int y;
    public Symbol symbol;
    public ReversiActions(int x, int y, Symbol symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }
}
