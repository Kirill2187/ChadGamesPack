package com.chadgames.gamespack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.screens.GameScreen;
import com.chadgames.gamespack.screens.MenuScreen;

public class GameManager extends Game {

    private static GameManager instance;
    public Skin skin;
    public static GameManager getInstance() {
        return instance;
    }
    public GameManager() {
        instance = this;
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("ui/skin/flat-earth-ui.json")); // TODO: replace with custom skin
        setScreen(new MenuScreen());
    }

    public void launchGame(GameType gameType) {
        setScreen(new GameScreen(gameType));
    }
}
