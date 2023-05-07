package com.chadgames.gamespack.games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class GameFactory {

    public abstract GameState createState();
    public abstract GameRenderer createRenderer(GameProcess gameProcess);

}
