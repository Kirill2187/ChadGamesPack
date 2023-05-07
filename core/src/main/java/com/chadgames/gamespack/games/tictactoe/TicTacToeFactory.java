package com.chadgames.gamespack.games.tictactoe;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.chadgames.gamespack.games.GameFactory;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;

public class TicTacToeFactory extends GameFactory {

    @Override
    public GameState createState() {
        return new TicTacToeState();
    }

    @Override
    public GameRenderer createRenderer(GameProcess gameProcess) {
        return new TicTacToeRenderer(gameProcess, gameProcess.getGameTable());
    }
}
