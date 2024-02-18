package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.participant.Participant;

public class ReadyEvent extends ParticipiantEvent {

	public ReadyEvent(Combat c, Participant actor) {
		super(actor);
	}
}
