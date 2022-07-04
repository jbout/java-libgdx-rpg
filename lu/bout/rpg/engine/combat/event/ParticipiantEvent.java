package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.participant.Participant;

abstract public class ParticipiantEvent extends BaseEvent {

	private Participant actor;

	public ParticipiantEvent(Combat c, Participant actor) {
		super(c);
		this.actor = actor;
	}

	public Participant getActor() {
		return actor;
	}
}
