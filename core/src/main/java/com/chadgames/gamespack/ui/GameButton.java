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

public class GameButton extends Table {

    private Image logoImg;
    public final static float IMG_SIZE = 80;
    private final static float ANIMATION_TIME = 0.2f;
    private final static float ANIMATION_SCALE = 1.1f;
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

        onlinePlayersLabel = new Label("?", GameManager.getInstance().skin);
        onlinePlayersLabel.setAlignment(Align.right);
        add(onlinePlayersLabel).fill();

        addListener(listener);

        logoImg.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (logoImg.hasActions()) return;
                logoImg.addAction(Actions.scaleTo(ANIMATION_SCALE, ANIMATION_SCALE, ANIMATION_TIME));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                logoImg.addAction(Actions.scaleTo(1, 1, ANIMATION_TIME));
                super.exit(event, x, y, pointer, toActor);
            }
        });
        logoImg.setOrigin(IMG_SIZE / 2, IMG_SIZE / 2);
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
        onlinePlayersLabel.setText(String.valueOf(onlinePlayers));
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }


}
