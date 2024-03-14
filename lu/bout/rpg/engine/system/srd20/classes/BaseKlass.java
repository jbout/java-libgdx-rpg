package lu.bout.rpg.engine.system.srd20.classes;

import java.util.ArrayList;
import java.util.List;

import lu.bout.rpg.engine.character.CharacterKlass;
import lu.bout.rpg.engine.character.Skill;

public abstract class BaseKlass extends CharacterKlass {

    public List<Skill> getSkills() {
        ArrayList<Skill> skills = new ArrayList<>();
        return skills;
    }
}
