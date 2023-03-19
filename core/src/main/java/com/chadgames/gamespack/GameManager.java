package com.chadgames.gamespack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.network.Network;
import com.chadgames.gamespack.network.Request;
import com.chadgames.gamespack.network.RequestType;
import com.chadgames.gamespack.screens.GameScreen;
import com.chadgames.gamespack.screens.MenuScreen;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager extends Game {

    private static GameManager instance;
    public Skin skin;
    public String username;
    public Client client;
    public static GameManager getInstance() {
        return instance;
    }
    public GameManager() {
        instance = this;
        username = generateDefaultUsername();

        client = new Client();
        client.start();
        Network.registerClasses(client);
    }
    private String generateDefaultUsername() {
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "Chad" + Integer.toString(randomNum);
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("ui/skin/flat-earth-ui.json")); // TODO: replace with custom skin
        asyncConnect();
        setScreen(new MenuScreen());
    }

    public void launchGame(GameType gameType) {
        setScreen(new GameScreen(gameType));
    }

    public void connect() {
        try {
            client.connect(5000, Network.IP, Network.PORT);

            client.addListener(new Listener() {
                @Override
                public void connected(Connection connection) {
                    Gdx.app.log("network", "Connected to server");
                    Request request = new Request();
                    request.requestType = RequestType.RegisterUser;
                    request.data = username;
                    client.sendTCP(request);
                }
                @Override
                public void disconnected(Connection connection) {
                    Gdx.app.log("network", "Disconnected from server");
                }
            });

        } catch (IOException e) {
            Gdx.app.log("network", "Failed to connect to server");
        }
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public void asyncConnect() {
        new Thread(this::connect).start();
    }
}
