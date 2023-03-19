package com.chadgames.gamespack.games.chat;

import com.chadgames.gamespack.games.Actions;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;
import com.chadgames.gamespack.utils.Player;

import java.util.ArrayList;

public class ChatState extends GameState {
    ArrayList<String> messages = new ArrayList<>();

    @Override
    public Actions playerJoined(Player player) {
        String message = "[" + player.username + "] joined";
        messages.add(message);
        ChatActions actionsSequence = new ChatActions();
        actionsSequence.messageToAdd = message;
        return actionsSequence;
    }

    @Override
    public Actions playerLeft(Player player) {
        String message = "[" + player.username + "] left";
        messages.add(message);
        ChatActions actionsSequence = new ChatActions();
        actionsSequence.messageToAdd = message;
        return actionsSequence;
    }

    @Override
    public ChatActions makeMove(MoveData moveData) {
        String username = getPlayerById(((ChatMoveData) moveData).playerId).username;
        String message = "[" + username + "] " + ((ChatMoveData) moveData).message;
        messages.add(message);

        ChatActions actionsSequence = new ChatActions();
        actionsSequence.messageToAdd = message;
        return actionsSequence;
    }

    @Override
    public boolean checkMove(MoveData moveData) {
        return !((ChatMoveData) moveData).message.trim().equals("");
    }
}
