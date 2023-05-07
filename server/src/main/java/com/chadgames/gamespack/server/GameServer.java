package com.chadgames.gamespack.server;

import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.MoveData;
import com.chadgames.gamespack.network.Network;
import com.chadgames.gamespack.network.Request;
import com.chadgames.gamespack.network.RequestType;
import com.chadgames.gamespack.network.Response;
import com.chadgames.gamespack.network.ResponseType;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
    private Server server;
    private RoomManager roomManager = new RoomManager();
    private HashMap<Integer, User> users = new HashMap<>();
    private int curUserId = 0;

    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());

    GameServer() throws IOException {
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new MyConnection();
            }
        };
        Network.registerClasses(server);

        server.start();
        server.bind(Network.PORT);
        server.addListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
                onDisconnect(connection);
            }

            public void received(Connection connection, Object object) {
                if (object instanceof Request) {
                    processRequest((MyConnection) connection, (Request) object);
                }
            }
        });
        LOGGER.info("Game server started!");
    }

    private void onDisconnect(Connection connection) {
        if (!(connection instanceof MyConnection)) return;
        MyConnection myConnection = (MyConnection) connection;
        if (!myConnection.registered) return;

        roomManager.leaveRoom(getUserById(myConnection.userId));
        users.remove(myConnection.userId);
        LOGGER.fine("User " + myConnection.userId + " disconnected");
    }

    public void processRequest(MyConnection connection, Request request) {
        if (request == null) return;
        if (!connection.registered && request.requestType != RequestType.RegisterUser) {
            LOGGER.warning("Unregistered user tried to send request");
            return;
        }

        switch (request.requestType) {
            case RegisterUser: {
                String username = (String) request.data;
                int userId = registerUser(connection, username);
                LOGGER.fine(String.format("User %s registered, given id=%d", username, userId));
                break;
            }
            case ChangeUsername: {
                if (roomManager.getRoomIdByUserId(connection.userId) != -1) break;
                String newUsername = (String) request.data;
                LOGGER.finer(String.format("User %s changed username to %s", connection.username, newUsername));
                connection.username = newUsername;
                break;
            }
            case JoinRoom: {
                if (request.data instanceof GameType) {
                    joinRoomRequest(connection, (GameType) request.data);
                } else {
                    joinRoomRequest(connection, (int) request.data);
                }
                break;
            }
            case CreateRoom: {
                roomManager.joinNewRoom((GameType) request.data, getUserById(connection.userId));
                break;
            }
            case StartGame: {
                startGameRequest(connection);
                break;
            }
            case SendMove: {
                sendMoveRequest(connection, (MoveData) request.data);
                break;
            }
            case LeaveRoom: {
                LOGGER.fine("User " + connection.userId + " is leaving room");
                roomManager.leaveRoom(getUserById(connection.userId));
                break;
            }
        }
    }

    /* Stores user in a table, sends a response with userId, though it's probably unnecessary */
    private int registerUser(MyConnection connection, String username) {
        connection.registered = true;
        connection.username = username;

        int userId = curUserId++;
        connection.userId = userId;

        User user = new User(connection);
        users.put(userId, user);

        return userId;
    }

    private void joinRoomRequest(MyConnection connection, GameType gameType) {
        int userId = connection.userId;
        User user = getUserById(userId);
        roomManager.joinSomeRoom(gameType, user);
    }

    private void joinRoomRequest(MyConnection connection, int roomId) {
        int userId = connection.userId;
        User user = getUserById(userId);
        if (roomManager.isThereRoomWithId(roomId) && !roomManager.getRoomById(roomId).isFull()) {
            roomManager.joinRoom(roomId, user);
        } else {
            user.getConnection().sendTCP(
                    new Response(false, ResponseType.UserJoined, null)
            );
        }
    }

    private void startGameRequest(MyConnection connection) {
        int roomId = roomManager.getRoomIdByUserId(connection.userId);
        if (roomManager.getRoomById(roomId).startGame(connection.userId)) {
            roomManager.getRoomById(roomId).sendToAll(new Response(true, ResponseType.GameStarted, roomManager.getRoomById(roomId).getGameState()));
        } else {
            connection.sendTCP(new Response(false, ResponseType.GameStarted, null));
        }
    }

    private void sendMoveRequest(MyConnection connection, MoveData moveData) {
        int roomId = roomManager.getRoomIdByUserId(connection.userId);
        boolean success = false;
        if (roomId != -1) {
            success = roomManager.getRoomById(roomId).makeMove(moveData);
        }
        if (success) {
            LOGGER.fine("User " + connection.userId + " made a move");
            roomManager.getRoomById(roomId).sendToAllExcept(
                new Response(true, ResponseType.FetchMove, moveData),
                connection.userId
            );
            roomManager.checkWinnerAndNotify(roomId);
        } else {
            LOGGER.warning("User " + connection.userId + " tried to make an invalid move");
            connection.sendTCP(
                new Response(false, ResponseType.FetchGameState, roomManager.getRoomById(roomId).getGameState())
            );
        }
    }

    private User getUserById(int userId) {
        return users.get(userId);
    }

}
