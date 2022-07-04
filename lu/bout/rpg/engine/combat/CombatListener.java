package lu.bout.rpg.engine.combat;

import lu.bout.rpg.engine.combat.event.CombatEvent;

public interface CombatListener {
	
	public void receiveCombatEvent(CombatEvent a);

}
