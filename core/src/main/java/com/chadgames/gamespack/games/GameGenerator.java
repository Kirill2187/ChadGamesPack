package com.chadgames.gamespack.games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chadgames.gamespack.games.chat.*;

public class GameGenerator {

    public static GameRenderer generateRenderer(GameType gameType, GameProcess gameProcess, Stage stage, SpriteBatch batch) {
        switch (gameType) {
            case Chat:
                return new ChatRenderer(gameProcess, stage, batch);
            default:
                throw new IllegalArgumentException("GameType not supported");
        }
    }

    public static GameState generateState(GameType gameType) {
        switch (gameType) {
            case Chat:
                return new ChatState();
            default:
                throw new IllegalArgumentException("GameType not supported");
        }
    }
}
