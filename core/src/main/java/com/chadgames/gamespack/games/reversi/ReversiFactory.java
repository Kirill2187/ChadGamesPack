package com.chadgames.gamespack.games.reversi;

import com.chadgames.gamespack.games.GameFactory;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;

public class ReversiFactory extends GameFactory {

    @Override
    public GameState createState() {
        return new ReversiState();
    }

    @Override
    public GameRenderer createRenderer(GameProcess gameProcess) {
        return new ReversiRenderer(gameProcess, gameProcess.getGameTable());
    }
}
