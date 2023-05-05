package com.chadgames.gamespack.games.reversi;

import static com.chadgames.gamespack.games.reversi.Symbol.*;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.common.FieldCell;

public class ReversiCell extends FieldCell<Symbol> {
    public ReversiCell(int x, int y) {
        super(x, y);
    }

    @Override
    public Sprite getSprite(Symbol symbol) {
        if (symbol == White) {
            return GameManager.getInstance().assetManager.getSprite(GameType.Reversi, "White");
        } else if (symbol == Black) {
            return GameManager.getInstance().assetManager.getSprite(GameType.Reversi, "Black");
        }
        return null;
    }
}