package com.chadgames.gamespack.utils;

import static com.chadgames.gamespack.games.GameType.Chat;
import com.chadgames.gamespack.games.chat.ChatFactory;
import com.chadgames.gamespack.games.GameFactory;

import com.chadgames.gamespack.games.GameType;

import java.util.HashMap;

public final class Constants {
    private Constants() {}

    public static final HashMap<GameType, GameFactory> GAME_FACTORIES = new HashMap<>();
    public static HashMap<GameType, Integer> minPlayersInRoom = new HashMap();
    public static HashMap<GameType, Integer> autostartPlayersInRoom = new HashMap();
    public static HashMap<GameType, Integer> maxPlayersInRoom = new HashMap();

    static {
        minPlayersInRoom.put(Chat, 1);
        autostartPlayersInRoom.put(Chat, 2);
        maxPlayersInRoom.put(Chat, 100);

        GAME_FACTORIES.put(GameType.Chat, new ChatFactory());

    }
}
