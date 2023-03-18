package com.chadgames.gamespack.games.ticktacktoe;

import com.chadgames.gamespack.games.ActionsSequence;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;
import com.chadgames.gamespack.utils.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class TickTackToeState extends GameState {

    private Symbol[][] field = new Symbol[TickTackToeConstants.SIZE][TickTackToeConstants.SIZE];
    public Symbol getSymbol(int x, int y) {
        return field[x][y];
    }

    private HashMap<Integer, Symbol> playerSymbols = new HashMap<>();
    public Symbol getSymbolForPlayer(int id) {
        return playerSymbols.get(id);
    }

    public int currentPlayerId = 0;

    public TickTackToeState() {
        for (int i = 0; i < TickTackToeConstants.SIZE; i++) {
            for (int j = 0; j < TickTackToeConstants.SIZE; j++) {
                field[i][j] = Symbol.EMPTY;
            }
        }
    }

    @Override
    public ActionsSequence playerJoined(Player player) {
        return null;
    }

    @Override
    public ActionsSequence playerLeft(Player player) {
        return null;
    }

    @Override
    public ActionsSequence makeMove(MoveData moveData) {

        TickTackToeMoveData data = (TickTackToeMoveData) moveData;
        field[data.x][data.y] = playerSymbols.get(moveData.playerId);

        return null;
    }

    @Override
    public boolean checkMove(MoveData moveData) {
        TickTackToeMoveData data = (TickTackToeMoveData) moveData;
        if (data.x < 0 || data.x >= TickTackToeConstants.SIZE ||
            data.y < 0 || data.y >= TickTackToeConstants.SIZE) {
            return false;
        }
        if (field[data.x][data.y] != Symbol.EMPTY) {
            return false;
        }
        return moveData.playerId == currentPlayerId;
    }
}
