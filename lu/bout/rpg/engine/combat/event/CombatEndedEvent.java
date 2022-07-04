package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.Combat;

public class CombatEndedEvent extends BaseEvent {

	public CombatEndedEvent(Combat c) {
		super(c);
	}
}
