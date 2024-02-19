package lu.bout.rpg.engine.combat.participant;

import java.util.HashMap;
import java.util.LinkedList;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.command.CombatCommand;
import lu.bout.rpg.engine.combat.event.DeathEvent;
import lu.bout.rpg.engine.combat.status.CombatStatus;
import lu.bout.rpg.engine.combat.status.DamageReceivedAffectingStatus;

public class Participant {

	private Character character;

	private int cooldown;
	private int cooldownRemaining;
	private int teamId;
	private CombatCommand nextCommand;

	private HashMap<Class, CombatStatus> statuses = new HashMap<>();
	
	public Participant(Character character, int teamId, int cooldown) {
		this.character = character;
		this.teamId = teamId;
		setCooldown(cooldown);
	}

	public boolean isAlive() {
		return character.getHp() > 0;
	}

	public boolean isReady() {
		return cooldownRemaining == 0;
	}

	public void setNextCommand(CombatCommand command) {
		nextCommand = command;
	}

	public CombatCommand getNextCommand() {
		CombatCommand toRun = nextCommand;
		if (toRun != null) {
			nextCommand = null;
		}
		return toRun;
	}

	public void setCooldown(int time) {
		cooldown = time;
		cooldownRemaining = time;
	}

	public int getCooldown() {
		return cooldownRemaining;
	}

	public float getCooldownPercentRemaining() {
		return (float) cooldownRemaining / cooldown;
	}

	public Character getCharacter() {
		return character;
	}

	public int getTeamId() {
		return teamId;
	}

	public void elapseTime(int time, Combat combat) {
		cooldownRemaining -= time;
		if (cooldownRemaining < 0) {
			cooldownRemaining = 0;
		}
	}

	public void setStatus(CombatStatus status) {
		statuses.put(status.getClass(), status);
	}

	public void removeStatus(CombatStatus status) {
		statuses.remove(status.getClass());
	}

	public CombatStatus getStatus(Class statusType) {
		return statuses.get(statusType);
	}

	/**
	 * Assign the damage from an attack
	 * @param combat combat context
	 * @param dmg incoming unmodified damage
	 * @return actual received damage
	 */
	public int takesDamage(Combat combat, int dmg) {
		for (CombatStatus status: statuses.values()) {
			if (status instanceof DamageReceivedAffectingStatus) {
				dmg = ((DamageReceivedAffectingStatus) status).adjustReceivedDamage(this, dmg);
			}
		}
		character.takesDamage(dmg);
		if (!isAlive()) {
			combat.trigger(new DeathEvent(this));
		}
		return dmg;
	}

	/**
	 * For debugging only
	 * @return human readable name of the character
	 */
	public String toString() {
		return this.character.toString();
	}

}
