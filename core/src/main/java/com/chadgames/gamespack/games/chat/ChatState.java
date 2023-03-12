package com.chadgames.gamespack.games.chat;

import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;

import java.util.ArrayList;

public class ChatState implements GameState {
    public ArrayList<String> messages;

    @Override
    public ChatActionsSequence makeMove(int userId, MoveData moveData) {
        String message = '[' + ((Integer)userId).toString() + ']' + ((ChatMoveData)moveData).message;
        messages.add(message);

        ChatActionsSequence actionsSequence = new ChatActionsSequence();
        actionsSequence.messageToAdd = message;
        return actionsSequence;
    }

    @Override
    public boolean checkMove(int userId, MoveData moveData) {
        return true;
    }
}
