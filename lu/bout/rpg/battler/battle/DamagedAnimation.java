package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
        subject.setX(subject.baseX + (MathUtils.random() - 0.5f) * 10);
        subject.setY(subject.baseY + (MathUtils.random() - 0.5f) * 10);
        timeElapsed += delta;
        label.setY(label.getY() + (delta * riseSpeed));
        if (timeElapsed > DURATION) {
            done = true;
            subject.setX(subject.baseX);
            subject.setY(subject.baseY);
        }
        return done;
    }

    public void draw(Batch batch) {
        label.draw(batch, 1 - (timeElapsed / DURATION));
    }
}
