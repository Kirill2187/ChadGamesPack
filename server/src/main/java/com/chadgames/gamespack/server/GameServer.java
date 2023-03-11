package com.chadgames.gamespack.server;

import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.network.Request;
import com.chadgames.gamespack.network.User;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;

public class GameServer {
    private Server server;
    private HashMap<Integer, Room> rooms;
    private HashMap<Integer, User> users;

    private Room getRoom(int userId) {
        for (Room room: rooms.values()) {
            if (room.hasUser(userId)) {
                return room;
            }
        }
        return null;
    }
    private int getRoomId(int userId) {
        for (int key: rooms.keySet()) {
            if (rooms.get(key).hasUser(userId)) {
                return key;
            }
        }
        return -1;
    }

    GameServer() throws IOException {
        server = new Server();
        server.start();
        server.bind(54555, 54777); // TODO: magic constants
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

    private int createRoom(GameType gameType) {
        return -1; // TODO: create room
    }

    private void deleteRoom(int roomId) {
        rooms.remove(roomId);
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
                int roomId = (int)request.data; // TODO: allow to enter any free room
                if (rooms.containsKey(roomId)) {
                    rooms.get(roomId).join(getUserById(request.userId));
                }
                break;
            }
            case CreateRoom: {
                int newRoom = createRoom((GameType) request.data);
                rooms.get(newRoom).join(getUserById(request.userId));
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
        // TODO: register user
        int userId = -1;
        users.put(userId, new User());
    }

    private User getUserById(int userId) {
        return users.get(userId);
    }

}
