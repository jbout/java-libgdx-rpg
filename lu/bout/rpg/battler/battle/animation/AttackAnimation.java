package lu.bout.rpg.battler.battle.animation;

import com.badlogic.gdx.math.Vector2;

import lu.bout.rpg.battler.battle.map.CombatSprite;

public class AttackAnimation implements BattleAnimation {

    public static final float ATTACK_DURATION = 1f;

    float timeElapsed = 0;
    CombatSprite attacker;
    CombatSprite attackTarget;
    Vector2 deltaVector;
    Vector2 pos;
    int damage;

    public AttackAnimation(CombatSprite attacker, CombatSprite attackTarget, int damage) {
        this.attacker = attacker;
        this.attackTarget = attackTarget;
        this.damage = damage;
        // do not modify original vectors
        pos = new Vector2(attacker.getBaseCenter());
        deltaVector = new Vector2(attackTarget.getBaseCenter());
        // calculate the vector from target to destination center
        deltaVector.sub(attacker.getBaseCenter());
        // adjust for height, move sprite in front of attacked
        deltaVector.y -= Math.signum(deltaVector.y) * (attacker.getHeight() + attackTarget.getHeight()) / 2;
        // calculate movement distance per timestep (seconds)
        deltaVector.scl(1 / ATTACK_DURATION);
    }

    public boolean animate(float delta) {
        boolean done = false;

        pos.add(deltaVector.x * delta, deltaVector.y * delta);
        attacker.setCenter(pos);
        timeElapsed = timeElapsed + delta;
        if ( timeElapsed > ATTACK_DURATION / 2 && timeElapsed - delta < ATTACK_DURATION / 2) {
            attackTarget.updateHealth();
            attackTarget.drawDamaged(damage);
            deltaVector.scl(-1);
        }
        if (timeElapsed > ATTACK_DURATION) {
            done = true;
            attacker.setCenter(attacker.getBaseCenter());
        }
        return done;
    }
}
