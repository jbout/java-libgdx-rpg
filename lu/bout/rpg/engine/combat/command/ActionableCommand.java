package lu.bout.rpg.engine.combat.command;

import lu.bout.rpg.engine.combat.action.CombatAction;
import lu.bout.rpg.engine.combat.participant.Participant;

/**
 * Class to represent the action a combat participant wants to take
 */
public interface ActionableCommand {

    public CombatAction getAction();
}
