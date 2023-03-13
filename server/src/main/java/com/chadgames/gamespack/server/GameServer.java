package com.chadgames.gamespack.server;

import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.network.Request;
import com.chadgames.gamespack.network.User;
import com.chadgames.gamespack.utils.Constants;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class GameServer {
    private Server server;
    private HashMap<Integer, Room> rooms;
    private HashMap<Integer, User> users;

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
        rooms = new HashMap<>();
        users = new HashMap<>();
        server = new Server();
        server.start();
        server.bind(Constants.TCP_PORT, Constants.UDP_PORT);
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Request) {
                    processRequest(connection, (Request) object);
                } else {
                    System.out.println("Unknown object received");
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
        System.out.print("User " + userId);
        int ejectRoomId = getRoomId(userId);
        Room ejectRoom = rooms.get(ejectRoomId);
        System.out.println(" disconnected from room " + ejectRoomId);
        if (ejectRoomId != -1) {
            ejectRoom.leave(userId);
            if (ejectRoom.size() == 0) {
                deleteRoom(ejectRoomId);
            }
        }
    }

    public void processRequest(Connection connection, Request request) {
        // TODO: check if user is valid: id matches with connection, and has access rights
        if (request == null) return;
        switch (request.requestType) {
            case RegisterUser: {
                registerUser();
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
            case Disconnect: {
                leaveRoom(request.userId);
                users.remove(request.userId);
                System.out.println("User " + request.userId + " disconnected");
                break;
            }
        }
    }

    private void registerUser() {
        Random rand = new Random();
        int userId = rand.nextInt();
        users.put(userId, new User());
    }

    private User getUserById(int userId) {
        return users.get(userId);
    }

}
