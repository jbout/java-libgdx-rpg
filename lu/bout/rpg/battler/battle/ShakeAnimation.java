package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.math.MathUtils;

public class ShakeAnimation implements BattleAnimation {

    float duration;
    float timeElapsed = 0;
    CombatSprite subject;

    public ShakeAnimation(CombatSprite subject, float duration) {
        this.subject = subject;
        this.duration = duration;
    }

    public boolean animate(float delta) {
        boolean done = false;
        subject.setX(subject.baseX + (MathUtils.random() - 0.5f) * 10);
        subject.setY(subject.baseY + (MathUtils.random() - 0.5f) * 10);
        timeElapsed += delta;
        if (timeElapsed > duration) {
            done = true;
            subject.setX(subject.baseX);
            subject.setY(subject.baseY);
        }
        return done;
    }
}
