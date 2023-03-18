package com.chadgames.gamespack.games.chat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chadgames.gamespack.games.GameFactory;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;

public class ChatFactory extends GameFactory {
    @Override
    public GameState createState() {
        return new ChatState();
    }

    @Override
    public GameRenderer createRenderer(GameProcess gameProcess, Stage stage, SpriteBatch batch) {
        return new ChatRenderer(gameProcess, stage, batch);
    }
}
