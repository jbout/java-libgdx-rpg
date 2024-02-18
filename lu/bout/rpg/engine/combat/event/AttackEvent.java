package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.participant.Participant;

public class AttackEvent extends ParticipiantEvent {

	private Participant target;
	private int damage;
	
	public AttackEvent(Combat c, Participant attacker, Participant target, int damage) {
		super(attacker);
		this.target = target;
		this.damage = damage;
	}
	
	public Participant getAttacker() {
		return getActor();
	}

	public Participant getTarget() {
		return target;
	}

	public int getDamage() {
		return damage;
	}
}
