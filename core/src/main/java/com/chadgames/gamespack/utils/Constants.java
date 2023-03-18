package com.chadgames.gamespack.utils;

import static com.chadgames.gamespack.games.GameType.*;

import com.chadgames.gamespack.games.GameType;

import java.util.HashMap;

public final class Constants {
    private Constants() {}
    public static HashMap<GameType, Integer> minPlayersInRoom = new HashMap();
    public static HashMap<GameType, Integer> maxPlayersInRoom = new HashMap();
    static {
        minPlayersInRoom.put(Chat, 1);

        maxPlayersInRoom.put(Chat, 16);
    }
}
