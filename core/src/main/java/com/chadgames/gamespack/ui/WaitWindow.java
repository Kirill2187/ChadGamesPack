package com.chadgames.gamespack.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.chadgames.gamespack.GameManager;
import static com.chadgames.gamespack.ui.UIScale.*;

public class WaitWindow extends Window {

    Label playerCount;
    TextButton startButton;
    TextButton leaveButton;

    public WaitWindow(String title, ClickListener startListener, ClickListener leaveListener) {
        super(title, GameManager.getInstance().skin, "default");
        createUI(startListener, leaveListener);
    }

    public void createUI(ClickListener startListener, ClickListener leaveListener) {
        Skin skin = GameManager.getInstance().skin;

        getTitleLabel().setAlignment(Align.center);

        Table root = new Table();
        root.setFillParent(true);
        root.bottom();
        addActor(root);

        playerCount = new Label("Wait...", skin, "big");
        root.add(playerCount).expand().pad(PADDING).padTop(PADDING * 5).row();

        startButton = new TextButton("Start", skin);
        root.add(startButton).growX().pad(PADDING).row();
        startButton.addListener(startListener);

        leaveButton = new TextButton("Leave", skin);
        root.add(leaveButton).growX().pad(PADDING).row();
        leaveButton.addListener(leaveListener);
    }

    public void setPlayerCount(int count, int max, boolean allowedToStart) {
        playerCount.setText(count + " / " + max);
        if (count == 0) playerCount.setText("Wait...");
        startButton.setDisabled(!allowedToStart);
    }
}
