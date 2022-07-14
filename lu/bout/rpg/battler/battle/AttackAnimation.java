package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AttackAnimation implements BattleAnimation {

    public static final float ATTACK_DURATION = 1f;

    float timeElapsed = 0;
    CombatSprite attacker;
    CombatSprite attackTarget;
    Vector2 targetVector;
    int damage;

    public AttackAnimation(CombatSprite attacker, CombatSprite attackTarget, int damage) {
        this.attacker = attacker;
        this.attackTarget = attackTarget;
        this.damage = damage;
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
        if ( timeElapsed > ATTACK_DURATION / 2 && timeElapsed - delta < ATTACK_DURATION / 2) {
            attackTarget.updateHealth();
            attackTarget.drawDamaged(damage);
        }
        if (timeElapsed > ATTACK_DURATION) {
            done = true;
            attacker.setX(attacker.baseX);
            attacker.setY(attacker.baseY);
        }
        return done;
    }
}
