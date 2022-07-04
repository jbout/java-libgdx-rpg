package lu.bout.rpg.engine.combat.participant;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.command.CombatCommand;

public class Participant {

	private Character character;

	private int cooldown = 100;
	private int teamId;
	private CombatCommand nextCommand;
	
	public Participant(Character character, int teamId, int cooldown) {
		this.character = character;
		this.teamId = teamId;
		this.cooldown = cooldown;
	}

	public boolean isAlive() {
		return character.getHp() > 0;
	}

	public boolean isReady() {
		return cooldown == 0;
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

	public void addCooldown(int time) {
		cooldown += time;
	}

	public int getCooldown() {
		return cooldown;
	}

	public Character getCharacter() {
		return character;
	}

	public int getTeamId() {
		return teamId;
	}

	public void elapseTime(int time, Combat combat) {
		cooldown -= time;
		if (cooldown < 0) {
			cooldown = 0;
		}
	}

	public void takesDamage(int dmg) {
		character.takesDamage(dmg);
	}

}
