package com.chadgames.gamespack.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.network.PlayerAndRoomId;
import com.chadgames.gamespack.network.RequestType;
import com.chadgames.gamespack.network.Response;
import com.chadgames.gamespack.ui.GameOverWindow;
import com.chadgames.gamespack.ui.PauseWindow;
import com.chadgames.gamespack.ui.WaitWindow;
import com.chadgames.gamespack.utils.Constants;
import com.chadgames.gamespack.utils.Player;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class GameProcess {

    private GameRenderer gameRenderer;
    private Table windowTable;
    private WaitWindow waitWindow;
    private PauseWindow pauseWindow;
    private GameOverWindow gameOverWindow;
    private GameState gameState;
    private GameType gameType;
    private Listener clientListener;
    private int myPlayerId;
    private int myRoomId;

    private Table gameTable;
    public Table getGameTable() {
        return gameTable;
    }

    private SpriteBatch batch;
    public SpriteBatch getBatch() {
        return batch;
    }

    public GameProcess(GameType gameType, Stage stage, SpriteBatch batch) {
        this.gameType = gameType;
        this.batch = batch;
        GameFactory gameFactory = Constants.GAME_FACTORIES.get(gameType);

        createUI(stage);
        this.gameState = gameFactory.createState();
        this.gameRenderer = gameFactory.createRenderer(this);
        updatePlayerCounter();

        GameManager.sendRequest(RequestType.JoinRoom, gameType);
    }

    public void activateWindow(Window window) {
        window.setVisible(true);
        windowTable.clearChildren();
        windowTable.setVisible(true);
        windowTable.add(window).minSize(200);
    }

    public void deactivateWindow(Window window) {
        window.setVisible(false);
        windowTable.clearChildren();
        windowTable.setVisible(false);
    }

    private void createUI(Stage stage) {
        Table root = new Table();
        root.setFillParent(true);
        root.debugAll();
        stage.addActor(root);

        Table gameBar = new Table();
        root.add(gameBar).expandX().height(50).row();

        TextButton pauseButton = new TextButton("||", GameManager.getInstance().skin); // TODO: replace with ImageButton
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!canPause()) return;
                activateWindow(pauseWindow);
            }
        });
        gameBar.add(pauseButton).width(100).expandY();

        gameTable = new Table();
        root.add(gameTable).expand().fill().row();

        windowTable = new Table();
        windowTable.setFillParent(true);
        stage.addActor(windowTable);

        ClickListener leaveListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.sendRequest(RequestType.LeaveRoom);
                GameManager.getInstance().setMenuScreen();
            }
        };
        waitWindow = new WaitWindow("Waiting for players...", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tryStartGame();
            }
        }, leaveListener);
        waitWindow.setMovable(false);
        activateWindow(waitWindow);

        pauseWindow = new PauseWindow("Game paused", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deactivateWindow(pauseWindow);
            }
        }, leaveListener);
        pauseWindow.setMovable(false);
        pauseWindow.setVisible(false);

        gameOverWindow = new GameOverWindow("Game over", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deactivateWindow(gameOverWindow);
                gameState.reset();
                GameManager.sendRequest(RequestType.JoinRoom, myRoomId);
                activateWindow(waitWindow);
            }
        }, leaveListener);
        gameOverWindow.setMovable(true);
        gameOverWindow.setVisible(false);

        clientListener = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Response) {
                    processResponse((Response) object);
                }
            }
        };
        Client client = GameManager.getInstance().client;
        client.addListener(clientListener);
    }

    public void processResponse(Response response) {
        switch (response.responseType) {
            case FetchMove: {
                makeMove((MoveData) response.data);
                break;
            }
            case UserJoined: {
                playerJoined((Player) response.data);
                if (!gameState.isGameStarted()) updatePlayerCounter();
                Gdx.app.log("debug", "Player joined");
                break;
            }
            case UserLeft: {
                playerLeft((Player) response.data);
                if (!gameState.isGameStarted()) updatePlayerCounter();
                break;
            }
            case GameStarted: {
                if (response.success) {
                    fetchGameState((GameState) response.data);
                    deactivateWindow(waitWindow);
                    Gdx.app.log("debug", "Game started");
                }
                break;
            }
            case FetchGameState: {
                Gdx.app.log("debug", "Received game state");
                fetchGameState((GameState) response.data);
                if (!gameState.isGameStarted()) updatePlayerCounter();
                break;
            }
            case PlayerIdAssigned: {
                PlayerAndRoomId ids = (PlayerAndRoomId) response.data;
                myPlayerId = ids.playerId;
                myRoomId = ids.roomId;
                Gdx.app.log("debug", "My player id is " + myPlayerId);
                break;
            }
            case GameFinished: {
                int winnerId = (int) response.data;
                Gdx.app.log("debug", "Game finished, player " + gameState.getPlayerById(winnerId).username + " won");
                if (!gameState.isGameFinished()) {
                    gameState.finishGame();
                    // TODO: notify renderer
                }
                activateWindow(gameOverWindow); // TODO: show window not immediately but after 2s
                break;
            }
        }
    }

    public void render(float delta) {
        if (gameState.isGameStarted() && waitWindow.isVisible()) deactivateWindow(waitWindow);
        gameRenderer.render(delta);
    }

    public boolean makeMove(MoveData moveData) {
        if (!gameState.isGameStarted()) return false;
        if (!gameState.checkMove(moveData)) return false;
        Actions sequence = gameState.makeMove(moveData);
        gameRenderer.makeActions(sequence);
        return true;
    }

    public void sendMoveToServer(MoveData moveData) {
        GameManager.sendRequest(RequestType.SendMove, moveData);
    }

    /* When renderer receives user input, move should be also sent to server */
    public void makeMoveAndSendToServer(MoveData moveData) {
        boolean checkMoveSuccess = makeMove(moveData);
        if (!checkMoveSuccess) return;
        sendMoveToServer(moveData);
    }

    public void playerJoined(Player player) {
        gameState.addPlayer(player);
        Actions sequence = gameState.playerJoined(player);
        gameRenderer.makeActions(sequence);
    }

    public void playerLeft(Player player) {
        gameState.removePlayer(player.id);
        Actions sequence = gameState.playerLeft(player);
        gameRenderer.makeActions(sequence);
    }

    public void fetchGameState(GameState gameState) {
        this.gameState = gameState; // TODO: not safe
        gameRenderer.loadFromState(gameState);
    }

    public void tryStartGame() {
        GameManager.sendRequest(RequestType.StartGame);
    }

    public void updatePlayerCounter() {
        int autostartPlayers = Constants.gameProperties.get(gameType).getAutostartPlayers();
        int currentPlayers = gameState.players.size();
        boolean allowedToStart = currentPlayers >= Constants.gameProperties.get(gameType).getMinPlayers();
        waitWindow.setPlayerCount(currentPlayers, autostartPlayers, allowedToStart);
    }

    public void dispose() {
        GameManager.getInstance().client.removeListener(clientListener);
    }

    public int getMyPlayerId() {
        return myPlayerId;
    }

    private boolean canPause() {
        return gameState.isGameStarted() && !gameState.isGameFinished();
    }

}
