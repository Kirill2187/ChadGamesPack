package com.chadgames.gamespack.games.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chadgames.gamespack.games.ActionsSequence;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;

public class ChatRenderer extends GameRenderer {
    public ChatRenderer(GameProcess gameProcess, Stage stage, SpriteBatch batch) {
        super(gameProcess, stage, batch);
        Gdx.app.log("debug", "Chat renderer created");
        // Create UI here
    }

    @Override
    public void render(float delta) {
        // Do nothing
    }

    @Override
    public void makeActions(ActionsSequence actionsSequence) {
        Gdx.app.log("debug", "New message: " + ((ChatActionsSequence)actionsSequence).messageToAdd);
        // Add message to UI
    }
}
