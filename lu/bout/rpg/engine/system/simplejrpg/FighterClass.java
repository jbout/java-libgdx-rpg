package lu.bout.rpg.engine.system.simplejrpg;

import java.util.Arrays;
import java.util.List;

import lu.bout.rpg.engine.system.CharacterClass;
import lu.bout.rpg.engine.system.Skill;
import lu.bout.rpg.engine.system.simplejrpg.skill.MeleeAttack;

public class FighterClass extends CharacterClass {

    public List<Skill> getSkills() {
        return Arrays.asList(new Skill[]{new MeleeAttack()});
    }
}
