package com.chadgames.gamespack.server;

import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.network.Network;
import com.chadgames.gamespack.network.Request;
import com.chadgames.gamespack.network.Response;
import com.chadgames.gamespack.network.ResponseType;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class GameServer {
    private Server server;
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private HashMap<Integer, User> users = new HashMap<>();
    private int curUserId = 0;

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
            if (cur.getGameType() == gameType && !cur.full() && !cur.isActive()) {
                return key;
            }
        }
        return -1;
    }

    GameServer() throws IOException {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new MyConnection();
            }
        };
        Network.registerClasses(server);

        server.start();
        server.bind(Network.PORT); // TODO: magic constants
        server.addListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
                if (!(connection instanceof MyConnection)) return;
                MyConnection myConnection = (MyConnection) connection;
                if (!myConnection.registered) return;

                leaveRoom(myConnection.userId);
                users.remove(myConnection.userId);
                System.out.println("User " + myConnection.userId + " disconnected");
            }

            public void received(Connection connection, Object object) {
                if (object instanceof Request) {
                    processRequest((MyConnection) connection, (Request) object);
                } else {
                     System.out.print("Unknown object received ");
                     if (connection instanceof MyConnection) {
                         System.out.println("from " + ((MyConnection) connection).username);
                     } else {
                         System.out.println("from unregistered user");
                     }
                }
            }
        });
    }

    private int createRoom(GameType gameType, int maxMembers) {
        Random rand = new Random();
        int roomId = rand.nextInt();
        rooms.put(roomId, new Room(gameType, maxMembers));
        return roomId;
    }

    private void deleteRoom(int roomId) {
        rooms.remove(roomId);
    }

    private void joinRoom(int roomId, User user) {
        rooms.get(roomId).join(user);
    }

    private void leaveRoom(int userId) {
        int ejectRoomId = getRoomId(userId);
        if (ejectRoomId != -1) {
            System.out.println("User " + userId + " disconnected from room " + ejectRoomId);
            Room ejectRoom = rooms.get(ejectRoomId);
            ejectRoom.leave(userId);
            if (ejectRoom.size() == 0) {
                deleteRoom(ejectRoomId);
            }
        }
    }

    public void processRequest(MyConnection connection, Request request) {
        // TODO: check if user is valid: id matches with connection, and has access rights
        if (request == null) return;
        System.out.println("Request received");
        switch (request.requestType) {
            case RegisterUser: {
                String username = (String) request.data;
                int userId = registerUser(connection, username);
                System.out.println(String.format("User %s registered, given id=%d", username, userId));
                break;
            }
            case JoinRoom: {
                if (request.data instanceof GameType) {
                    int accessibleRoomId = getAccessibleRoomIdByType((GameType) request.data);
                    if (accessibleRoomId != -1) {
                        joinRoom(accessibleRoomId, getUserById(request.userId));
                        // TODO: response: OK, accessibleRoomId
                    } else {
                        // TODO: response: FAIL
                    }
                } else {
                    int roomId = (int) request.data;
                    if (rooms.containsKey(roomId)) {
                        joinRoom(roomId, getUserById(request.userId));
                    }
                    // TODO: response: OK/Failure
                }
                // TODO: also should send full room state
                break;
            }
            case CreateRoom: {
                int newRoom = createRoom((GameType) request.data, 4); // TODO: somehow receive maxMembers
                rooms.get(newRoom).join(getUserById(request.userId));
                // TODO: response success
                break;
            }
            case SendMove: {
                int roomId = getRoomId(request.userId);
                if (roomId != -1) {
                    rooms.get(roomId).makeMove(request.userId, request.data);
                }
                // TODO: broadcast changes
                break;
            }
            case LeaveRoom: {
                leaveRoom(request.userId);
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

        Response response = new Response();
        response.responseType = ResponseType.UserRegistered;
        response.success = true;
        response.data = userId; // TODO: Does user really need his id? We can determine him by connection
        connection.sendTCP(response);

        return userId;
    }

    private User getUserById(int userId) {
        return users.get(userId);
    }

}
