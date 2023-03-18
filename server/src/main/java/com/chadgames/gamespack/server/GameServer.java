package com.chadgames.gamespack.server;

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
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private HashMap<Integer, User> users = new HashMap<>();
    private int curUserId = 0;
    private int curRoomId = 0;

    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());

    private int getRoomId(int userId) {
        for (int key : rooms.keySet()) {
            if (rooms.get(key).hasUser(userId)) {
                return key;
            }
        }
        return -1;
    }

    private int getAccessibleRoomIdByType(GameType gameType) {
        for (int key : rooms.keySet()) {
            Room cur = rooms.get(key);
            if (cur.getGameType() == gameType && !cur.isFull() && (!cur.isActive() || cur.getProperties().canJoinWhenStarted())) {
                return key;
            }
        }
        return -1;
    }

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
                if (!(connection instanceof MyConnection)) return;
                MyConnection myConnection = (MyConnection) connection;
                if (!myConnection.registered) return;

                leaveRoom(getUserById(myConnection.userId));
                users.remove(myConnection.userId);
                LOGGER.fine("User " + myConnection.userId + " disconnected");
            }

            public void received(Connection connection, Object object) {
                if (object instanceof Request) {
                    processRequest((MyConnection) connection, (Request) object);
                }
            }
        });
        LOGGER.info("Game server started!");
    }

    private int createRoom(GameType gameType) {
        int roomId = curRoomId++;
        rooms.put(roomId, new Room(gameType));
        return roomId;
    }

    private void deleteRoom(int roomId) {
        rooms.remove(roomId);
    }

    private void joinRoom(int roomId, User user) {
        Room room = rooms.get(roomId);
        boolean activeBefore = room.isActive();
        room.join(user); // After this call user.player must be initialized and assigned an id
        assert user.player != null;

        LOGGER.finer("User " + user.getUsername() + " joined room " + roomId);
        LOGGER.finer("User " + user.getUsername() + " assigned id " + user.player.id);

        user.getConnection().sendTCP(
                new Response(true, ResponseType.PlayerIdAssigned, user.player.id)
        );
        user.getConnection().sendTCP(
                new Response(true, ResponseType.FetchGameState, room.getGameState())
        );
        // We do not need to send "user joined" message to the user who joined,
        // because updates are already fetched in the previous line
        room.sendToAllExcept(
                new Response(true, ResponseType.UserJoined, user.player), user.getUserId()
        );
        boolean activeAfter = room.isActive();
        if (activeAfter && !activeBefore) {
            room.sendToAll(new Response(true, ResponseType.GameStarted, room.getGameState()));
        }

    }

    private void leaveRoom(User user) {
        int userId = user.getUserId();
        int ejectRoomId = getRoomId(userId);
        if (ejectRoomId != -1) {
            LOGGER.finer("User " + user.getUsername() + " disconnected from room " + ejectRoomId);

            Room ejectRoom = rooms.get(ejectRoomId);
            ejectRoom.leave(userId);
            ejectRoom.sendToAll(
                    new Response(true, ResponseType.UserLeft, user.player)
            );

            if (ejectRoom.size() == 0) {
                deleteRoom(ejectRoomId);
            }
        }
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
            case JoinRoom: {
                int userId = connection.userId;
                User user = getUserById(userId);
                if (request.data instanceof GameType) {
                    GameType gameType = (GameType) request.data;
                    joinSomeRoom(gameType, user);
                } else {
                    int roomId = (int) request.data;
                    if (rooms.containsKey(roomId) && !rooms.get(roomId).isFull()) {
                        joinRoom(roomId, user);
                    } else {
                        user.getConnection().sendTCP(
                                new Response(false, ResponseType.UserJoined, null)
                        );
                    }
                }
                break;
            }
            case CreateRoom: {
                joinRoom(createRoom((GameType) request.data), getUserById(connection.userId));
                break;
            }
            case StartGame: {
                int roomId = getRoomId(connection.userId);
                if (rooms.get(roomId).startGame(connection.userId)) {
                    rooms.get(roomId).sendToAll(new Response(true, ResponseType.GameStarted, rooms.get(roomId).getGameState()));
                } else {
                    connection.sendTCP(new Response(false, ResponseType.GameStarted, null));
                }
                break;
            }
            case SendMove: {
                int roomId = getRoomId(connection.userId);
                boolean success = false;
                if (roomId != -1) {
                    success = rooms.get(roomId).makeMove((MoveData) request.data);
                }
                if (success) {
                    rooms.get(roomId).sendToAllExcept(
                            new Response(true, ResponseType.FetchMove, request.data),
                            connection.userId
                    );
                } else {
                    connection.sendTCP(
                            new Response(false, ResponseType.FetchGameState, rooms.get(roomId).getGameState())
                    );
                }
                break;
            }
            case LeaveRoom: {
                leaveRoom(getUserById(connection.userId));
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

    private int joinSomeRoom(GameType gameType, User user) {
        int roomId = getAccessibleRoomIdByType(gameType);

        if (roomId == -1) {
            roomId = createRoom(gameType);
        }
        joinRoom(roomId, user);

        return roomId;
    }

    private User getUserById(int userId) {
        return users.get(userId);
    }

}
