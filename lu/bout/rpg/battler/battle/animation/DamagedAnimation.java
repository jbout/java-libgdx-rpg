package lu.bout.rpg.battler.battle.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import lu.bout.rpg.battler.battle.map.CombatSprite;

public class DamagedAnimation implements BattleAnimation {

    public static final float DURATION = 0.5f;

    float timeElapsed = 0;
    CombatSprite subject;

    Label label;
    float riseSpeed;

    public DamagedAnimation(CombatSprite subject, int damage, Skin skin) {
        this.subject = subject;
        label = new Label("" + damage, skin, "blood");
        label.setColor(Color.RED);
        label.setPosition(subject.getX() + (subject.getWidth() - label.getWidth()) / 2 , subject.getY());
        riseSpeed = 100 / DURATION;
    }

    public boolean animate(float delta) {
        boolean done = false;
        subject.setCenter(subject.getBaseCenter().add(new Vector2(
                        (MathUtils.random() - 0.5f) * 10,
                        (MathUtils.random() - 0.5f) * 10
        )));
        timeElapsed += delta;
        label.setY(label.getY() + (delta * riseSpeed));
        if (timeElapsed > DURATION) {
            done = true;
            subject.setCenter(subject.getBaseCenter());
        }
        return done;
    }

    public void draw(Batch batch) {
        label.draw(batch, 1 - (timeElapsed / DURATION));
    }
}
