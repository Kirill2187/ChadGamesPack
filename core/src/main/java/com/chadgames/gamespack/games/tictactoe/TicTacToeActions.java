package com.chadgames.gamespack.games.tictactoe;

import com.chadgames.gamespack.games.Actions;

public class TicTacToeActions implements Actions {
    public int x;
    public int y;
    public Symbol symbol;
    public TicTacToeActions(int x, int y, Symbol symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }
}
