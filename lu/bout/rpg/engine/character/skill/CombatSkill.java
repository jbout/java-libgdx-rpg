package lu.bout.rpg.engine.character.skill;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.action.CombatAction;
import lu.bout.rpg.engine.combat.participant.Participant;
import lu.bout.rpg.engine.character.Skill;

public abstract class CombatSkill extends Skill {

    public abstract CombatAction getAction(Participant actor, Participant[] targets) throws InvalidTarget;

    public void execute(Participant actor, Participant[] targets, Combat combat)  throws InvalidTarget {
        getAction(actor, targets).execute(combat);
    }
}
