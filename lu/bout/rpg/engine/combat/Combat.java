package lu.bout.rpg.engine.combat;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.character.Party;
import lu.bout.rpg.engine.combat.command.AttackCommand;
import lu.bout.rpg.engine.combat.command.CombatCommand;
import lu.bout.rpg.engine.combat.event.AttackEvent;
import lu.bout.rpg.engine.combat.event.CombatEndedEvent;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.event.DeathEvent;
import lu.bout.rpg.engine.combat.event.ReadyEvent;
import lu.bout.rpg.engine.combat.participant.CombatAi;
import lu.bout.rpg.engine.combat.participant.MonsterAi;
import lu.bout.rpg.engine.combat.participant.Participant;

public class Combat {

	final static int TEAM_PLAYER = 0;
	final static int TEAM_ENEMY = 1;

	private LinkedList<CombatListener> listeners = new LinkedList<CombatListener>();
	
	private LinkedList<Participant> persons = new LinkedList<Participant>();

	public Combat(Encounter encounter) {
		Party playerParty = encounter.getPlayerParty();
		Party enemyParty = encounter.getOpponentParty();
		for (Character character: playerParty.getMembers()) {
			this.participate(character, TEAM_PLAYER);
		}
		for (Character character: enemyParty.getMembers()) {
			this.participate(character, TEAM_ENEMY);
		}
	}

	protected void participate(Character character, int teamId) {
		Participant p = new Participant(character, teamId, (int)(Math.random() * 90) + 10);
		CombatAi brain = new MonsterAi(p);// character.getClass() == Monster.class ? new MonsterAi() : new PlayerUi();
		this.addListener(brain);
		persons.add(p);
	}

	public LinkedList<Participant> getParticipants() {
		return persons;
	}
	
	public LinkedList<Participant> getEnemies(Participant participant) {
		LinkedList<Participant> enemies = new LinkedList<Participant>();
		for (Participant candidate: persons) {
			if (candidate.isAlive() && participant.getTeamId() != candidate.getTeamId()) {
				enemies.add(candidate);
			}
		}
		return enemies;
	}
	
	public boolean isOver() {
		return false;
	}

	public void addListener(CombatListener listener) {
		listeners.add(listener);
	}

	public void advanceTimer(int units) {
		do {
			int elapsed = stepTimeToNextAction(units);
			units -= elapsed;
		} while (units > 0);
	}

	protected int stepTimeToNextAction(int maxSteps) {
		int minCooldown = maxSteps;
		for (Participant p: persons) {
			if (!p.isReady() && p.getCooldown() < minCooldown) {
				minCooldown = p.getCooldown();
			}
		}

		for(Participant p : persons) {
			if (p.isAlive()) {
				if (p.isReady()) {
					CombatCommand c = p.getNextCommand();
					if (c != null) {
						executeCommand(p, c);
						p.addCooldown(100);
					}
				} else {
					p.elapseTime(minCooldown, this);
					if (p.isReady()) {
						propagate(new ReadyEvent(this, p));
					}
				}
			}
		}
		Collections.sort(persons, new Comparator<Participant>() {
			@Override
			public int compare(Participant p1, Participant p2) {
				return p1.getCooldown() - p2.getCooldown();
			}
		});
		return minCooldown;
	}

	public boolean isCombatOver() {
		int teamId = -1;
		for(Participant p : persons) {
			if (p.isAlive()) {
				if (teamId == -1) {
					teamId = p.getTeamId();
				} else {
					if (teamId != p.getTeamId()) {
						// two different teams detected
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void executeCommand(Participant who, CombatCommand command) {
		if (who.isReady()) {
			if (command.getClass() == AttackCommand.class) {
				Participant target = ((AttackCommand) command).getTarget();
				attackAction(who, target);
				if (!target.isAlive()) {
					propagate(new DeathEvent(this, target));
				}
			}
		}
		if (isCombatOver()) {
			this.propagate(new CombatEndedEvent(this));
		}
	}

	//============== Actions ==================
	
	private void attackAction(Participant who, Participant whom) {
		// TODO calculate damage
		int dmg = 10;
		CombatEvent event = new AttackEvent(this, who, whom, dmg);
		whom.takesDamage(dmg);
		this.propagate(event);
	}

	//=========================================

	private void propagate(CombatEvent action) {
		for(CombatListener listener : listeners) {
			listener.receiveCombatEvent(action);
		}
	}

}
