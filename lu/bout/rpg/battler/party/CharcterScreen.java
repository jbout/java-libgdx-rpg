package lu.bout.rpg.battler.party;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lu.bout.rpg.battler.RpgGame;

public class CharcterScreen implements Screen {

    final RpgGame game;
    private Screen returnTo;
    private SamplePlayer character;

    private Stage stage;

    Texture bg;

	public CharcterScreen(final RpgGame game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());

        bg = new Texture("bg_01.png");
        Image image1 = new Image(bg);
        image1.setWidth(Gdx.graphics.getWidth());
        image1.setHeight(Gdx.graphics.getHeight());
        stage.addActor(image1);

        Button button1 = new TextButton("Return",game.getSkin());
        button1.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 20);
        button1.setPosition(Gdx.graphics.getWidth() - button1.getWidth() - 100,100);
        button1.setColor(Color.GREEN);
        button1.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                returnToPrevious();
            }
        });
        stage.addActor(button1);
    }

    public void showCharacter(final SamplePlayer character) {
        this.character = character;
        if (game.getScreen() != this) {
            returnTo = game.getScreen();
        }
        game.setScreen(this);
    }

    public void returnToPrevious() {
        game.setScreen(returnTo);
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
