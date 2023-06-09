package com.chadgames.gamespack.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameType;
import static com.chadgames.gamespack.ui.UIScale.*;

public class GameScreen implements Screen {

    private GameProcess gameProcess;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Viewport viewport;

    public GameScreen(GameType gameType) {
        stage = new Stage();
        batch = new SpriteBatch();
        skin = GameManager.getInstance().skin;
        gameProcess = new GameProcess(gameType, stage, batch);

        viewport = new ExtendViewport(BASE_WIDTH, BASE_HEIGHT);
        stage.setViewport(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(skin.getColor("green"));
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
        gameProcess.dispose();
        stage.dispose();
        batch.dispose();
    }

}
