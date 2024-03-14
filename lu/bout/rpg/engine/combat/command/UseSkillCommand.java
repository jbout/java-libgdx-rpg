package lu.bout.rpg.engine.combat.command;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.action.CombatAction;
import lu.bout.rpg.engine.combat.participant.Participant;
import lu.bout.rpg.engine.character.skill.CombatSkill;
import lu.bout.rpg.engine.character.skill.InvalidTarget;

public class UseSkillCommand extends CombatCommand implements ActionableCommand {
    CombatSkill skill;
    Participant actor;
    Participant[] targets;

    public UseSkillCommand(CombatSkill skill, Participant actor, Participant[] targets) {
        this.skill = skill;
        this.actor = actor;
        this.targets = targets;
    }

    public void execute(Combat combat) throws InvalidTarget {
        skill.execute(actor, targets, combat);
    }
}
