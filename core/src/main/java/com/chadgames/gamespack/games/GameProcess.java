package com.chadgames.gamespack.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.network.Request;
import com.chadgames.gamespack.network.RequestType;
import com.chadgames.gamespack.network.Response;
import com.chadgames.gamespack.utils.Constants;
import com.chadgames.gamespack.utils.Player;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class GameProcess {

    private GameRenderer gameRenderer;
    private GameState gameState;
    private Listener listener;
    private int myPlayerId;

    public GameProcess(GameType gameType, Stage stage, SpriteBatch batch) {
        GameFactory gameFactory = Constants.GAME_FACTORIES.get(gameType);

        this.gameState = gameFactory.createState();
        this.gameRenderer = gameFactory.createRenderer(this, stage, batch);
        listener = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Response) {
                    processResponse((Response) object);
                }
            }
        };
        Client client = GameManager.getInstance().client;
        client.addListener(listener);
        client.sendTCP(new Request(RequestType.JoinRoom, gameType));
    }

    public void processResponse(Response response) {
        switch (response.responseType) {
            case FetchMove: {
                makeMove((MoveData) response.data);
                break;
            }
            case UserJoined: {
                playerJoined((Player) response.data);
                Gdx.app.log("debug", "Player joined");
                break;
            }
            case UserLeft: {
                playerLeft((Player) response.data);
                break;
            }
            case FetchGameState: {
                Gdx.app.log("debug", "Received game state");
                fetchGameState((GameState) response.data);
                break;
            }
            case PlayerIdAssigned: {
                myPlayerId = (int) response.data;
                Gdx.app.log("debug", "My player id is " + myPlayerId);
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

    public void playerJoined(Player player) {
        gameState.addPlayer(player);
        ActionsSequence sequence = gameState.playerJoined(player);
        gameRenderer.makeActions(sequence);
    }
    public void playerLeft(Player player) {
        gameState.removePlayer(player.id);
        ActionsSequence sequence = gameState.playerLeft(player);
        gameRenderer.makeActions(sequence);
    }
    public void fetchGameState(GameState gameState) {
        this.gameState = gameState; // TODO: not safe
        gameRenderer.loadFromState(gameState);
    }
    public void dispose() {
        GameManager.getInstance().client.removeListener(listener);
    }

    public int getMyPlayerId() {
        return myPlayerId;
    }

}
