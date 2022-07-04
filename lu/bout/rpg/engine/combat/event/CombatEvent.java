package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.Combat;

/**
 * CombatEvent represents the outcome of an action of a combat participant
 */
public interface CombatEvent {

    public Combat getCombat();
}
