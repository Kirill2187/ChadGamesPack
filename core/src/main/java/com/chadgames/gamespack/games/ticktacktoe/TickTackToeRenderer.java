package com.chadgames.gamespack.games.ticktacktoe;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chadgames.gamespack.games.ActionsSequence;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.MoveData;

public class TickTackToeRenderer extends GameRenderer {

    private TickTackToeCell[][] cells =
        new TickTackToeCell[TickTackToeConstants.SIZE][TickTackToeConstants.SIZE];

    public TickTackToeRenderer(GameProcess gameProcess, Stage stage, SpriteBatch batch) {
        super(gameProcess, stage, batch);

        Table root = new Table();
        root.debugAll();
        root.setFillParent(true);
        root.center();
        stage.addActor(root);

        Table gameTable = new Table();
        gameTable.debugAll();
        gameTable.defaults().minSize(0).maxSize(1000);

        for (int i = 0; i < TickTackToeConstants.SIZE; i++) {
            for (int j = 0; j < TickTackToeConstants.SIZE; j++) {
                cells[i][j] = new TickTackToeCell(i, j);
                gameTable.add(cells[i][j]).grow();

                cells[i][j].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        TickTackToeCell cell = (TickTackToeCell) event.getListenerActor();
                        TickTackToeMoveData moveData = new TickTackToeMoveData(
                            cell.x, cell.y
                        );
                        moveData.playerId = gameProcess.getMyPlayerId();
                        gameProcess.makeMoveAndSendToServer(moveData);
                    }
                });
            }
            gameTable.row();
        }

        root.add(gameTable)
            .grow()
            .maxHeight(Value.percentWidth(1f, root))
            .maxWidth(Value.percentHeight(1f, root));
    }

    @Override
    public void render(float delta) {}

    @Override
    public void makeActions(ActionsSequence actionsSequence) {
        if (actionsSequence == null) return;
        TickTackToeAction action = (TickTackToeAction) actionsSequence;
        cells[action.x][action.y].setSymbol(action.symbol);
    }

    @Override
    public void loadFromState(GameState gameState) {
        TickTackToeState state = (TickTackToeState) gameState;
        for (int i = 0; i < TickTackToeConstants.SIZE; i++) {
            for (int j = 0; j < TickTackToeConstants.SIZE; j++) {
                cells[i][j].setSymbol(state.getSymbol(i, j));
            }
        }
    }
}
