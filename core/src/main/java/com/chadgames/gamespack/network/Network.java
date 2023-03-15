package com.chadgames.gamespack.network;

import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.chat.ChatMoveData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

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

        kryo.register(ChatMoveData.class);
    }

}
