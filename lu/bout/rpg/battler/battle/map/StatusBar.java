package lu.bout.rpg.battler.battle.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashMap;

import lu.bout.rpg.engine.combat.status.CombatStatus;

public class StatusBar {

    public static final int ICON_WIDTH = 16;
    public static final int ICON_HEIGHT = 16;

    private TextureRegionDrawable[] statusIcons = new TextureRegionDrawable[0];

    public void draw(Batch batch, float x, float y) {
        int i = 0;
        for (TextureRegionDrawable t: statusIcons) {
            t.draw(batch, Math.round(x) + i * ICON_WIDTH, Math.round(y), ICON_WIDTH, ICON_HEIGHT);
            i++;
        }
    }

    public void setStatus(CombatStatus[] statuses) {
        statusIcons = new TextureRegionDrawable[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            statusIcons[i] = new TextureRegionDrawable(new TextureRegion(StatusArt.getBestSpriteTexture(statuses[i])));
        }
    }

    // TODO: do I need to dispose?
}
