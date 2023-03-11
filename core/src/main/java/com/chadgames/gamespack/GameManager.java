package com.chadgames.gamespack;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameManager extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}