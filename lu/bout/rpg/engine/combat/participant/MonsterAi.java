package lu.bout.rpg.engine.combat.participant;

import java.util.LinkedList;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.CombatListener;
import lu.bout.rpg.engine.combat.command.AttackCommand;
import lu.bout.rpg.engine.combat.command.CombatCommand;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.event.ReadyEvent;

public class MonsterAi implements CombatAi, CombatListener {

	Participant monster;

	public MonsterAi(Participant monster) {
		this.monster = monster;
	}

	public CombatCommand getAttackCommand(Combat c) {
		LinkedList<Participant> enemies = c.getEnemies(monster);
		if (enemies.size() > 0) {
			int target = (int)(Math.random() * enemies.size());
			return new AttackCommand(enemies.get(target));
		} else {
			return null;
		}
	}

	@Override
	public void receiveCombatEvent(CombatEvent a) {
		if (a instanceof ReadyEvent) {
			if (((ReadyEvent)a).getActor() == monster) {
				CombatCommand command = getAttackCommand(a.getCombat());
				if (command != null) {
					monster.setNextCommand(command);
				}
			}
		}
	}
}
