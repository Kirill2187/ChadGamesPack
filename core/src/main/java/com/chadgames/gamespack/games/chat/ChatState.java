package com.chadgames.gamespack.games.chat;

import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;

import java.util.ArrayList;

public class ChatState implements GameState {
    public ArrayList<String> messages;

    @Override
    public void makeMove(int userId, MoveData moveData) {
        messages.add('[' + ((Integer)userId).toString() + ']' + ((ChatMoveData)moveData).message);
    }

    @Override
    public boolean checkMove() {
        return true;
    }
}
