package com.chadgames.gamespack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.chadgames.gamespack.GameManager;

// GameButton contains
// 1. Game logo (shrink when clicked)
// 2. Game name
// 3. Current online players

public class GameButton extends Table {

    private Image logoImg;
    public final static int IMG_SIZE = 100;
    private int onlinePlayers = 0;
    private Label onlinePlayersLabel;

    public GameButton(Sprite logo, String name, ClickListener listener) {
        logoImg = new Image(logo);
        add(logoImg).size(IMG_SIZE).colspan(2).row();
        debugAll();
        setWidth(IMG_SIZE);

        Label nameLabel = new Label(name, GameManager.getInstance().skin);
        add(nameLabel).padRight(10).fill();
        nameLabel.setAlignment(Align.left);

        onlinePlayersLabel = new Label("0", GameManager.getInstance().skin);
        onlinePlayersLabel.setAlignment(Align.right);
        add(onlinePlayersLabel).padRight(10).fill();

        addListener(listener);
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
        onlinePlayersLabel.setText(String.valueOf(onlinePlayers));
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }


}
