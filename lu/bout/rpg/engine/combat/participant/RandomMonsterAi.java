package lu.bout.rpg.engine.combat.participant;

import java.util.LinkedList;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.CombatListener;
import lu.bout.rpg.engine.combat.action.attack.HitAction;
import lu.bout.rpg.engine.combat.command.AttackCommand;
import lu.bout.rpg.engine.combat.command.CombatCommand;
import lu.bout.rpg.engine.combat.command.GenericCommand;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.event.ReadyEvent;
import lu.bout.rpg.engine.system.Skill;

public class RandomMonsterAi implements CombatAi, CombatListener {

	Participant monster;

	public RandomMonsterAi(Participant monster) {
		this.monster = monster;
	}

	public CombatCommand getAttackCommand(Combat c) {
		Skill skill = monster.getCharacter().getSkills().getFirst();
		LinkedList<Participant> enemies = c.getEnemies(monster);
		if (enemies.size() > 0) {
			int target = (int)(Math.random() * enemies.size());
			return new GenericCommand(new HitAction(monster, enemies.get(target)));
//			return new AttackCommand(enemies.get(target));
		} else {
			return null;
		}
	}

	@Override
	public void receiveCombatEvent(Combat combat, CombatEvent a) {
		if (a instanceof ReadyEvent) {
			if (((ReadyEvent)a).getActor() == monster) {
				CombatCommand command = getAttackCommand(combat);
				if (command != null) {
					monster.setNextCommand(command);
				}
			}
		}
	}
}
