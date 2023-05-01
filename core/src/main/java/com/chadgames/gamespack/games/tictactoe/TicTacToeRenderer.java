package com.chadgames.gamespack.games.tictactoe;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chadgames.gamespack.games.Actions;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;

public class TicTacToeRenderer extends GameRenderer {

    private TicTacToeCell[][] cells =
        new TicTacToeCell[TicTacToeConstants.SIZE][TicTacToeConstants.SIZE];

    public TicTacToeRenderer(GameProcess gameProcess, Table rootTable) {
        super(gameProcess);

        Table gameTable = new Table();
        gameTable.debugAll();
        gameTable.defaults().minSize(0).maxSize(1000);

        for (int i = 0; i < TicTacToeConstants.SIZE; i++) {
            for (int j = 0; j < TicTacToeConstants.SIZE; j++) {
                cells[i][j] = new TicTacToeCell(i, j);
                gameTable.add(cells[i][j]).grow();

                cells[i][j].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        TicTacToeCell cell = (TicTacToeCell) event.getListenerActor();
                        cellClicked(cell);
                    }
                });
            }
            gameTable.row();
        }

        rootTable.add(gameTable)
            .grow()
            .maxHeight(Value.percentWidth(1f, rootTable))
            .maxWidth(Value.percentHeight(1f, rootTable));
    }

    void cellClicked(TicTacToeCell cell) {
        TicTacToeMoveData moveData = new TicTacToeMoveData(
            cell.x, cell.y
        );
        moveData.playerId = gameProcess.getMyPlayerId();
        gameProcess.makeMoveAndSendToServer(moveData);
    }

    @Override
    public void render(float delta) {}

    @Override
    public void makeActions(Actions actions) {
        if (actions == null) return;
        TicTacToeActions action = (TicTacToeActions) actions;
        cells[action.x][action.y].setSymbol(action.symbol);
    }

    @Override
    public void loadFromState(GameState gameState) {
        TicTacToeState state = (TicTacToeState) gameState;
        for (int i = 0; i < TicTacToeConstants.SIZE; i++) {
            for (int j = 0; j < TicTacToeConstants.SIZE; j++) {
                cells[i][j].setSymbol(state.getSymbol(i, j));
            }
        }
    }
}
