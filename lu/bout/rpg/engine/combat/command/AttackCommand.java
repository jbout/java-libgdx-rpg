package lu.bout.rpg.engine.combat.command;

import lu.bout.rpg.engine.combat.action.CombatAction;
import lu.bout.rpg.engine.combat.action.attack.WeakeningStrikeAction;
import lu.bout.rpg.engine.combat.participant.Participant;

public class AttackCommand extends CombatCommand {
    private Participant target;

    public AttackCommand(Participant target) {
        this.target = target;
    }

    public CombatAction getAction(Participant actor) {
        return new WeakeningStrikeAction(actor, target);
    }
}
