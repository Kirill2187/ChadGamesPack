package com.chadgames.gamespack.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.GameType;

/** First screen of the application. Displayed after the application is created. */
public class MenuScreen implements Screen {
    private Stage stage;
    private Viewport viewport;
    private Skin skin;
    public MenuScreen() {
        viewport = new ExtendViewport(225, 400); // TODO: remove magic numbers, should be some global constants
        stage = new Stage(viewport);
        skin = GameManager.getInstance().skin;

        Gdx.input.setInputProcessor(stage);
        createUI();
    }

    private void createUI() {
        Table root = new Table();
        root.debugAll();
        root.setFillParent(true);
        stage.addActor(root);
        root.top();

        Label testLabel = new Label("Test", skin, "title");
        root.add(testLabel).expandX().row();

        Table gamesTable = new Table();
        gamesTable.setColor(0, 0, 0, 0.5f);
        gamesTable.add(new Label("Games", skin)).expandX().row();
        root.add(gamesTable).expandY().row();

        Table bottomTable = new Table();

        TextButton testButton = new TextButton("Chat", skin);
        testButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.getInstance().launchGame(GameType.Chat);
            }
        });
        bottomTable.add(testButton);

        bottomTable.add(new Table()).expandX().fillX();

        TextButton exitButton = new TextButton("TTT", skin);
        bottomTable.add(exitButton);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.getInstance().launchGame(GameType.TicTacToe);
            }
        });

        root.add(bottomTable).fillX().padLeft(10).padRight(10).padBottom(5).row();
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
