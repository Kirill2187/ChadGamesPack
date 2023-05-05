package com.chadgames.gamespack.games.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
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
import static com.chadgames.gamespack.ui.UIScale.*;

public class ChatRenderer extends GameRenderer {

    private Label receivedMessages;
    private ScrollPane scrollPane;
    private TextArea textArea;

    private final StringBuilder currentMessages = new StringBuilder();
    private boolean update = false;
    private Table rootTable;
    public ChatRenderer(GameProcess gameProcess, Table rootTable) {
        super(gameProcess);
        this.rootTable = rootTable;

        createUI();
    }

    void send() {
        scrollDown();
        String text = textArea.getText();
        if (text.endsWith("\n")) text = text.substring(0, text.length() - 1);
        ChatMoveData textSent = new ChatMoveData(gameProcess.getMyPlayerId(), text);
        gameProcess.makeMoveAndSendToServer(textSent);

        textArea.setText("");
    }

    private void createUI() {
        Skin skin = GameManager.getInstance().skin;

        Table messages = new Table();
        messages.top();
        rootTable.add(messages).grow().pad(PADDING).row();

        Table bottomTable = new Table();
        rootTable.add(bottomTable).fillX().padLeft(PADDING).padRight(PADDING).padBottom(PADDING);

        textArea = new TextArea("", skin);
        bottomTable.add(textArea).padRight(PADDING).growX()
            .minWidth(percentWidth(.6f))
            .minHeight(percentHeight(.1f));
        textArea.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ENTER) {
                    if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SHIFT_LEFT)) return false;
                    send();
                    return true;
                }
                return super.keyDown(event, keycode);
            }
        });


        TextButton sendButton = new TextButton("Send", skin);
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                send();
            }
        });
        bottomTable.add(sendButton);

        receivedMessages = new Label("", skin, "default");
        receivedMessages.setAlignment(Align.top | Align.left);
        receivedMessages.setColor(GameManager.getInstance().skin.getColor("perfect_white"));

        scrollPane = new ScrollPane(receivedMessages, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.setSmoothScrolling(true);
        scrollPane.setVariableSizeKnobs(false);

        messages.add(scrollPane).grow().padTop(5).padLeft(5).padRight(5).row();

    }

    void scrollDown() {
        scrollPane.scrollTo(0, 0, 0, 0);
    }

    @Override
    public void render(float delta) {
        if (update) {
            boolean isScrolledDown = scrollPane.getScrollY() == scrollPane.getMaxY();
            receivedMessages.setText(currentMessages.toString());
            update = false;
            if (isScrolledDown) scrollDown();
        }
    }

    @Override
    public void makeActions(Actions actions) {
        String current_message = ((ChatActions) actions).messageToAdd;
        currentMessages.append(current_message).append("\n");
        update = true;
    }

    @Override
    public void loadFromState(GameState gameState) {
        receivedMessages.clear();
        currentMessages.setLength(0);
        for (String message : ((ChatState) gameState).messages) {
            currentMessages.append(message).append("\n");
        }
        update = true;
    }
}
