package com.chadgames.gamespack.games.reversi;

import static com.chadgames.gamespack.games.reversi.ReversiConstants.SIZE;
import static com.chadgames.gamespack.games.reversi.Symbol.*;

import com.chadgames.gamespack.games.Actions;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;
import com.chadgames.gamespack.utils.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ReversiState extends GameState {
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

    public ReversiState() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j] = null;
            }
        }
        field[SIZE / 2 - 1][SIZE / 2 - 1] = White;
        field[SIZE / 2][SIZE / 2] = White;
        field[SIZE / 2 - 1][SIZE / 2] = Black;
        field[SIZE / 2][SIZE / 2 - 1] = Black;
    }

    @Override
    public Actions playerJoined(Player player) {
        if (!symbolToPlayer.containsKey(White)) {
            symbolToPlayer.put(White, player.id);
            playerToSymbol.put(player.id, White);
        } else {
            symbolToPlayer.put(Black, player.id);
            playerToSymbol.put(player.id, Black);
        }
        return null;
    }
    @Override
    public Actions playerLeft(Player player) {
        Symbol s = playerToSymbol.get(player.id);
        playerToSymbol.remove(player.id);
        symbolToPlayer.remove(s);
        return null;
    }

    @Override
    public Actions makeMove(MoveData moveData) {
        ReversiMoveData data = (ReversiMoveData) moveData;
        field[data.x][data.y] = playerToSymbol.get(moveData.playerId);
        ArrayList<ReversiCoords> list = recolor(data.x, data.y);
        currentPlayerId = currentPlayerId == symbolToPlayer.get(White) ? symbolToPlayer.get(Black) : symbolToPlayer.get(White);
        return new ReversiActions(list, field[data.x][data.y]);
    }

    private ArrayList<ReversiCoords> recolor(int x, int y) {
        ArrayList<ReversiCoords> ret = new ArrayList<>();
        ret.add(new ReversiCoords(x, y));
        ret.addAll(recolor(x, y, 0, 1));
        ret.addAll(recolor(x, y, 0, -1));
        ret.addAll(recolor(x, y, -1, 0));
        ret.addAll(recolor(x, y, 1, 0));
        ret.addAll(recolor(x, y, 1, 1));
        ret.addAll(recolor(x, y, 1, -1));
        ret.addAll(recolor(x, y, -1, -1));
        ret.addAll(recolor(x, y, -1, 1));
        return ret;
    }

    private ArrayList<ReversiCoords> recolor(int x, int y, int dx, int dy) {
        ArrayList<ReversiCoords> ret = new ArrayList();
        Symbol another_color = (field[x][y] == White ? Black : White);
        int k = 1;
        for (; inField(x + dx * k, y + dy * k); ++k) {
            int nx = x + dx * k;
            int ny = y + dy * k;
            if (field[nx][ny] == null) {
                return new ArrayList<>();
            }
            if (field[nx][ny] == field[x][y]) {
                break;
            }
        }
        for (int t = 1; t < k; ++t) {
            int nx = x + dx * t;
            int ny = y + dy * t;
            field[nx][ny] = another_color;
            ret.add(new ReversiCoords(nx, ny));
        }
        return ret;
    }

    private boolean inField(int x, int y) {
        return (x >= 0 && y >= 0 && x < SIZE && y < SIZE);
    }


    @Override
    public boolean checkMove(MoveData moveData) {
        if (gameFinished || !gameStarted) return false;
        if (moveData.playerId != currentPlayerId) return false;
        ReversiMoveData data = (ReversiMoveData) moveData;
        if (!inField(data.x, data.y)) {
            return false;
        }
        return field[data.x][data.y] == null;
    }
    @Override
    public void startGame() {
        super.startGame();
        currentPlayerId = symbolToPlayer.get(White);
    }

    @Override
    public int getWinner() {
        if (!isGameStarted()) return -1;
        if (players.size() == 1) {
            return players.values().iterator().next().id;
        }
        return checkWin();
    }

    private int checkWin() {
        int balance = 0;
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                if (field[i][j] == null) return -1;
                if (field[i][j] == White) {
                    ++balance;
                } else {
                    --balance;
                }
            }
        }
        if (balance > 0) return symbolToPlayer.get(White);
        return symbolToPlayer.get(Black);
    }

}
