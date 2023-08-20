package lu.bout.rpg.battler.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class PrecachedFont {
    private FreeTypeFontGenerator generator;
    private BitmapFont cache;

    public PrecachedFont() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/KosugiMaru-Regular.ttf"));
    }

    public void preCache(String $characters) {
        Gdx.app.log("Game", "Generating " + $characters.length() + " characters");
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 75;
        parameter.characters = $characters;
        cache = generator.generateFont(parameter);
    }

    public BitmapFont getCachedFont() {
        return cache;
    }

    public Sprite getSpriteFor(char c) {
        BitmapFont.Glyph glyph = cache.getData().getGlyph(c);
        TextureRegion tr = cache.getRegion(glyph.page);
        return new Sprite(tr, glyph.srcX, glyph.srcY, glyph.width, glyph.height);
    }
}
