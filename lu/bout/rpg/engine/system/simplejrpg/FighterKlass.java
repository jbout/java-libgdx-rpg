package lu.bout.rpg.engine.system.simplejrpg;

import java.util.Arrays;
import java.util.List;

import lu.bout.rpg.engine.system.CharacterKlass;
import lu.bout.rpg.engine.system.Skill;
import lu.bout.rpg.engine.system.simplejrpg.skill.MeleeAttack;

public class FighterKlass extends CharacterKlass {

    public String getName() {
        return "Fighter";
    }

    public List<Skill> getSkills() {
        return Arrays.asList(new Skill[]{new MeleeAttack()});
    }
}
