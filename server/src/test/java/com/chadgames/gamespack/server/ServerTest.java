package com.chadgames.gamespack.server;

import org.junit.Test;
import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.chat.ChatMoveData;
import com.chadgames.gamespack.network.Network;
import com.chadgames.gamespack.network.PlayerAndRoomId;
import com.chadgames.gamespack.network.Request;
import com.chadgames.gamespack.network.RequestType;
import com.chadgames.gamespack.network.Response;
import com.chadgames.gamespack.network.ResponseType;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class ServerTest {

    @Test
    public void test() throws IOException, InterruptedException {
        GameServer server = new GameServer();

        Client client1 = new Client();
        client1.start();
        Network.registerClasses(client1);
        Client client2 = new Client();
        client2.start();
        Network.registerClasses(client2);
        final int[] test_message = {0};
        final boolean[] test_user_left = {false};

        Listener clientListener1 = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Response) {
                    if (((Response) object).responseType == ResponseType.FetchMove) {
                        ++test_message[0];
                        String message = ((ChatMoveData) ((Response) object).data).message;
                        assertEquals(message, "message_from_2");
                        client1.sendTCP(new Request(RequestType.LeaveRoom));
                    }
                }
            }
        };
        client1.addListener(clientListener1);
        Listener clientListener2 = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Response) {
                    if (((Response) object).responseType == ResponseType.FetchMove) {
                        ++test_message[0];
                        String message = ((ChatMoveData) ((Response) object).data).message;
                        assertEquals(message, "message_from_1");
                        client2.sendTCP(new Request(RequestType.SendMove, new ChatMoveData(1, "message_from_2")));
                    } else if (((Response) object).responseType == ResponseType.UserLeft) {
                        test_user_left[0] = true;
                    }
                }
            }
        };
        client2.addListener(clientListener2);

        client1.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                client1.sendTCP(new Request(RequestType.RegisterUser, "abac1"));
                client1.sendTCP(new Request(RequestType.JoinRoom, GameType.Chat));
                client1.sendTCP(new Request(RequestType.StartGame));
                try {
                    client2.connect(5000, Network.IP, Network.PORT);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        client2.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                client2.sendTCP(new Request(RequestType.RegisterUser, "abac2"));
                client2.sendTCP(new Request(RequestType.JoinRoom, 0));
                client1.sendTCP(new Request(RequestType.SendMove, new ChatMoveData(0, "message_from_1")));
            }
        });
        client1.connect(5000, Network.IP, Network.PORT);
        Thread.sleep(1000);
        assertEquals(test_message[0], 2);
        assertTrue(test_user_left[0]);
    }

}
