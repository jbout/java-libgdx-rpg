package lu.bout.rpg.engine.combat.participant;

import java.util.HashMap;

import lu.bout.rpg.engine.character.CharacterSheet;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.command.CombatCommand;
import lu.bout.rpg.engine.combat.event.DeathEvent;
import lu.bout.rpg.engine.combat.status.AccumulatingStatus;
import lu.bout.rpg.engine.combat.status.CombatStatus;
import lu.bout.rpg.engine.combat.status.DamageReceivedAffectingStatus;

public class Participant {

	private CharacterSheet characterSheet;

	private int cooldown;
	private int cooldownRemaining;
	private int teamId;
	private CombatCommand nextCommand;

	private HashMap<Class, CombatStatus> statuses = new HashMap<>();
	
	public Participant(CharacterSheet characterSheet, int teamId, int cooldown) {
		this.characterSheet = characterSheet;
		this.teamId = teamId;
		setCooldown(cooldown);
	}

	public boolean isAlive() {
		return characterSheet.getHp() > 0;
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

	public CharacterSheet getCharacter() {
		return characterSheet;
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
		if (statuses.containsKey(status.getClass()) && statuses.get(status.getClass()) instanceof AccumulatingStatus) {
			((AccumulatingStatus) statuses.get(status.getClass())).accumulate(status);
		} else {
			statuses.put(status.getClass(), status);
		}
	}

	public void removeStatus(CombatStatus status) {
		statuses.remove(status.getClass());
	}

	public CombatStatus[] getStatuses() {
		return statuses.values().toArray(new CombatStatus[0]);
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
		characterSheet.takesDamage(dmg);
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
		return this.characterSheet.toString();
	}

}
