package lu.bout.rpg.battler.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.party.PlayerCharacter;

public class PlayerPortrait extends Table {
    PlayerCharacter player;
    Image portrait;
    ProgressBar healthBar;

    public PlayerPortrait(PlayerCharacter player, Skin skin) {
        super();
        this.player = player;
        PortraitService p = new PortraitService();
        Texture t = p.getPortrait(player.getPortaitId());
        portrait = new Image(buildRoundPortrait(player.getPortaitId(), 99));
        t.dispose();
        add(portrait);
        Table subTable = new Table();
        Label playerName = new Label(player.getName(), skin, "white");
        playerName.setColor(Color.WHITE);
        subTable.add(playerName).left();

        healthBar = new ProgressBar(0, player.getMaxhp(), 1, false, skin, "healthbar");
        healthBar.setAnimateDuration(0.0f);
        subTable.row();
        subTable.add(healthBar);
        add(subTable).spaceLeft(10);

        updateHp();
    }

    private Texture buildRoundPortrait(int id, int size) {
        PortraitService p = new PortraitService();
        Pixmap source = p.getScaled(player.getPortaitId(), size, size);
        Pixmap result = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        result.setBlending(Pixmap.Blending.None);
        result.setColor(1f, 1f, 1f, 1f);
        result.fillCircle(size / 2, size / 2, size/2-1);

        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                result.drawPixel(x, y, source.getPixel(x, y) & result.getPixel(x, y));
            }
        }
        result.setColor(0f, 0f, 0f, 1f);
        result.drawCircle(size/2, size/2, size/2-1);

        return new Texture(result);
    }

    public void updateHp() {
        healthBar.setValue(player.getHp());
    }
}
