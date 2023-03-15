package com.chadgames.gamespack.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chadgames.gamespack.games.GameGenerator;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameType;

public class GameScreen implements Screen {

    private GameProcess gameProcess;
    private Stage stage;
    private SpriteBatch batch;
    private Viewport viewport;

    public GameScreen(GameType gameType) {
        stage = new Stage();
        batch = new SpriteBatch();
        gameProcess = new GameProcess(gameType, stage, batch);

        viewport = new ExtendViewport(225, 400); // TODO: remove magic numbers
        stage.setViewport(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.6f, 0.3f, 1);
        gameProcess.render(delta);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameProcess.removeListener();
        stage.dispose();
        batch.dispose();
    }

}
