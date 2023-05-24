package com.chadgames.gamespack.games.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.common.FieldCell;

public class TicTacToeCell extends FieldCell<Symbol> {

    public TicTacToeCell(int x, int y) {
        super(x, y);
    }

    @Override
    public Sprite getSprite(Symbol symbol) {
        if (symbol == Symbol.X) {
            return GameManager.getInstance().assetManager.getSprite(GameType.TicTacToe, "x");
        } else if (symbol == Symbol.O) {
            return GameManager.getInstance().assetManager.getSprite(GameType.TicTacToe, "o");
        }
        return null;
    }
}
