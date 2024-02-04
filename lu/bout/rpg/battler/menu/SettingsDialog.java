package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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

import java.util.Set;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.menu.settings.SettingsTable;

public class SettingsDialog extends Dialog {

    public static final AssetDescriptor FILE_SETTINGS_ICON = new AssetDescriptor("settings.png", Texture.class);

    protected final RpgGame game;

	public SettingsDialog(final RpgGame game) {
        // title does not show properly
        super("", game.getSkin(), "dialog");
        this.game = game;
        Table main = getContentTable();
        main.defaults().pad(10);

        main.row();
        main.add(new Label("Settings", game.getSkin(), "dark-wood"));

        main.row();
        main.add(new SettingsTable(game));
        main.row();

        Button backMain = new TextButton("Back to menu",game.getSkin());
        final Dialog current = this;
        backMain.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                // hide without animation
                current.hide(null);
                game.showMenu();
            }
        });
        main.row();
        main.add(backMain);

        this.button("Close");
    }
}
