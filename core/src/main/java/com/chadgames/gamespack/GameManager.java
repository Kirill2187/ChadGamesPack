package com.chadgames.gamespack;

import com.badlogic.gdx.Game;
import com.chadgames.gamespack.screens.MenuScreen;

public class GameManager extends Game {

    private static GameManager instance;
    public static GameManager getInstance() {
        return instance;
    }
    public GameManager() {
        instance = this;
    }

    @Override
    public void create() {
        setScreen(new MenuScreen());
    }
}
