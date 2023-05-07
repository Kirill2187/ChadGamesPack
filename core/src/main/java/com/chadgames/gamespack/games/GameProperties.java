package com.chadgames.gamespack.games;

public class GameProperties {
    private final int minPlayers;
    private final int autostartPlayers;
    private final int maxPlayers;
    private final boolean canJoinWhenStarted;
    public GameProperties(int minPlayers, int autostartPlayers, int maxPlayers, boolean canJoinWhenStarted) {
        this.minPlayers = minPlayers;
        this.autostartPlayers = autostartPlayers;
        this.maxPlayers = maxPlayers;
        this.canJoinWhenStarted = canJoinWhenStarted;
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
