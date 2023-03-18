package com.chadgames.gamespack.games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GameFactory {

    public abstract GameState createState();
    public abstract GameRenderer createRenderer(GameProcess gameProcess, Stage stage, SpriteBatch batch);

}
