package com.chadgames.gamespack.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chadgames.gamespack.GameManager;
import static com.chadgames.gamespack.ui.UIScale.*;

public class PauseWindow extends Window {
    TextButton resumeButton;
    TextButton menuButton;

    public PauseWindow(String title, ClickListener resumeListener, ClickListener menuListener) {
        super(title, GameManager.getInstance().skin, "default");
        createUI(resumeListener, menuListener);
    }

    public void createUI(ClickListener resumeListener, ClickListener menuListener) {
        Skin skin = GameManager.getInstance().skin;

        Table root = new Table();
        root.setFillParent(true);
        root.bottom();
        addActor(root);

        resumeButton = new TextButton("Resume", skin);
        root.add(resumeButton).growX().pad(PADDING).row();
        resumeButton.addListener(resumeListener);

        menuButton = new TextButton("Leave", skin);
        root.add(menuButton).growX().pad(PADDING).row();
        menuButton.addListener(menuListener);
    }

}
