package com.chadgames.gamespack.games.ticktacktoe;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chadgames.gamespack.games.GameFactory;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;

public class TickTackToeFactory extends GameFactory {

    @Override
    public GameState createState() {
        return new TickTackToeState();
    }

    @Override
    public GameRenderer createRenderer(GameProcess gameProcess, Stage stage, SpriteBatch batch) {
        return new TickTackToeRenderer(gameProcess, stage, batch);
    }
}
