package lu.bout.rpg.engine.system.simplejrpg.skill.combatSkill.attack;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.action.CombatAction;
import lu.bout.rpg.engine.combat.action.attack.HitAction;
import lu.bout.rpg.engine.combat.participant.Participant;
import lu.bout.rpg.engine.character.skill.AttackSkill;
import lu.bout.rpg.engine.character.skill.InvalidTarget;

public class WeaponHit extends AttackSkill {

    public float getChanceToHit() {
        return 1;
    }

    public float getDamage() {
        return 10;
    }

    @Override
    public CombatAction getAction(Participant actor, Participant[] targets) throws InvalidTarget {
        if (targets.length != 1) {
            throw new InvalidTarget();
        }
        return new HitAction(actor, targets[0]);
    }

    public void execute(Participant actor, Participant[] targets, Combat combat)  throws InvalidTarget {
        getAction(actor, targets).execute(combat);
    }

}
