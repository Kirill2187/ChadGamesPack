package com.chadgames.gamespack.games.reversi;

import static com.chadgames.gamespack.games.reversi.ReversiConstants.SIZE;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.chadgames.gamespack.games.Actions;
import com.chadgames.gamespack.games.GameProcess;
import com.chadgames.gamespack.games.GameRenderer;
import com.chadgames.gamespack.games.GameState;
import com.chadgames.gamespack.games.common.Field;

public class ReversiRenderer extends GameRenderer {

    private final Field<ReversiCell> field;

    public ReversiRenderer(GameProcess gameProcess, Table rootTable) {
        super(gameProcess);
        field = new Field<>(ReversiCell.class, rootTable, this::cellClicked, SIZE, SIZE);

    }

    void cellClicked(ReversiCell cell) {
        ReversiMoveData moveData = new ReversiMoveData(
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
        ReversiActions action = (ReversiActions) actions;
        for (ReversiCoords coords: action.list) {
            field.getCell(coords.x, coords.y).setSymbol(action.symbol);
        }
    }

    @Override
    public void loadFromState(GameState gameState) {
        ReversiState state = (ReversiState) gameState;
        for (int x = 0; x < field.WIDTH; x++) {
            for (int y = 0; y < field.HEIGHT; y++) {
                field.getCell(x, y).setSymbol(state.getSymbol(x, y));
            }
        }
    }
}
