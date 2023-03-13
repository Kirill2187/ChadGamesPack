package com.chadgames.gamespack.network;

import com.chadgames.gamespack.games.chat.ChatMoveData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

    public static int PORT = 54555;
    public static String IP = "localhost"; // TODO: It's just for testing purposes

    public static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Request.class);
        kryo.register(Response.class);
        kryo.register(RequestType.class);
        kryo.register(ResponseType.class);

        kryo.register(ChatMoveData.class);
    }

}
