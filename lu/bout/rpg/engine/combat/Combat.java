package lu.bout.rpg.engine.combat;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.character.Party;
import lu.bout.rpg.engine.combat.command.ActionableCommand;
import lu.bout.rpg.engine.combat.command.AttackCommand;
import lu.bout.rpg.engine.combat.command.CombatCommand;
import lu.bout.rpg.engine.combat.event.CombatEndedEvent;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.event.ReadyEvent;
import lu.bout.rpg.engine.combat.participant.CombatAi;
import lu.bout.rpg.engine.combat.participant.RandomMonsterAi;
import lu.bout.rpg.engine.combat.participant.Participant;

public class Combat {

	// TODO do not hardcore player team
	final public static int TEAM_PLAYER = 0;
	final public static int TEAM_ENEMY = 1;

	private LinkedList<CombatEvent> pendingEvents = new LinkedList<>();

	private LinkedList<CombatListener> listeners = new LinkedList<CombatListener>();
	
	private LinkedList<Participant> persons = new LinkedList<Participant>();

	public Combat(Encounter encounter) {
		for (Map.Entry<Integer, Party> ref: encounter.getParties().entrySet()) {
			for (Character character: ref.getValue().getMembers()) {
				this.participate(character, ref.getKey());
			}
		}
	}

	protected void participate(Character character, int teamId) {
		Participant p = new Participant(character, teamId, (int)(Math.random() * 90) + 10);
		CombatAi brain = new RandomMonsterAi(p);// character.getClass() == Monster.class ? new MonsterAi() : new PlayerUi();
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

	public Participant getParticipant(Character character) {
		for (Participant candidate: persons) {
			if (candidate.getCharacter() == character) {
				return candidate;
			}
		}
		return null;
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
						p.setCooldown(100);
					}
				} else {
					p.elapseTime(minCooldown, this);
					if (p.isReady()) {
						trigger(new ReadyEvent(this, p));
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
		propagateEvents();
		return minCooldown;
	}

	/**
	 * @return id of the team who won, -1 if noone won
	 */
	public int whoWon() {
		int teamId = -1;
		for(Participant p : persons) {
			if (p.isAlive()) {
				if (teamId == -1) {
					teamId = p.getTeamId();
				} else {
					if (teamId != p.getTeamId()) {
						// two different teams detected
						return -1;
					}
				}
			}
		}
		return teamId;
	}
	
	private void executeCommand(Participant who, CombatCommand command) {
		if (who.isReady()) {
			if (command.getClass() == AttackCommand.class) {
				((AttackCommand) command).getAction(who).execute(this);
			} else if (command instanceof ActionableCommand) {
				((ActionableCommand) command).getAction().execute(this);
			}
		}
		int winner = whoWon();
		if (winner != -1) {
			this.trigger(new CombatEndedEvent(winner));
		}
	}

	//=========================================

	public void trigger(CombatEvent event) {
		pendingEvents.add(event);
	}

	private void propagateEvents() {
		for (CombatEvent event: pendingEvents) {
			for (CombatListener listener : listeners) {
				listener.receiveCombatEvent(this, event);
			}
		}
		pendingEvents.clear();
	}

}
