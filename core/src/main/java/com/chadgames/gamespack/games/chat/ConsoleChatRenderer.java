package com.chadgames.gamespack.games.chat;

import com.chadgames.gamespack.games.Actions;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;

import java.io.Console;

public class ConsoleChatRenderer extends GameRenderer {

    Console console;
    public ConsoleChatRenderer(GameProcess gameProcess) {
        super(gameProcess);
        console = System.console();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String message = readMessage();
                    if (message == null || message.isEmpty()) {
                        continue;
                    }
                    ChatMoveData data = new ChatMoveData();
                    data.message = message;
                    data.playerId = gameProcess.getMyPlayerId();
                    gameProcess.makeMoveAndSendToServer(data);
                }
            }
        }).start();

    }

    String readMessage() {
        if (console != null) {
            return console.readLine();
        }
        return null;
    }

    @Override
    public void render(float delta) {}

    @Override
    public void makeActions(Actions actions) {
        ChatActions chatActions = (ChatActions) actions;
        System.out.println(chatActions.messageToAdd);
    }

    @Override
    public void loadFromState(GameState gameState) {
        System.out.println("\n\n\nState load\n\n\n");
        for (String message : ((ChatState) gameState).messages) {
            System.out.println(message);
        }
    }
}
