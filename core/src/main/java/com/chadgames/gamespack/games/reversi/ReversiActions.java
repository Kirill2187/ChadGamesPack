package com.chadgames.gamespack.games.reversi;

import com.chadgames.gamespack.games.Actions;

import java.util.ArrayList;

public class ReversiActions implements Actions {
    public ArrayList<ReversiCoords> list;
    public Symbol symbol;
    public ReversiActions(ArrayList list, Symbol symbol) {
        this.list = list;
        this.symbol = symbol;
    }
}
