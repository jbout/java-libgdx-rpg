package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.participant.Participant;

abstract public class ParticipiantEvent implements CombatEvent {

	private Participant actor;

	public ParticipiantEvent(Participant actor) {
		this.actor = actor;
	}

	public Participant getActor() {
		return actor;
	}
}
