package com.chadgames.gamespack.games.chat;

import com.chadgames.gamespack.games.ActionsSequence;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;
import com.chadgames.gamespack.utils.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatState extends GameState {
    ArrayList<String> messages = new ArrayList<>();

    @Override
    public ActionsSequence playerJoined(Player player) {
        String message = "[" + player.username + "] joined";
        messages.add(message);
        ChatActionsSequence actionsSequence = new ChatActionsSequence();
        actionsSequence.messageToAdd = message;
        return actionsSequence;
    }

    @Override
    public ActionsSequence playerLeft(Player player) {
        String message = "[" + player.username + "] left";
        messages.add(message);
        ChatActionsSequence actionsSequence = new ChatActionsSequence();
        actionsSequence.messageToAdd = message;
        return actionsSequence;
    }

    @Override
    public ChatActionsSequence makeMove(MoveData moveData) {
        String username = getPlayerById(((ChatMoveData) moveData).playerId).username;
        String message = "[" + username + "] " + ((ChatMoveData) moveData).message;
        messages.add(message);

        ChatActionsSequence actionsSequence = new ChatActionsSequence();
        actionsSequence.messageToAdd = message;
        return actionsSequence;
    }

    @Override
    public boolean checkMove(MoveData moveData) {
        return !((ChatMoveData) moveData).message.trim().equals("");
    }
}
