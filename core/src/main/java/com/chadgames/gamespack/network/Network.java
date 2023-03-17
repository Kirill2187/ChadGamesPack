package com.chadgames.gamespack.network;

import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.chat.ChatMoveData;
import com.chadgames.gamespack.games.chat.ChatState;
import com.chadgames.gamespack.utils.Player;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;
import java.util.HashMap;

public final class Network {

    private Network() {}
    public static int PORT = 54555;
    public static String IP = "localhost"; // TODO: It's just for testing purposes

    public static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Request.class);
        kryo.register(Response.class);
        kryo.register(RequestType.class);
        kryo.register(ResponseType.class);
        kryo.register(GameType.class);
        kryo.register(Player.class);
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);

        kryo.register(ChatMoveData.class);
        kryo.register(ChatState.class);

    }

}
