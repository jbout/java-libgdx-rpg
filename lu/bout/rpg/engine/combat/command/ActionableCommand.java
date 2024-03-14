package lu.bout.rpg.engine.combat.command;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.action.CombatAction;
import lu.bout.rpg.engine.character.skill.InvalidTarget;

/**
 * Class to represent the action a combat participant wants to take
 */
public interface ActionableCommand {

    public void execute(Combat combat) throws InvalidTarget;
}
