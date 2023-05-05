package com.chadgames.gamespack.ui;

public class UIScale {

    public static final float BASE_WIDTH = 550;
    public static final float BASE_HEIGHT = 800;

    public static float percentWidth(float percent) {
        return BASE_WIDTH * percent;
    }

    public static float percentHeight(float percent) {
        return BASE_HEIGHT * percent;
    }

    public static float PADDING = 10f;
    public static float WINDOW_WIDTH = percentWidth(.6f);
    public static float WINDOW_HEIGHT = WINDOW_WIDTH;
}
