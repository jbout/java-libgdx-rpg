package lu.bout.rpg.engine.combat.action;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.event.AttackEvent;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.participant.Participant;

public abstract class CombatAction {

    protected Participant actor;

    public CombatAction(Participant actor) {
        this.actor = actor;
    }
    abstract public void execute(Combat combat);
}
