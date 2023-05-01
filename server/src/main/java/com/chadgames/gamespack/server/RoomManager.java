package com.chadgames.gamespack.server;

import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.network.PlayerAndRoomId;
import com.chadgames.gamespack.network.Response;
import com.chadgames.gamespack.network.ResponseType;

import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomManager {
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private int curRoomId = 0;
    private static final Logger LOGGER = Logger.getLogger(RoomManager.class.getName());

    RoomManager() {
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
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

    private int createRoom(GameType gameType) {
        int roomId = curRoomId++;
        rooms.put(roomId, new Room(gameType));
        return roomId;
    }

    private void deleteRoom(int roomId) {
        rooms.remove(roomId);
    }

    public boolean checkWinnerAndNotify(int roomId) {
        GameState gameState = rooms.get(roomId).getGameState();
        if (gameState.isGameFinished()) {
            LOGGER.fine("Game in room " + roomId + " finished, winnerId is " + gameState.getWinner());
            Room room = rooms.get(roomId);
            room.sendToAll(
                    new Response(true, ResponseType.GameFinished, gameState.getWinner())
            );
            kickAll(room);
            room.reset();
            return true;
        }
        return false;
    }

    public int getRoomIdByUserId(int userId) {
        for (int key : rooms.keySet()) {
            if (rooms.get(key).hasUser(userId)) {
                return key;
            }
        }
        return -1;
    }

    public Room getRoomById(int roomId) {
        return rooms.get(roomId);
    }

    public boolean isThereRoomWithId(int roomId) {
        return rooms.containsKey(roomId);
    }

    public void joinRoom(int roomId, User user) {
        Room room = rooms.get(roomId);
        boolean activeBefore = room.isActive();
        room.join(user); // After this call user.player must be initialized and assigned an id
        assert user.player != null;

        LOGGER.finer("User " + user.getUsername() + " joined room " + roomId);
        LOGGER.finer("User " + user.getUsername() + " assigned id " + user.player.id);

        user.getConnection().sendTCP(
                new Response(true, ResponseType.PlayerIdAssigned, new PlayerAndRoomId(user.player.id, roomId))
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
            LOGGER.fine("Game in room " + roomId + " started");
        }
    }

    public void joinNewRoom(GameType gameType, User user) {
        joinRoom(createRoom(gameType), user);
    }

    public int joinSomeRoom(GameType gameType, User user) {
        int roomId = getAccessibleRoomIdByType(gameType);

        if (roomId == -1) {
            roomId = createRoom(gameType);
        }
        joinRoom(roomId, user);

        return roomId;
    }

    public void leaveRoom(User user) {
        int userId = user.getUserId();
        int ejectRoomId = getRoomIdByUserId(userId);
        if (ejectRoomId != -1) {
            LOGGER.finer("User " + user.getUsername() + " disconnected from room " + ejectRoomId);

            Room ejectRoom = rooms.get(ejectRoomId);
            boolean wasFinished = ejectRoom.getGameState().isGameFinished();

            ejectRoom.leave(userId);
            ejectRoom.sendToAll(
                    new Response(true, ResponseType.UserLeft, user.player)
            );
            if (!wasFinished) checkWinnerAndNotify(ejectRoomId);

            if (ejectRoom.size() == 0) {
// Do not delete since we need to allow to restart in the same room
//                deleteRoom(ejectRoomId);
            }
        }
    }
    public void kickAll(Room room) {
        LOGGER.fine("Kick everyone");
        while (!room.getUsers().isEmpty()) {
            leaveRoom(room.getUsers().get(room.getUsers().size() - 1));
        }
    }

}
