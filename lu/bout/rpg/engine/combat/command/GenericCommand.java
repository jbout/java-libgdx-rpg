package lu.bout.rpg.engine.combat.command;

import lu.bout.rpg.engine.combat.action.CombatAction;
import lu.bout.rpg.engine.combat.participant.Participant;

public class GenericCommand extends CombatCommand implements ActionableCommand {

    CombatAction action;

    public GenericCommand(CombatAction action) {
        this.action = action;
    }
    public CombatAction getAction() {
        return action;
    }
}
