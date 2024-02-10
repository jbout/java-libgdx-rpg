package lu.bout.rpg.battler.battle.minigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import lu.bout.rpg.battler.shared.SubScreen;
import lu.bout.rpg.engine.combat.command.CombatCommand;

public class MiniGameActor extends Actor {

    private Vector3 touchPosRaw;
    private Vector2 touchPos;

    private Texture bg;
    private SubScreen wrappedGame;
    private float elapsedTime = 0;

    public MiniGameActor(SubScreen miniGame, Texture bg) {
        wrappedGame = miniGame;
        touchPosRaw = new Vector3();
        touchPos = new Vector2();
        this.bg = bg;
    }

    public void act (float delta) {
        super.act(delta);
        elapsedTime += delta;
    }

    public void draw (Batch batch, float parentAlpha) {
        batch.draw(bg, getX(), getY(), getWidth(), getHeight());

        // TODO remove workaround
        boolean isTouched = Gdx.input.isTouched();
        if (isTouched) {
            touchPosRaw.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            getStage().getViewport().unproject(touchPosRaw);
            touchPos.set(touchPosRaw.x, touchPosRaw.y);
        }
        wrappedGame.render((SpriteBatch) batch, elapsedTime, isTouched ? touchPos : null);
        elapsedTime = 0;
    }
}
