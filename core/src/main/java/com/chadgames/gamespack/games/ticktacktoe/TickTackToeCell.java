package com.chadgames.gamespack.games.ticktacktoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TickTackToeCell extends Actor {
    public int x;
    public int y;
    public Symbol symbol;

    Texture X;
    Texture O;
    public Sprite sprite;

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
        if (symbol == Symbol.X) {
            sprite.setTexture(X);
        } else if (symbol == Symbol.O) {
            sprite.setTexture(O);
        }
    }

    public TickTackToeCell(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.symbol = Symbol.EMPTY;

        // TODO: get textures from somewhere else, do not initialize them here
        X = new Texture(Gdx.files.internal("games/tictactoe/x.png"));
        O = new Texture(Gdx.files.internal("games/tictactoe/o.png"));
        sprite = new Sprite(X);

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
