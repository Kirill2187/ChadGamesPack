package com.chadgames.gamespack.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.chadgames.gamespack.GameManager;
import static com.chadgames.gamespack.ui.UIScale.*;

public class GameOverWindow extends Window {
    TextButton restartButton;
    TextButton menuButton;

    public GameOverWindow(String title, ClickListener restartListener, ClickListener menuListener) {
        super(title, GameManager.getInstance().skin, "default");
        createUI(restartListener, menuListener);
    }

    public void createUI(ClickListener restartListener, ClickListener menuListener) {
        Skin skin = GameManager.getInstance().skin;

        getTitleLabel().setAlignment(Align.center);

        Table root = new Table();
        root.setFillParent(true);
        root.bottom();
        addActor(root);

        restartButton = new TextButton("Restart", skin);
        root.add(restartButton).growX().pad(PADDING).row();
        restartButton.addListener(restartListener);

        menuButton = new TextButton("Leave", skin);
        root.add(menuButton).growX().pad(PADDING).row();
        menuButton.addListener(menuListener);
    }

}
