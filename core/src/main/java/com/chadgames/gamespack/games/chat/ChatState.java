package com.chadgames.gamespack.games.chat;

import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatState implements GameState {
    private ArrayList<String> messages = new ArrayList<>();

    @Override
    public ChatActionsSequence makeMove(MoveData moveData) {
        String message = "[" + ((ChatMoveData) moveData).playerId + "] " + ((ChatMoveData) moveData).message;
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
