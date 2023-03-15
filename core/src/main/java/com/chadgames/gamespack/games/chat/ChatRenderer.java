package com.chadgames.gamespack.games.chat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.ActionsSequence;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.MoveData;
import com.chadgames.gamespack.network.Request;
import com.chadgames.gamespack.network.RequestType;

public class ChatRenderer extends GameRenderer {

    private Label receivedMessages;
    public ChatRenderer(GameProcess gameProcess, Stage stage, SpriteBatch batch) {
        super(gameProcess, stage, batch);
        Gdx.app.log("debug", "Chat renderer created");

        createUI();
    }

    private void createUI() {
        Skin skin = GameManager.getInstance().skin;

        Table root = new Table();
        root.debugAll();
        root.setFillParent(true);
        stage.addActor(root);
        root.top();

        Table messages = new Table();
        messages.top();
        root.add(messages).grow().padTop(5).row();

        Table bottomTable = new Table();
        root.add(bottomTable).fillX().padLeft(10).padRight(10);

        TextField testTextField = new TextField("", skin);
        bottomTable.add(testTextField).padRight(5).growX().minWidth(100);

        TextButton sendButton = new TextButton("Send", skin);
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ChatMoveData textSent = new ChatMoveData();
                textSent.message = testTextField.getText();
                gameProcess.makeMoveAndSendToServer(0, textSent);
            }
        });
        bottomTable.add(sendButton);

        receivedMessages = new Label("", skin, "default");
        receivedMessages.setAlignment(Align.top);
        receivedMessages.setColor(Color.PURPLE);
        messages.add(receivedMessages).grow();
    }

    @Override
    public void render(float delta) {
        // Do nothing
    }

    @Override
    public void makeActions(ActionsSequence actionsSequence) {
        String current_message = ((ChatActionsSequence)actionsSequence).messageToAdd;
        Gdx.app.log("debug", "New message: " + current_message);
        receivedMessages.setText(receivedMessages.getText() + "\n" + current_message);
    }
}
