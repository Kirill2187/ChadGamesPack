package com.chadgames.gamespack.games.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.Actions;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.network.Network;

public class ChatRenderer extends GameRenderer {

    private Label receivedMessages;
    public ChatRenderer(GameProcess gameProcess, Table rootTable, SpriteBatch batch) {
        super(gameProcess, rootTable, batch);
        Gdx.app.log("debug", "Chat renderer created");

        createUI();
    }

    private void createUI() {
        Skin skin = GameManager.getInstance().skin;

        Table messages = new Table();
        messages.top();
        rootTable.add(messages).grow().padTop(5).padLeft(5).padRight(5).row();

        Table bottomTable = new Table();
        rootTable.add(bottomTable).fillX().padLeft(10).padRight(10);

        TextField testTextField = new TextField("", skin);
        bottomTable.add(testTextField).padRight(5).growX().minWidth(100);

        TextButton sendButton = new TextButton("Send", skin);
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ChatMoveData textSent = new ChatMoveData(gameProcess.getMyPlayerId(),
                                                        testTextField.getText());
                gameProcess.makeMoveAndSendToServer(textSent);
            }
        });
        bottomTable.add(sendButton);

        receivedMessages = new Label("", skin, "default");
        receivedMessages.setAlignment(Align.top | Align.left);
        receivedMessages.setColor(Color.PURPLE);

        ScrollPane scrollPane = new ScrollPane(receivedMessages, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.setSmoothScrolling(true);
        scrollPane.setVariableSizeKnobs(false);

        messages.add(scrollPane).grow().padTop(5).padLeft(5).padRight(5).row();

    }

    @Override
    public void render(float delta) {
        // Do nothing
    }

    @Override
    public void makeActions(Actions actions) {
        String current_message = ((ChatActions) actions).messageToAdd;
        receivedMessages.setText(receivedMessages.getText() + "\n" + current_message);
    }

    @Override
    public void loadFromState(GameState gameState) {
        receivedMessages.clear();
        StringBuilder messages = new StringBuilder();
        for (String message : ((ChatState) gameState).messages) {
            messages.append(message).append("\n");
        }
        messages.setLength(messages.length() - 1);
        receivedMessages.setText(messages.toString());
    }
}
