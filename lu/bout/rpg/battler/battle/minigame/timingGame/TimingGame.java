package lu.bout.rpg.battler.battle.minigame.timingGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import lu.bout.rpg.battler.battle.minigame.GameFeedback;
import lu.bout.rpg.battler.battle.minigame.MiniGame;
import lu.bout.rpg.engine.combat.command.CombatCommand;

public class TimingGame extends Rectangle implements MiniGame {

    Texture barBg;
    Texture barOk;
    Sprite slider;

    private GameFeedback callback;
    private CombatCommand commandToRun;
    float posX = 0;
    float okWidth;

    float speed;

    public TimingGame(GameFeedback callback, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.callback = callback;

        // load images
        barBg = new Texture("minigame/brownbar.png");
        barOk = new Texture("minigame/greenbar.png");
        slider = new Sprite(new Texture("minigame/slider.png"));
        slider.setY((height-slider.getHeight()) / 2);

    }

    public void init(int difficulty, CombatCommand command) {
        commandToRun = command;
        speed = difficulty;
        okWidth = 600 / difficulty;
    }


    public void render(SpriteBatch batch, float delta, Vector2 inputVector) {
        posX += delta * speed;
        batch.draw(barBg, (width-barBg.getWidth())/2, (height-barBg.getHeight())/2);
        batch.draw(barOk, (width-okWidth)/2, (height-barOk.getHeight())/2, okWidth, barOk.getHeight());
        slider.setCenterX((float) (width/2 + Math.cos(posX) * barBg.getWidth() / 2));
        slider.draw(batch);
        if (inputVector != null && this.contains(inputVector)) {
            if (Math.abs(Math.cos(posX) * barBg.getWidth()) <= okWidth ) {
                callback.minigameEnded(true, this, 0);
            } else {
                callback.minigameEnded(false, this, 0);
                speed = 0;
            }
        }
    }

    @Override
    public CombatCommand getCommandToRun() {
        return commandToRun;
    }

    public void dispose() {
        barBg.dispose();
        barOk.dispose();
        slider.getTexture().dispose();
    }
}
