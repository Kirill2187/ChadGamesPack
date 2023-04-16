package com.chadgames.gamespack.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.network.PlayerAndRoomId;
import com.chadgames.gamespack.network.Request;
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
    private Listener listener;
    private int myPlayerId;
    private int myRoomId;

    public GameProcess(GameType gameType, Stage stage, SpriteBatch batch) {
        this.gameType = gameType;
        GameFactory gameFactory = Constants.GAME_FACTORIES.get(gameType);

        Table root = new Table();
        root.setFillParent(true);
        root.debugAll();
        stage.addActor(root);

        Table gameBar = new Table();
        root.add(gameBar).expandX().top().right().row();

        TextButton pauseButton = new TextButton("||", GameManager.getInstance().skin); // TODO: replace with ImageButton
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (waitWindow.isVisible()) return;
                activateWindow(pauseWindow);
            }
        });
        gameBar.add(pauseButton).width(50).padTop(5).padRight(10);

        Table rootTop = new Table();
        rootTop.debugAll();
        rootTop.setFillParent(true);
        rootTop.top();
        stage.addActor(rootTop);

        Table cpy = new Table();
        rootTop.add(cpy).expandX().top().left();
        TextButton playerListButton = new TextButton("Players", GameManager.getInstance().skin); // TODO: replace with ImageButton
        playerListButton.getLabel().setFontScale(0.5f, 0.5f);

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

                    playerListTable.add(new_label).bottom().row();
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
        cpy.add(playerListButton).width(50).padTop(5).padLeft(10).left();
        cpy.row();
        cpy.add(playerListTable).right().padTop(5).padLeft(10);

        Table gameTable = new Table();
        root.add(gameTable).expand().fill().row();

        this.gameState = gameFactory.createState();
        this.gameRenderer = gameFactory.createRenderer(this, gameTable, batch);

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
        updatePlayerCounter();


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
                GameManager.sendRequest(RequestType.JoinRoom, myRoomId);
                gameState.gameStarted = false;
                activateWindow(waitWindow);
            }
        }, leaveListener);
        gameOverWindow.setMovable(true);
        gameOverWindow.setVisible(false);

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
        GameManager.getInstance().client.removeListener(listener);
    }

    public int getMyPlayerId() {
        return myPlayerId;
    }

}
