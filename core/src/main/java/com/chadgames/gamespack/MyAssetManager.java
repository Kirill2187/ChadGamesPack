package com.chadgames.gamespack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.utils.Constants;

import java.util.HashMap;

public class MyAssetManager {

    private final HashMap<GameType, TextureAtlas> atlases = new HashMap<>();
    private TextureAtlas icons;
    private final Skin skin;
    public Skin getSkin() {
        return skin;
    }

    public MyAssetManager() {
        Gdx.app.log("MyAssetManager", "Loading assets...");
        loadIcons();
        skin = new Skin(Gdx.files.internal("ui/skin/flat-earth-ui.json")); // TODO: replace with custom skin
    }

    public void loadIcons() {
        icons = new TextureAtlas("games/icons.atlas");
    }

    public void load(GameType gameType) {
        if (!atlases.containsKey(gameType)) {
            atlases.put(gameType, new TextureAtlas("games/" + gameType.name().toLowerCase() + "/atlas.atlas"));
        }
    }

    public TextureAtlas getAtlas(GameType gameType) {
        if (!atlases.containsKey(gameType)) load(gameType);
        return atlases.get(gameType);
    }

    public void dispose() {
        for (TextureAtlas atlas : atlases.values()) {
            atlas.dispose();
        }
    }

    public Sprite getSprite(GameType gameType, String name) {
        return getAtlas(gameType).createSprite(name);
    }

    public Sprite getIcon(String name) {
        return icons.createSprite(name);
    }

}
