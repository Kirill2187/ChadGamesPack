package com.chadgames.gamespack.games;

public class GameProperties {
    private final int minPlayers;
    private final int autostartPlayers;
    private final int maxPlayers;
    private final boolean canJoinWhenStarted;
    public GameProperties(int x1, int x2, int x3, boolean x4) {
        minPlayers = x1;
        autostartPlayers = x2;
        maxPlayers = x3;
        canJoinWhenStarted = x4;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getAutostartPlayers() {
        return autostartPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean canJoinWhenStarted() {
        return canJoinWhenStarted;
    }
}
