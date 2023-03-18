package com.chadgames.gamespack.utils;

import com.chadgames.gamespack.games.GameFactory;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.chat.ChatFactory;

import java.util.HashMap;

public final class Constants {
    private Constants() {}

    public static final HashMap<GameType, GameFactory> GAME_FACTORIES = new HashMap<>();
    static {
        GAME_FACTORIES.put(GameType.Chat, new ChatFactory());
    }

}
