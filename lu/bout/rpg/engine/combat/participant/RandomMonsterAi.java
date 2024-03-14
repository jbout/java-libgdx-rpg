package lu.bout.rpg.engine.combat.participant;

import java.util.ArrayList;
import java.util.LinkedList;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.CombatListener;
import lu.bout.rpg.engine.combat.command.CombatCommand;
import lu.bout.rpg.engine.combat.command.UseSkillCommand;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.event.ReadyEvent;
import lu.bout.rpg.engine.character.Skill;
import lu.bout.rpg.engine.character.skill.AttackSkill;
import lu.bout.rpg.engine.character.skill.CombatSkill;

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
			return new UseSkillCommand(getAttackSkill(), monster, new Participant[]{enemies.get(target)});
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

	private CombatSkill getAttackSkill() {
		ArrayList<CombatSkill> attackSkills = new ArrayList<>();
		for (Skill skill: monster.getCharacter().getSkills()) {
			if (skill instanceof AttackSkill) {
				attackSkills.add((CombatSkill) skill);
				break;
			}
		}
		if (attackSkills.size() == 0) {
			throw new RuntimeException("Enemy has no attack skills");
		}
		return attackSkills.get((int)(Math.random() * attackSkills.size()));
	}
}
