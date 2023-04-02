package com.chadgames.gamespack.games.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.chadgames.gamespack.GameManager;
import com.chadgames.gamespack.games.GameType;

public class TicTacToeCell extends Actor {
    public int x;
    public int y;
    public Symbol symbol;

    Sprite sprite;

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
        if (symbol == Symbol.X) {
            sprite = GameManager.getInstance().assetManager.getSprite(GameType.TicTacToe, "x");
        } else if (symbol == Symbol.O) {
            sprite = GameManager.getInstance().assetManager.getSprite(GameType.TicTacToe, "o");
        }
        sprite.setSize(getWidth(), getHeight());
    }

    public TicTacToeCell(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.symbol = Symbol.EMPTY;

        sprite = new Sprite();
        sprite.setPosition(getX(), getY());
        sprite.setSize(getWidth(), getHeight());
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (symbol != Symbol.EMPTY) {
            sprite.draw(batch);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 pos = localToStageCoordinates(new Vector2(0, 0));
        sprite.setPosition(pos.x, pos.y);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        sprite.setSize(getWidth(), getHeight());
    }
}
