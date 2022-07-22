package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;

public class StatsScreen extends MenuScreen {

    private final Preferences stats;

    // Nicer colors: Color.BROWN with shadow 0xcfa772ff

	public StatsScreen(final RpgGame game) {
        super(game);
        stats = Gdx.app.getPreferences("stats");
    }

    @Override
    protected void init() {
        super.init();
        Table root = getRootTable();

        Label label = new Label("Stats", game.getSkin(), "title");
        label.setAlignment(Align.center);
        root.add(label);

        root.row();
        root.add(showMinigame(0, "Simon Says", game.getSkin()));
        root.row();
        root.add(showMinigame(1, "Lights", game.getSkin()));
        root.row();
        root.add(showMinigame(2, "Timing", game.getSkin()));

        root.row();
        Button button1 = new TextButton("Back",game.getSkin());
        button1.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                game.showMenu();
            }
        });

        root.row();
        root.add(button1);

    }

    public Table showMinigame(int minigameId, String minigameName, Skin labelStyle) {
        Table minigame = new Table();

        Label label2 = new Label(minigameName,labelStyle);
        label2.setAlignment(Align.center);
        minigame.add(label2).colspan(2).padTop(10);
        for (int i = 3; i <= 7; i++) {
            int success = stats.getInteger(minigameId +"-"+i+"-s", 0);
            int failure = stats.getInteger(minigameId +"-"+i+"-l", 0);
            if (success + failure > 0) {
                minigame.row();
                minigame.add(new Label("Difficulty " + i + " : ", labelStyle));
                minigame.add(new Label((int) (100f * success / (success + failure)) + "%", labelStyle)).align(Align.right);
            }
        }
        return minigame;
    }
}
