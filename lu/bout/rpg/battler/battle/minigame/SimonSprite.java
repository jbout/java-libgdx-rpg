package lu.bout.rpg.battler.battle.minigame;

import static lu.bout.rpg.battler.battle.minigame.SimonButton.RENDER_WIDTH;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;

public class SimonSprite extends Circle {

    TextureAtlas spriteAtlas;
    int currentFrame;
    float alpha = 1;

    public SimonSprite() {
        super();
        spriteAtlas = new TextureAtlas("pipo-mapeffect013.atlas");
        currentFrame = 0;
        this.radius = spriteAtlas.findRegion("pipo-mapeffect013_00000").originalHeight / 2;
    }

    public void render(SpriteBatch batch) {
        currentFrame = currentFrame >= 19 ? 0 : currentFrame + 1;
        // TODO can be done much cleaner with a sprite
        batch.setColor(1, 1, 1, alpha);
        batch.draw(spriteAtlas.findRegion("pipo-mapeffect013_" + String.format("%05d", currentFrame)), x - 80, y - 60, 160, 120);
        batch.setColor(1, 1, 1, 1);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void dispose() {
        spriteAtlas.dispose();
    }

}
