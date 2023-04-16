package com.chadgames.gamespack.games.chat;

import com.chadgames.gamespack.games.MoveData;

public class ChatMoveData extends MoveData {
    String message;
    ChatMoveData() {}
    public ChatMoveData(int playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }
}
