package com.chadgames.gamespack.ui;

public class UIScale {

    public static final float BASE_WIDTH = 225;
    public static final float BASE_HEIGHT = 400;

    public static float percentWidth(float percent) {
        return BASE_WIDTH * percent;
    }

    public static float percentHeight(float percent) {
        return BASE_HEIGHT * percent;
    }

    public static float PADDING = 5f;
}
