package com.chadgames.gamespack.games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class GameRenderer {

    protected Table rootTable;
    protected SpriteBatch batch;
    protected GameProcess gameProcess;
    public GameRenderer(GameProcess gameProcess, Table rootTable, SpriteBatch batch) {
        this.gameProcess = gameProcess;
        this.rootTable = rootTable;
        this.batch = batch;
    }

    public abstract void render(float delta);
    public abstract void makeActions(Actions actions);
    public abstract void loadFromState(GameState gameState);
}
