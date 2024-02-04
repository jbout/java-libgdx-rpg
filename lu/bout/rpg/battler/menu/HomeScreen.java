package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.map.MapScreen;
import lu.bout.rpg.battler.saves.GameState;
import lu.bout.rpg.battler.saves.SaveMetadata;

public class HomeScreen extends MenuScreen {

    TextButton continueButton;

    public HomeScreen(final RpgGame game) {
        super(game);
    }

    @Override
    protected void init() {
        super.init();
        Table root = getRootTable();
        addTitle("RPG Battle");

        root.row().expandY().fillY();
        Table buttons = new Table();
        buttons.defaults().expand().fillX();
        root.add(buttons).width(Gdx.graphics.getWidth() / 2).padBottom(25);


        buttons.row();
        continueButton = new TextButton("No game found",game.getSkin());
        continueButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                continueGame();
            }
        });
        buttons.add(continueButton);

        buttons.row();

        Button newgameButton = new TextButton("New Game",game.getSkin());
        newgameButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new NewGameScreen(game));
            }
        });
        buttons.add(newgameButton);

        buttons.row();
        Button showMap = new TextButton("Try Map",game.getSkin());
        showMap.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                MapScreen map = new MapScreen(game);
                game.getAssetService().preload(map);
                map.init();
                game.setScreen(map);
            }
        });
        buttons.add(showMap);

        buttons.row();
        Button settingsButton = new TextButton("Settings",game.getSkin());
        settingsButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new SettingsScreen(game));
            }
        });
        buttons.add(settingsButton);

        buttons.row();

        Button button2 = new TextButton("Statistics",game.getSkin());
        //button2.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        //button2.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() * 0.2f);
        button2.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new StatsScreen(game));
            }
        });
        buttons.add(button2);
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
            continueButton.setColor(Color.WHITE);
            continueButton.setText("Resume");
            continueButton.setDisabled(false);
        }
    }
}
