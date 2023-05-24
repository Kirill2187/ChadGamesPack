package com.chadgames.gamespack.games.common;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chadgames.gamespack.GameManager;

public class Field<C extends FieldCell<?>> {

    public interface CellClickListener<C extends FieldCell<?>> {
        void onCellClick(C cell);
    }

    public final int WIDTH;
    public final int HEIGHT;
    private final C[][] cells;
    private Class<C> cellClass;

    public Field(Class<C> cellClass, Table rootTable, CellClickListener<C> listener, int width, int height) {
        this.cellClass = cellClass;
        WIDTH = width;
        HEIGHT = height;

        @SuppressWarnings("unchecked")
        final C[][] cells = (C[][]) new FieldCell[WIDTH][HEIGHT];
        this.cells = cells;

        Table gameTable = new Table();
        if (GameManager.DEBUG) gameTable.debugAll();
        gameTable.defaults().minSize(0).maxSize(1000);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                try {
                    cells[x][y] = cellClass.getConstructor(int.class, int.class).newInstance(x, y);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                gameTable.add(cells[x][y]).grow();
                cells[x][y].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        @SuppressWarnings("unchecked")
                        C cell = (C) event.getListenerActor();
                        listener.onCellClick(cell);
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

    public C getCell(int x, int y) {
        return cells[x][y];
    }
}
