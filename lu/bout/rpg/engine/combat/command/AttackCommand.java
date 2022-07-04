package lu.bout.rpg.engine.combat.command;

import lu.bout.rpg.engine.combat.participant.Participant;

public class AttackCommand extends CombatCommand {
    private Participant target;

    public AttackCommand(Participant target) {
        this.target = target;
    }

    public Participant getTarget() {
        return target;
    }
}
