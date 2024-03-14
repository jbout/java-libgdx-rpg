package lu.bout.rpg.engine.system.simplejrpg.character;

import java.util.Arrays;
import java.util.List;

import lu.bout.rpg.engine.character.CharacterKlass;
import lu.bout.rpg.engine.character.Skill;
import lu.bout.rpg.engine.system.simplejrpg.skill.combatSkill.attack.WeaponHit;

public class FighterKlass extends CharacterKlass {

    public String getName() {
        return "Fighter";
    }

    public List<Skill> getSkills() {
        return Arrays.asList(new Skill[]{new WeaponHit()});
    }
}
