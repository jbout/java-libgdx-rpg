package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.math.MathUtils;

import lu.bout.rpg.battler.battle.BattleAnimation;
import lu.bout.rpg.battler.battle.CombatSprite;

public class DeathAnimation implements BattleAnimation {

    public static final float DEATH_DURATION = 1f;

    float timeElapsed = 0;
    CombatSprite subject;

    /**
     * Trick to keep the sprite drawn until the attack animation finished
     * @param subject the dying sprite
     */
    public DeathAnimation(CombatSprite subject) {
        this.subject = subject;
    }

    public boolean animate(float delta) {
        timeElapsed += delta;
        return timeElapsed > DEATH_DURATION;
    }
}
