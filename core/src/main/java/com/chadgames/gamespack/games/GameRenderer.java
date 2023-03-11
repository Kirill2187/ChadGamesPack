package com.chadgames.gamespack.games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GameRenderer {

    private Stage stage;
    private SpriteBatch batch;
    public GameRenderer(Stage stage, SpriteBatch batch) {
        this.stage = stage;
        this.batch = batch;
    }

    public abstract void render(float delta, GameState gameState);
}
