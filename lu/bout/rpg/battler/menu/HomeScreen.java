package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.saves.GameState;
import lu.bout.rpg.battler.saves.SaveMetadata;

public class HomeScreen extends MenuScreen {

    TextButton continueButton;
    SelectBox<String> selectBox;

    public HomeScreen(final RpgGame game) {
        super(game);
    }

    @Override
    protected void init() {
        super.init();
        Label label = new Label("RPG Battle", game.getSkin(), "title");
        label.setSize(Gdx.graphics.getWidth(),200);
        label.setPosition(0,Gdx.graphics.getHeight()-400);
        label.setAlignment(Align.center);
        stage.addActor(label);

        continueButton = new TextButton("No game found",game.getSkin());
        continueButton.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        continueButton.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() * 0.5f);
        continueButton.setColor(Color.GREEN);
        continueButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                continueGame();
            }
        });
        stage.addActor(continueButton);

        Button button1 = new TextButton("New Game",game.getSkin());
        button1.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        button1.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() * 0.4f);
        button1.setColor(Color.GREEN);
        button1.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new NewGameScreen(game));
            }
        });
        stage.addActor(button1);

        String[] values = new String[]{"Simon Says", "Light's out", "Timing"};
        selectBox = new SelectBox<>(game.getSkin());
        selectBox.setAlignment(Align.center);
        selectBox.setName("Minigame");
        selectBox.setColor(Color.GREEN);
        selectBox.setItems(values);
        selectBox.setSelectedIndex(game.getPreferences().getInteger("minigame", 0));
        selectBox.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        selectBox.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() * 0.3f);
        selectBox.addListener(new ChangeListener(){
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                savePreferredMinigame();
            }
        });
        stage.addActor(selectBox);

        Button button2 = new TextButton("Statistics",game.getSkin());
        button2.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        button2.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() * 0.2f);
        button2.setColor(Color.GREEN);
        button2.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new StatsScreen(game));
            }
        });
        stage.addActor(button2);
    }

    private void savePreferredMinigame() {
        game.getPreferences().putInteger("minigame", selectBox.getSelectedIndex());
        game.getPreferences().flush();
    }

    private void continueGame() {
        SaveMetadata latest = game.getSaveService().getLatest();
        GameState state = game.getSaveService().restore(latest.getId());
        game.launchGame(state);
    }


    @Override
    public void show() {
        super.show();
        SaveMetadata latest = game.getSaveService().getLatest();
        if (latest == null) {
            continueButton.setColor(Color.GRAY);
            continueButton.setDisabled(true);
        } else {
            continueButton.setColor(Color.GREEN);
            continueButton.setText("Continue");
            continueButton.setDisabled(false);
        }
    }
}
