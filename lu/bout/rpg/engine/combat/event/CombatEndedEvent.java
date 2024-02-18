package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.Combat;

public class CombatEndedEvent extends BaseEvent {

	private int winner;

	public CombatEndedEvent(int winner) {
		this.winner = winner;
	}

	public boolean isPlayerWinner() {
		return winner == Combat.TEAM_PLAYER;
	}
}
