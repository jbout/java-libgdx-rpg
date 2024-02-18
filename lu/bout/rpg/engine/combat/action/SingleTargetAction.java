package lu.bout.rpg.engine.combat.action;

import lu.bout.rpg.engine.combat.participant.Participant;

public abstract class SingleTargetAction extends CombatAction{

    protected Participant target;

    public SingleTargetAction(Participant actor, Participant target) {
        super(actor);
        this.target = target;
    }
}
