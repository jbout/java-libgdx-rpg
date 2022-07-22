package lu.bout.rpg.battler.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import org.w3c.dom.Text;

import lu.bout.rpg.battler.RpgGame;

public class SkinFactory implements AssetConsumer {

    private static final AssetDescriptor FILE_SKIN = new AssetDescriptor("skin/flat-earth-ui.json", Skin.class);
    private static final AssetDescriptor FILE_FONT_DEFAULT = new AssetDescriptor("font/Amble-Light.ttf", FreeTypeFontGenerator.class);
    private static final AssetDescriptor FILE_FONT_BLOOD = new AssetDescriptor("font/bloody.ttf", FreeTypeFontGenerator.class);
    private static final AssetDescriptor FILE_WINDOW_BG = new AssetDescriptor("bg2.9.png", Texture.class);

    private RpgGame  game;

    public SkinFactory(RpgGame rpgGame) {
        game = rpgGame;
    }

    public AssetDescriptor[] getRequiredFiles() {
        return new AssetDescriptor[]{
                FILE_SKIN
                ,FILE_FONT_DEFAULT
                ,FILE_FONT_BLOOD
                ,FILE_WINDOW_BG
        };
    }

    public Skin generateSkin() {
        Skin skin = (Skin) game.getAssetService().get(FILE_SKIN);

        FreeTypeFontGenerator generator = (FreeTypeFontGenerator) game.getAssetService().get(FILE_FONT_DEFAULT);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.borderWidth = 0;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;
        parameter.size = 18;
        BitmapFont font18 = generator.generateFont(parameter);
        parameter.size = 36;
        BitmapFont font36 = generator.generateFont(parameter);
        parameter.color = Color.WHITE;
        BitmapFont font36white = generator.generateFont(parameter);

        parameter.size = 100;
        parameter.borderWidth = 3;
        parameter.color = Color.BROWN;
        parameter.shadowOffsetX = 10;
        parameter.shadowOffsetY = 10;
        parameter.shadowColor = new Color(0x8b451333); // 0xcfa772FF
        BitmapFont font100 = generator.generateFont(parameter);
        generator.dispose();

        generator = (FreeTypeFontGenerator) game.getAssetService().get(FILE_FONT_BLOOD);
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.borderWidth = 0;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;
        parameter.size = 36;
        parameter.characters = "0123456789 .KMGT+";
        BitmapFont bloodyFont = generator.generateFont(parameter);

        Label.LabelStyle labelStyle18 = new Label.LabelStyle();
        labelStyle18.font = font18;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font36;

        Label.LabelStyle whiteLabelStyle = new Label.LabelStyle();
        whiteLabelStyle.font = font36white;

        Label.LabelStyle bloodyLabelStyle = new Label.LabelStyle();
        bloodyLabelStyle.font = bloodyFont;

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font100;

        skin.add("small", labelStyle18);
        skin.add("default", labelStyle);
        skin.add("white", whiteLabelStyle);
        skin.add("blood", bloodyLabelStyle);
        skin.add("title", titleStyle);
        skin.get(TextButton.TextButtonStyle.class).font = font36;
        skin.get(SelectBox.SelectBoxStyle.class).font = font36;
        skin.get(SelectBox.SelectBoxStyle.class).listStyle.font = font36;
        skin.get(TextField.TextFieldStyle.class).font = font36;

        skin.add("health", generateProgressBarStyle(Color.RED, Color.BLACK));
        skin.add("xp", generateProgressBarStyle(Color.GREEN, Color.BLACK));

        Texture windowBg = (Texture) game.getAssetService().get(FILE_WINDOW_BG);
        skin.add("dialog", new WindowStyle(font36, Color.WHITE, new TextureRegionDrawable(windowBg)));

        return skin;
    }


    private ProgressBar.ProgressBarStyle generateProgressBarStyle(Color before, Color after) {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();

        style.background = getColoredDrawable(10, 10, after);
        style.knob = getColoredDrawable(0, 10, before);
        style.knobBefore = getColoredDrawable(10, 10, before);

        return style;
    }

    private Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return drawable;
    }
}
