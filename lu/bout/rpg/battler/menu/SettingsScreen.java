package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.menu.settings.SettingsTable;

public class SettingsScreen extends MenuScreen {

    // Nicer colors: Color.BROWN with shadow 0xcfa772ff

	public SettingsScreen(final RpgGame game) {
        super(game);
    }

    @Override
    protected void init() {
        super.init();
        Table root = getRootTable();
        addTitle("Settings");
        root.row().expandY();
        Table main = new Table();
        main.defaults().pad(10);
        root.add(main);

        main.row();
        main.add(new SettingsTable(game));

        main.row();

        Button button1 = new TextButton("Back",game.getSkin());
        button1.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                game.showMenu();
            }
        });

        main.row();
        main.add(button1);

    }

}
