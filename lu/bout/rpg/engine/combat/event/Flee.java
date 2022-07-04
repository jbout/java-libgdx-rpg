package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.participant.Participant;

public class Flee extends ParticipiantEvent {

	public Flee(Combat c, Participant actor) {
		super(c, actor);
	}
}
