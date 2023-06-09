package com.chadgames.gamespack.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
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
import static com.chadgames.gamespack.ui.UIScale.*;

public class GameProcess {

    private GameRenderer gameRenderer;
    private Table windowTable;
    private WaitWindow waitWindow;
    private PauseWindow pauseWindow;
    private GameOverWindow gameOverWindow;
    private Drawable windowBackground;
    private GameState gameState;
    private GameType gameType;
    private Listener clientListener;
    private int myPlayerId;
    private int myRoomId;

    private Table gameTable;
    public Table getGameTable() {
        return gameTable;
    }

    private Stage stage;
    public Stage getStage() {
        return stage;
    }

    private SpriteBatch batch;
    public SpriteBatch getBatch() {
        return batch;
    }

    public GameProcess(GameType gameType, Stage stage, SpriteBatch batch) {
        this.gameType = gameType;
        this.batch = batch;
        this.stage = stage;
        windowBackground = GameManager.getInstance().skin.newDrawable("white", new Color(0, 0, 0, 0.5f));
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
        windowTable.add(window).size(WINDOW_WIDTH, WINDOW_HEIGHT);
        windowTable.setVisible(true);

        windowTable.setBackground(windowBackground);
        windowTable.getColor().a = 0;
        windowTable.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn(0.4f));
    }

    public void deactivateWindow(Window window) {
        window.setVisible(false);
        windowTable.clearChildren();
        windowTable.setVisible(false);
    }

    private void createUI(Stage stage) {
        Table root = new Table();
        root.setFillParent(true);
        if (GameManager.DEBUG) root.debugAll();
        stage.addActor(root);

        Table gameBar = new Table();
        root.add(gameBar).expandX().top().right().row();

        TextButton pauseButton = new TextButton("||", GameManager.getInstance().skin); // TODO: replace with ImageButton
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!canPause()) return;
                activateWindow(pauseWindow);
            }
        });
        gameBar.add(pauseButton).padTop(PADDING).padRight(PADDING);

        Table rootTop = new Table();
        if (GameManager.DEBUG) rootTop.debugAll();
        rootTop.setFillParent(true);
        rootTop.top();
        stage.addActor(rootTop);

        Table cpy = new Table();
        rootTop.add(cpy).expandX().top().left().padLeft(PADDING);
        TextButton playerListButton = new TextButton("Players", GameManager.getInstance().skin); // TODO: replace with ImageButton

        Table playerListTable = new Table();
        Color[] colors = {Color.CORAL, Color.ORANGE, Color.BROWN, Color.CHARTREUSE};
        playerListButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playerListButton.getLabel().setText("X");

                Player[] players = gameState.players.values().toArray(new Player[0]);

                Pixmap labelColor = new Pixmap(1, 1, Pixmap.Format.RGB888);
                for (int i = 0; i < players.length; ++i) {
                    Label new_label = new Label(players[i].username, GameManager.getInstance().skin);
                    Label.LabelStyle style = new Label.LabelStyle(new_label.getStyle());
                    style.fontColor = Color.WHITE;
                    new_label.setStyle(style);
                    labelColor.setColor(colors[i % 4]);
                    labelColor.fill();
                    new_label.getStyle().background = new Image(new Texture(labelColor)).getDrawable();

                    playerListTable.add(new_label).left().growX().row();
                }
                labelColor.dispose();

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playerListButton.getLabel().setText("Players");
                playerListTable.clearChildren(false);
            }
        });
        cpy.add(playerListButton).width(percentWidth(.25f)).padTop(PADDING).center();
        cpy.row();
        cpy.add(playerListTable).center().padTop(PADDING);

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
