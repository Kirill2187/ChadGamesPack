package com.chadgames.gamespack.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.chadgames.gamespack.GameManager;

public class WaitWindow extends Window {
    public WaitWindow(String title) {
        super(title, GameManager.getInstance().skin, "default");
        createUI();
    }

    public void createUI() {
        Skin skin = GameManager.getInstance().skin;

        Table root = new Table();
        root.setFillParent(true);
        root.top();
        addActor(root);

        TextButton startButton = new TextButton("Start", skin);
        root.add(startButton).expandX().row();
    }
}
