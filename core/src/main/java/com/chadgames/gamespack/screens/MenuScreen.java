package com.chadgames.gamespack.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.MyAssetManager;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.ui.GameButton;
import com.chadgames.gamespack.ui.UIScale;
import com.chadgames.gamespack.utils.Constants;
import static com.chadgames.gamespack.ui.UIScale.*;

/** First screen of the application. Displayed after the application is created. */
public class MenuScreen implements Screen {
    private Stage stage;
    private Viewport viewport;
    private Skin skin;
    public MenuScreen() {
        viewport = new ExtendViewport(BASE_WIDTH, BASE_HEIGHT);
        stage = new Stage(viewport);
        skin = GameManager.getInstance().skin;

        createUI();
    }

    private void createUI() {
        Table root = new Table();
        if (GameManager.DEBUG) root.debugAll();
        root.setFillParent(true);
        stage.addActor(root);
        root.top();

        Label testLabel = new Label("Test", skin, "title");
        root.add(testLabel).expandX().padTop(PADDING).row();

        Table gamesTable = new Table();
        root.add(gamesTable).grow().row();
        createGameTable(gamesTable);

        Table usernameTable = new Table();
        TextField nickTextField = new TextField(GameManager.getInstance().username, skin);
        usernameTable.add(nickTextField).padRight(PADDING).growX()
            .minWidth(percentWidth(.5f))
            .minHeight(percentHeight(.08f));

        TextButton setUsername = new TextButton("Set", skin);
        setUsername.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.getInstance().changeUsername(nickTextField.getText());
            }
        });
        usernameTable.add(setUsername);
        root.add(usernameTable).expandX().pad(PADDING).row();

        createWaitingWindow();
    }

    private void createWaitingWindow() {
        Table windowTable = new Table();
        windowTable.setFillParent(true);
        stage.addActor(windowTable);
        windowTable.center();

        Window window = new Window("", skin);
        window.setBackground(GameManager.getInstance().skin.newDrawable("white", 0.5f, 0.5f, 0.5f, 1));
        windowTable.add(window).size(percentWidth(.5f), percentWidth(.3f));

        window.add(new Label("Connecting...", skin)).row();
        windowTable.setBackground(GameManager.getInstance().skin.newDrawable("white", 0, 0, 0, 0.5f));

        GameManager.getInstance().onConnected.subscribe(() -> {
            windowTable.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.removeActor()));
            GameManager.getInstance().onConnected.unsubscribe("waitWindow");
        }, "waitWindow");
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
            gameTable.add(button).pad(PADDING).growX();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(skin.getColor("green"));

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
