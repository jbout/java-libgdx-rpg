package lu.bout.rpg.battler.battle.minigame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;

public class RuneButton extends Circle {

    static final int RENDER_WIDTH = 60;
    static final int RENDER_HEIGHT = 70;

    Texture buttonUp;
    Texture buttonDown;
    TextureAtlas runes;

    private boolean down = false;
    private String runeName;

    public RuneButton(Texture buttonUp, Texture buttonDown, TextureAtlas runes) {
        super();
        setRune(0);
        this.buttonUp = buttonUp;
        this.buttonDown = buttonDown;
        this.runes = runes;
    }

    public void setRune(int i) {
        runeName = "runes_70x90_bluegray_" + String.format("%05d", i);
    }

    public void setDown(boolean state) {
        down = state;
    }

    public void toogle() {
        down = ! down;
    }

    public boolean isDown() {
        return down;
    }

    public void render(SpriteBatch batch) {
        float ratio = 2*radius / RENDER_WIDTH;
        batch.draw((down ? buttonDown : buttonUp), x - radius, y - radius, 2*radius, ratio * RENDER_HEIGHT);
        batch.draw(runes.findRegion(runeName), x - (14*ratio), y - ((down ? 14 : 8)*ratio), 28*ratio , 36*ratio);
    }
}
