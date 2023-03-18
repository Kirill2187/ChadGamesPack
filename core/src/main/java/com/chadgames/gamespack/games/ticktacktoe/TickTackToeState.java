package com.chadgames.gamespack.games.ticktacktoe;

import static com.chadgames.gamespack.games.ticktacktoe.TickTackToeConstants.*;

import com.chadgames.gamespack.games.ActionsSequence;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;
import com.chadgames.gamespack.utils.Player;

import java.util.HashMap;

public class TickTackToeState extends GameState {

    private Symbol[][] field = new Symbol[SIZE][SIZE];
    public Symbol getSymbol(int x, int y) {
        return field[x][y];
    }

    private HashMap<Integer, Symbol> playerToSymbol = new HashMap<>();
    private HashMap<Symbol, Integer> symbolToPlayer = new HashMap<>();
    public Symbol getSymbolForPlayer(int id) {
        return playerToSymbol.get(id);
    }

    public int currentPlayerId = 0;

    public TickTackToeState() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j] = Symbol.EMPTY;
            }
        }
    }

    @Override
    public ActionsSequence playerJoined(Player player) {
        if (!symbolToPlayer.containsKey(Symbol.X)) {
            symbolToPlayer.put(Symbol.X, player.id);
            playerToSymbol.put(player.id, Symbol.X);
        } else {
            symbolToPlayer.put(Symbol.O, player.id);
            playerToSymbol.put(player.id, Symbol.O);
        }
        return null;
    }

    @Override
    public ActionsSequence playerLeft(Player player) {
        Symbol s = playerToSymbol.get(player);
        playerToSymbol.remove(player);
        symbolToPlayer.remove(s);
        return null;
    }

    @Override
    public ActionsSequence makeMove(MoveData moveData) {
        TickTackToeMoveData data = (TickTackToeMoveData) moveData;
        field[data.x][data.y] = playerToSymbol.get(moveData.playerId);
        currentPlayerId = currentPlayerId == symbolToPlayer.get(Symbol.X) ? symbolToPlayer.get(Symbol.O) : symbolToPlayer.get(Symbol.X);
        int winner = checkWin();
        System.out.println("Winner: " + winner);
        if (winner != -1) {
            finishGame();
        }
        return new TickTackToeAction(data.x, data.y, field[data.x][data.y]);
    }

    @Override
    public boolean checkMove(MoveData moveData) {
        if (gameFinished || !gameStarted) return false;
        if (moveData.playerId != currentPlayerId) return false;
        TickTackToeMoveData data = (TickTackToeMoveData) moveData;
        if (data.x < 0 || data.x >= SIZE ||
            data.y < 0 || data.y >= SIZE) {
            return false;
        }
        if (field[data.x][data.y] != Symbol.EMPTY) {
            return false;
        }
        return moveData.playerId == currentPlayerId;
    }

    private int checkOneLine(int i, int j, int dx, int dy) {
        int cx = 0, co = 0;
        for (int k = 0; k < TO_WIN && i + k * dx < SIZE && j + k * dy < SIZE; ++k) {
            if (field[i + dx * k][j + dy * k] == Symbol.X) {
                ++cx;
            } else if (field[i + dx * k][j + dy * k] == Symbol.O) {
                ++co;
            }
        }
        if (cx == SIZE) return symbolToPlayer.get(Symbol.X);
        if (co == SIZE) return symbolToPlayer.get(Symbol.O);
        return -1;
    }
    private int checkWin() {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                int val = checkOneLine(i, j, 0, 1);
                if (val != -1) return val;
                val = checkOneLine(i, j, 1, 0);
                if (val != -1) return val;
                val = checkOneLine(i, j, 1, 1);
                if (val != -1) return val;
            }
        }
        return -1;
    }

    @Override
    public void startGame() {
        super.startGame();
        currentPlayerId = symbolToPlayer.get(Symbol.X);
    }
}
