package com.chadgames.gamespack.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.chat.ChatMoveData;
import com.chadgames.gamespack.network.Request;
import com.chadgames.gamespack.network.RequestType;
import com.chadgames.gamespack.network.Response;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class GameProcess {

    private GameRenderer gameRenderer;
    private GameState gameState;
    private Listener listener;

    public GameProcess(GameType gameType, Stage stage, SpriteBatch batch) {
        this.gameState = GameGenerator.generateState(gameType);
        this.gameRenderer = GameGenerator.generateRenderer(gameType, this, stage, batch);
        listener = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Response) {
                    processResponse((Response) object);
                } else {
                    System.out.print("Unknown object received ");
                }
            }
        };
        GameManager.getInstance().client.addListener(listener);
    }

    public void processResponse(Response response) {
        switch (response.responseType) {
            case FetchMove: {
                makeMove((MoveData) response.data); // TODO make something with userId
                break;
            }
        }
    }

    public void render(float delta) {
        gameRenderer.render(delta);
    }

    public boolean makeMove(MoveData moveData) {
        if (!gameState.checkMove(moveData)) return false;
        ActionsSequence sequence = gameState.makeMove(moveData);
        gameRenderer.makeActions(sequence);
        return true;
    }

    public void sendMoveToServer(MoveData moveData) {
        Request request = new Request();
        request.requestType = RequestType.SendMove;
        request.data = moveData;
        GameManager.getInstance().client.sendTCP(request);
    }

    /* When renderer receives user input, move should be also sent to server */
    public void makeMoveAndSendToServer(MoveData moveData) {
        boolean checkMoveSuccess = makeMove(moveData);
        if (!checkMoveSuccess) return;
        sendMoveToServer(moveData);
    }

    public void removeListener() {
        GameManager.getInstance().client.removeListener(listener);
    }

}
