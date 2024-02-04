package lu.bout.rpg.engine.combat.participant;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.command.CombatCommand;

public class Participant {

	private Character character;

	private int cooldown;
	private int cooldownRemaining;
	private int teamId;
	private CombatCommand nextCommand;
	
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

	public void takesDamage(int dmg) {
		character.takesDamage(dmg);
	}

}
