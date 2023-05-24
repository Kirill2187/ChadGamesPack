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
import com.chadgames.gamespack.games.common.Field;

public class TicTacToeRenderer extends GameRenderer {

    private final Field<TicTacToeCell> field;

    public TicTacToeRenderer(GameProcess gameProcess, Table rootTable) {
        super(gameProcess);

        field = new Field<>(TicTacToeCell.class, rootTable, this::cellClicked,
            TicTacToeConstants.SIZE, TicTacToeConstants.SIZE);
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
        field.getCell(action.x, action.y).setSymbol(action.symbol);
    }

    @Override
    public void loadFromState(GameState gameState) {
        TicTacToeState state = (TicTacToeState) gameState;
        for (int x = 0; x < field.WIDTH; x++) {
            for (int y = 0; y < field.HEIGHT; y++) {
                field.getCell(x, y).setSymbol(state.getSymbol(x, y));
            }
        }
    }
}
