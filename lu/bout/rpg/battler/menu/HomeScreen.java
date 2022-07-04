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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lu.bout.rpg.battler.RpgBattler;

public class HomeScreen implements Screen {

    final RpgBattler game;

    private Stage stage;

    Texture bg;

	public HomeScreen(final RpgBattler game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        // TODO scale this

        bg = new Texture("bg_01.png");
        Image image1 = new Image(bg);
        image1.setSize(RpgBattler.WIDTH,RpgBattler.HEIGHT);

        stage.addActor(image1);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Amble-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.borderWidth = 3;
        parameter.color = Color.BROWN;
        parameter.shadowOffsetX = 10;
        parameter.shadowOffsetY = 10;
        parameter.shadowColor = new Color(0xcfa772ff);
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;

        Label label = new Label("RPG Battle",labelStyle);
        label.setSize(Gdx.graphics.getWidth(),200);
        label.setPosition(0,Gdx.graphics.getHeight()-400);
        label.setAlignment(Align.center);
        stage.addActor(label);

        Skin mySkin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        Button button1 = new TextButton("Start Game",mySkin);
        button1.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        button1.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() / 2);
        button1.setColor(Color.GREEN);
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.launchDungeon();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(button1);

        String[] values = new String[]{"Simon Says", "Light's out", "Timing"};
        SelectBox<String> selectBox = new SelectBox<>(mySkin);
        selectBox.setAlignment(Align.center);
        selectBox.setName("Minigame");
        selectBox.setColor(Color.BROWN);
        selectBox.setItems(values);
        selectBox.setSelectedIndex(game.getPreferences().getInteger("minigame", 0));
        selectBox.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        selectBox.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() * 0.375f);
        selectBox.addListener(new ChangeListener(){
            private RpgBattler game;

            public EventListener setGame(RpgBattler game) {
                this.game = game;
                return this;
            }
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                game.getPreferences().putInteger("minigame", ((SelectBox<String>)actor).getSelectedIndex());
                game.getPreferences().flush();
            }
        }.setGame(game));

        Button button2 = new TextButton("Statistics",mySkin);
        button2.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        button2.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() / 4);
        button2.setColor(Color.BROWN);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new StatsScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(button2);


        stage.addActor(selectBox);
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
