package lu.bout.rpg.engine.system.simplejrpg.monster;

import java.util.Arrays;
import java.util.LinkedList;

import lu.bout.rpg.engine.character.CharacterSheet;
import lu.bout.rpg.engine.character.Skill;
import lu.bout.rpg.engine.system.simplejrpg.skill.combatSkill.attack.WeaponHit;

public class Monster extends CharacterSheet {

    int level;

    String[] tags;

    // for serialization only
    public Monster() {
    }

    public Monster(int level) {
        this(level, new String[0]);
    }

    public Monster(int level, String[] tags) {
        this.setLevel(level);
        this.tags = tags;
    }


    protected void setLevel(int level)
    {
        this.level = level;
        this.hp = getMaxhp();
    }

    public int getLevel() {
        return level;
    }

    public int getMaxhp()
    {
        return level * 5;
    }

    @Override
    public int getDamage() {
        return Math.max(1, (int)(level * (Math.random() + 0.5)));
    }

    public LinkedList<Skill> getSkills() {
        LinkedList<Skill> list = new LinkedList<>();
        list.add(new WeaponHit());
        return list;
    }

    @Override
    public Iterable<String> getTags() {
        return Arrays.asList(tags);
    }
}