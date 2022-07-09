package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lu.bout.rpg.battler.RpgGame;

public abstract class MenuScreen implements Screen {

    protected final RpgGame game;

    protected Stage stage;

    private Texture bg;

	public MenuScreen(final RpgGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        bg = new Texture("bg_01.png");
        Image image1 = new Image(bg);
        image1.setPosition(
            (Gdx.graphics.getWidth() - image1.getWidth()) / 2,
            (Gdx.graphics.getHeight() - image1.getHeight()) / 2
        );
        stage.addActor(image1);
    }

    public Label.LabelStyle getTitleStyle() {
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
