package lu.bout.rpg.battler.menu.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;

public class SettingsTable extends Table {

    SelectBox minigameSelectBox;

    RpgGame game;

    public SettingsTable(RpgGame game) {
        this.game = game;
        init();
    }

    private void init() {
        add(new Label("Minigame", game.getSkin(), "small")).align(Align.left).padBottom(0);
        this.row();
        String[] values = new String[]{"Simon Says", "Light's out", "Timing", "Word Search"};
        minigameSelectBox = new SelectBox<>(game.getSkin());
        minigameSelectBox.setAlignment(Align.center);
        minigameSelectBox.setName("Minigame");
        minigameSelectBox.setItems(values);
        minigameSelectBox.setSelectedIndex(game.getPreferences().getInteger("minigame", 0));
        minigameSelectBox.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        minigameSelectBox.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() * 0.3f);
        minigameSelectBox.addListener(new ChangeListener(){
            public void changed (ChangeEvent event, Actor actor) {
                savePreferredMinigame();
            }
        });
        add(minigameSelectBox);
    }

    private void savePreferredMinigame() {
        game.getPreferences().putInteger("minigame", minigameSelectBox.getSelectedIndex());
        game.getPreferences().flush();
    }
}
