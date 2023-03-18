package com.chadgames.gamespack.utils;

import static com.chadgames.gamespack.games.GameType.Chat;

import com.chadgames.gamespack.games.GameProperties;
import com.chadgames.gamespack.games.chat.ChatFactory;
import com.chadgames.gamespack.games.GameFactory;

import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.ticktacktoe.TickTackToeFactory;

import java.util.HashMap;

public final class Constants {
    private Constants() {}

    public static final HashMap<GameType, GameFactory> GAME_FACTORIES = new HashMap<>();
    public static HashMap<GameType, GameProperties> gameProperties = new HashMap();

    static {
        gameProperties.put(Chat, new GameProperties(1, 2, 100, true));
        gameProperties.put(GameType.TickTackToe, new GameProperties(2, 2, 2, false));

        GAME_FACTORIES.put(GameType.Chat, new ChatFactory());
        GAME_FACTORIES.put(GameType.TickTackToe, new TickTackToeFactory());

    }
}
