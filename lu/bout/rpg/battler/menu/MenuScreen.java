package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lu.bout.rpg.battler.RpgGame;

public abstract class MenuScreen implements Screen {

    protected final RpgGame game;

    protected Stage stage;

    private Image bgImage;

	public MenuScreen(final RpgGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        bgImage = new Image(new Texture("bg_01.png"));
        bgImage.setPosition(
            (Gdx.graphics.getWidth() - bgImage.getWidth()) / 2,
            (Gdx.graphics.getHeight() - bgImage.getHeight()) / 2
        );
        stage.addActor(bgImage);
    }

    protected Label.LabelStyle getTitleStyle() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Amble-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.borderWidth = 3;
        parameter.color = Color.BROWN;
        parameter.shadowOffsetX = 10;
        parameter.shadowOffsetY = 10;
        parameter.shadowColor = new Color(0xcfa772ff);
        BitmapFont font100 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font100;
        return titleStyle;
    }

    protected Rectangle getUsableSurface() {
        return new Rectangle(bgImage.getX()+100, bgImage.getY()+100, bgImage.getWidth()-200, bgImage.getHeight()-200);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.776f, 0.643f, 0.466f, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
