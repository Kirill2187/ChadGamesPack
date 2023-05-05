package com.chadgames.gamespack.network;

import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.chat.ChatMoveData;
import com.chadgames.gamespack.games.chat.ChatState;
import com.chadgames.gamespack.games.reversi.ReversiMoveData;
import com.chadgames.gamespack.games.tictactoe.TicTacToeMoveData;
import com.chadgames.gamespack.games.tictactoe.TicTacToeState;
import com.chadgames.gamespack.games.reversi.ReversiState;
import com.chadgames.gamespack.utils.Player;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;
import java.util.HashMap;

public final class Network {

    private Network() {}
    public static int PORT = 54555;
    public static String IP = "158.160.32.100"; // TODO: should it be hardcoded?
//    public static String IP = "localhost";

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

        kryo.register(TicTacToeMoveData.class);
        kryo.register(ReversiMoveData.class);
        kryo.register(com.chadgames.gamespack.games.tictactoe.Symbol.class);
        kryo.register(com.chadgames.gamespack.games.tictactoe.Symbol[][].class);
        kryo.register(com.chadgames.gamespack.games.tictactoe.Symbol[].class);
        kryo.register(com.chadgames.gamespack.games.reversi.Symbol.class);
        kryo.register(com.chadgames.gamespack.games.reversi.Symbol[][].class);
        kryo.register(com.chadgames.gamespack.games.reversi.Symbol[].class);
        kryo.register(TicTacToeState.class);
        kryo.register(ReversiState.class);
        kryo.register(PlayerAndRoomId.class);
    }

}
