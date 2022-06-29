package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.math.Vector2;

public class AttackAnimation implements BattleAnimation {

    public static final float ATTACK_DURATION = 1f;

    float timeElapsed = 0;
    CombatSprite attacker;
    CombatSprite attackTarget;
    Vector2 targetVector;

    public AttackAnimation(CombatSprite attacker, CombatSprite attackTarget) {
        this.attacker = attacker;
        this.attackTarget = attackTarget;
        targetVector = new Vector2(
                attackTarget.baseX + (attackTarget.getWidth() / 2) - (attacker.getWidth() / 2),
                attackTarget.baseY + (attackTarget.getHeight() / 2) - (attacker.getHeight() / 2)
        );
    }

    public boolean animate(float delta) {
        boolean done = false;
        float percent = timeElapsed / ATTACK_DURATION;
        if (percent > 0.5f) {
            percent = 1 - percent;
        }
        attacker.setX((attacker.baseX * (1-percent)) + (targetVector.x * percent));
        attacker.setY((attacker.baseY * (1-(percent*0.5f))) + (targetVector.y * (percent*0.5f)));
        timeElapsed = timeElapsed + delta;
        if (percent < 0.5f && timeElapsed > ATTACK_DURATION / 2) {
            attackTarget.drawShake(0.25f);
        }
        if (timeElapsed > ATTACK_DURATION) {
            done = true;
            attacker.setX(attacker.baseX);
            attacker.setY(attacker.baseY);
        }
        return done;
    }
}
