package com.chadgames.gamespack.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chadgames.gamespack.GameManager;

public class WaitWindow extends Window {

    Label playerCount;
    TextButton startButton;

    public WaitWindow(String title, ClickListener startListener) {
        super(title, GameManager.getInstance().skin, "default");
        createUI(startListener);
    }

    public void createUI(ClickListener startListener) {
        Skin skin = GameManager.getInstance().skin;

        Table root = new Table();
        root.setFillParent(true);
        root.bottom();
        addActor(root);

        playerCount = new Label("1 / 0", skin, "title");
        root.add(playerCount).expand().padRight(10).padLeft(10).padBottom(10).row();

        startButton = new TextButton("Start", skin);
        root.add(startButton).growX().padRight(10).padLeft(10).padBottom(10).row();
        startButton.addListener(startListener);
    }

    public void setPlayerCount(int count, int max, boolean allowedToStart) {
        playerCount.setText(count + " / " + max);
        startButton.setDisabled(!allowedToStart);
    }
}
