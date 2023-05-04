package com.chadgames.gamespack.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.MyAssetManager;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.ui.GameButton;
import com.chadgames.gamespack.utils.Constants;

/** First screen of the application. Displayed after the application is created. */
public class MenuScreen implements Screen {
    private Stage stage;
    private Viewport viewport;
    private Skin skin;
    public MenuScreen() {
        viewport = new ExtendViewport(225, 400); // TODO: remove magic numbers, should be some global constants
        stage = new Stage(viewport);
        skin = GameManager.getInstance().skin;

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
        root.add(gamesTable).grow().row();
        createGameTable(gamesTable);

        Table usernameTable = new Table();
        TextField nickTextField = new TextField(GameManager.getInstance().username, skin);
        usernameTable.add(nickTextField).padRight(5).growX().minWidth(100);

        TextButton setUsername = new TextButton("Set", skin);
        setUsername.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.getInstance().changeUsername(nickTextField.getText());
            }
        });
        usernameTable.add(setUsername);
        root.add(usernameTable).expandX().padLeft(10).padRight(10).padBottom(10).row();
    }

    void createGameTable(Table gameTable) {
        MyAssetManager manager = GameManager.getInstance().assetManager;
        gameTable.align(Align.top);
        for (GameType gameType : GameType.values()) {
            GameButton button = new GameButton(manager.getIcon(gameType.name().toLowerCase()),
            gameType.name(), new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    GameManager.getInstance().launchGame(gameType);
                }
            });
            gameTable.add(button).pad(5).growX();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
