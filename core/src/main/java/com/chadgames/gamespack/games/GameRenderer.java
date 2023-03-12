package com.chadgames.gamespack.games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GameRenderer {

    protected Stage stage;
    protected SpriteBatch batch;
    protected GameProcess gameProcess;
    public GameRenderer(GameProcess gameProcess, Stage stage, SpriteBatch batch) {
        this.gameProcess = gameProcess;
        this.stage = stage;
        this.batch = batch;
    }

    public abstract void render(float delta);
    public abstract void makeActions(ActionsSequence actionsSequence);
}
