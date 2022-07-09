package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.Combat;

public class CombatEndedEvent extends BaseEvent {

	private boolean playerWon;

	public CombatEndedEvent(Combat c, boolean isPlayerWinner) {
		super(c);
		playerWon = isPlayerWinner;
	}

	public boolean isPlayerWinner() {
		return playerWon;
	}
}
