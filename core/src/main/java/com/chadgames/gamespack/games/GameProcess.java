package com.chadgames.gamespack.games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameProcess {

    private GameRenderer gameRenderer;
    private GameState gameState;

    public GameProcess(GameType gameType, Stage stage, SpriteBatch batch) {
        this.gameState = GameGenerator.generateState(gameType);
        this.gameRenderer = GameGenerator.generateRenderer(gameType, this, stage, batch);
    }

    public void render(float delta) {
        gameRenderer.render(delta);
    }

    public void makeMove(int userId, MoveData moveData) {
        if (!gameState.checkMove(userId, moveData)) return;
        ActionsSequence sequence = gameState.makeMove(userId, moveData);
        gameRenderer.makeActions(sequence);
    }

    public void sendMoveToServer(int userId, MoveData moveData) {
        // TODO, client possibly will be stored in GameManager
    }

    /* When renderer receives user input, move should be also sent to server */
    public void makeMoveAndSendToServer(int userId, MoveData moveData) {
        makeMove(userId, moveData);
        sendMoveToServer(userId, moveData);
    }

}
