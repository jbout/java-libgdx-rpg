package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lu.bout.rpg.battler.RpgGame;

public class StatsScreen implements Screen {

    final RpgGame game;
    private final Preferences stats;

    private Stage stage;

    Texture bg;

	public StatsScreen(final RpgGame game) {
        stats = Gdx.app.getPreferences("stats");
        this.game = game;
        stage = new Stage(new ScreenViewport());

        bg = new Texture("bg_01.png");
        Image image1 = new Image(bg);
        image1.setWidth(Gdx.graphics.getWidth());
        image1.setHeight(Gdx.graphics.getHeight());

        stage.addActor(image1);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Amble-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.borderWidth = 3;
        parameter.color = Color.BROWN;
        parameter.shadowOffsetX = 10;
        parameter.shadowOffsetY = 10;
        parameter.shadowColor = new Color(0xcfa772ff);
        BitmapFont font100 = generator.generateFont(parameter); // font size 24 pixels
        parameter.borderWidth = 0;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;
        parameter.size = 24;
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font100;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;


        Label label = new Label("Stats",titleStyle);
        label.setSize(Gdx.graphics.getWidth(),200);
        label.setPosition(0,Gdx.graphics.getHeight()-300);
        label.setAlignment(Align.center);
        stage.addActor(label);

        Skin mySkin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        Button button1 = new TextButton("Back",mySkin);
        button1.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        button1.setPosition(Gdx.graphics.getWidth() / 4,100);
        button1.setColor(Color.BROWN);
        button1.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                game.showMenu();
            }
        });

        stage.addActor(button1);

        showMinigame(0, "Simon Says", labelStyle, 1000);
        showMinigame(1, "Lights", labelStyle, 700);
        showMinigame(2, "Timing", labelStyle, 450);
    }

    public void showMinigame(int minigame, String minigameName, Label.LabelStyle labelStyle, int ypos) {
        Label label2 = new Label(minigameName,labelStyle);
        label2.setSize(Gdx.graphics.getWidth(),50);
        label2.setPosition(0,ypos);
        label2.setAlignment(Align.center);
        stage.addActor(label2);
        int offset = ypos;
        for (int i = 3; i <= 7; i++) {
            int success = stats.getInteger(minigame+"-"+i+"-s", 0);
            int failure = stats.getInteger(minigame+"-"+i+"-l", 0);
            if (success + failure > 0) {
                offset -= 40;
                Label label = new Label("Difficulty " + i + " : " + (int) (100f * success / (success + failure)) + "%", labelStyle);
                label.setSize(Gdx.graphics.getWidth() / 2, 40);
                label.setPosition(150, offset);
                stage.addActor(label);
            }
        }
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
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
