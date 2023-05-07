package com.chadgames.gamespack.games;

public abstract class GameRenderer {

    protected GameProcess gameProcess;
    public GameRenderer(GameProcess gameProcess) {
        this.gameProcess = gameProcess;
    }

    public abstract void render(float delta);
    public abstract void makeActions(Actions actions);
    public abstract void loadFromState(GameState gameState);
}
