package com.chadgames.gamespack.server;

import org.junit.Test;
import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.GameState;
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
    public void test1() throws IOException, InterruptedException {
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
                        System.out.println("client1 receive FetchMove");
                        ++test_message[0];
                        String message = ((ChatMoveData) ((Response) object).data).message;
                        assertEquals(message, "message_from_2");
                        System.out.println("client1 send LeaveRoom");
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
                        System.out.println("client2 receive FetchMove");
                        ++test_message[0];
                        String message = ((ChatMoveData) ((Response) object).data).message;
                        assertEquals(message, "message_from_1");
                        System.out.println("client2 send SendMove");
                        client2.sendTCP(new Request(RequestType.SendMove, new ChatMoveData(1, "message_from_2")));
                    } else if (((Response) object).responseType == ResponseType.UserLeft) {
                        System.out.println("client2 receive UserLeft");
                        test_user_left[0] = true;
                    }
                }
            }
        };
        client2.addListener(clientListener2);

        client1.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("client1 send RegisterUser");
                client1.sendTCP(new Request(RequestType.RegisterUser, "abac1"));
                System.out.println("client1 send JoinRoom");
                client1.sendTCP(new Request(RequestType.JoinRoom, GameType.Chat));
                System.out.println("client1 send StartGame");
                client1.sendTCP(new Request(RequestType.StartGame));
                System.out.println("client2 connect");
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
                System.out.println("client2 send RegisterUser");
                client2.sendTCP(new Request(RequestType.RegisterUser, "abac2"));
                System.out.println("client2 send JoinRoom");
                client2.sendTCP(new Request(RequestType.JoinRoom, 0));
                System.out.println("client1 send SendMove");
                client1.sendTCP(new Request(RequestType.SendMove, new ChatMoveData(0, "message_from_1")));
            }
        });
        System.out.println("client1 connect");
        client1.connect(5000, Network.IP, Network.PORT);
        Thread.sleep(2000);
        System.out.println("start assertions");
        assertEquals(test_message[0], 2);
        assertTrue(test_user_left[0]);

        server.shutDown();
    }

    @Test
    public void test2() throws IOException, InterruptedException {
        GameServer server = new GameServer();

        Client client1 = new Client();
        client1.start();
        Network.registerClasses(client1);
        Client client2 = new Client();
        client2.start();
        Network.registerClasses(client2);
        final int[] test_passed = {0};

        Listener clientListener2 = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Response) {
                    if (((Response) object).responseType == ResponseType.UserJoined) {
                        System.out.println("client2 receive failure UserJoined");
                        ++test_passed[0];
                        assertFalse(((Response) object).success);
                        System.out.println("client2 send JoinRoom");
                        client2.sendTCP(new Request(RequestType.JoinRoom, GameType.Chat));
                    } else if (((Response) object).responseType == ResponseType.FetchGameState) {
                        if (((Response) object).success) {
                            System.out.println("client2 receive FetchGameState");
                            ++test_passed[0];
                            String username1 = ((GameState) ((Response) object).data).getPlayerById(0).username;
                            assertEquals(username1, "ABAC1");
                            client2.sendTCP(new Request(RequestType.SendMove, new ChatMoveData(1, "")));
                        } else {
                            System.out.println("client2 receive failure FetchGameState");
                            ++test_passed[0];
                        }
                    }
                }
            }
        };
        client2.addListener(clientListener2);

        client1.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("client1 send RegisterUser");
                client1.sendTCP(new Request(RequestType.RegisterUser, "abac1"));
                System.out.println("client1 send ChangeUsername");
                client1.sendTCP(new Request(RequestType.ChangeUsername, "ABAC1"));
                System.out.println("client1 send CreateRoom");
                client1.sendTCP(new Request(RequestType.CreateRoom, GameType.Chat));
                System.out.println("client1 send StartGame");
                client1.sendTCP(new Request(RequestType.StartGame));
                System.out.println("client2 connect");
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
                System.out.println("client2 send RegisterUser");
                client2.sendTCP(new Request(RequestType.RegisterUser, "abac2"));
                System.out.println("client2 try to send JoinRoom 444");
                client2.sendTCP(new Request(RequestType.JoinRoom, 444));
            }
        });
        System.out.println("client1 connect");
        client1.connect(5000, Network.IP, Network.PORT);
        Thread.sleep(2000);
        System.out.println("start assertions");
        assertEquals(test_passed[0], 3);

        server.shutDown();
    }

}
