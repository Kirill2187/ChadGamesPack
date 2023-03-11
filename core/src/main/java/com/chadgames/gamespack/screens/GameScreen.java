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
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameType;

public class GameScreen implements Screen {

    private GameRenderer gameRenderer;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;

    public GameScreen(GameType gameType) {
        stage = new Stage();
        batch = new SpriteBatch();
        gameRenderer = GameGenerator.generateRenderer(gameType, stage, batch);

        // TODO: stretch viewport shouldn't be used
        viewport = new StretchViewport(16, 9); // TODO: remove magic numbers
        stage.setViewport(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        stage.dispose();
        batch.dispose();
    }

}
