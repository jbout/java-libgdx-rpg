package lu.bout.rpg.engine.system.srd20.classes;

import java.util.ArrayList;
import java.util.List;

import lu.bout.rpg.engine.system.CharacterClass;
import lu.bout.rpg.engine.system.Skill;

public class BaseClass extends CharacterClass {

    public List<Skill> getSkills() {
        ArrayList<Skill> skills = new ArrayList<>();
        return skills;
    }
}
