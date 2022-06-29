package lu.bout.rpg.battler.battle.minigame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

import lu.bout.rpg.battler.battle.BattleScreen;

public class SimonButton extends Circle {

    static final int RENDER_WIDTH = 60;
    static final int RENDER_HEIGHT = 70;

    private boolean down = false;
    private String runeName;

    public SimonButton() {
        super();
        setRune(0);
    }

    public void setRune(int i) {
        runeName = "runes_70x90_bluegray_" + String.format("%05d", i);
    }

    public void setDown(boolean state) {
        down = state;
    }

    public boolean isDown() {
        return down;
    }

    public void render(SimonSays simonSays, SpriteBatch batch) {
        batch.draw((down ? simonSays.buttonDown : simonSays.buttonUp), x - radius, y - radius, RENDER_WIDTH, RENDER_HEIGHT);
        batch.draw(simonSays.runes.findRegion(runeName), x - 14, y - (down ? 14 : 8), 28 , 36);
    }
}
