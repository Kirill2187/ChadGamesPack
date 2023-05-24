package com.chadgames.gamespack.games.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class FieldCell<E extends Enum<E>> extends Actor {
    public int x;
    public int y;
    public E symbol = null;
    private Sprite sprite;

    public FieldCell(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public void setSymbol(E symbol) {
        this.symbol = symbol;
        sprite = getSprite(symbol);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (sprite != null) batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
    }

    public abstract Sprite getSprite(E symbol);

}
