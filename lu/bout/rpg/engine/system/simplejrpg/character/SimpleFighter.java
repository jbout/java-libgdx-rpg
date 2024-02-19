package lu.bout.rpg.engine.system.simplejrpg.character;

import java.util.LinkedList;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.system.CharacterKlass;
import lu.bout.rpg.engine.system.Skill;
import lu.bout.rpg.engine.system.simplejrpg.FighterKlass;
import lu.bout.rpg.engine.system.simplejrpg.skill.combatSkill.attack.MeleeAttack;

public class SimpleFighter extends Character {

    private int level;
    private int xp;

    public void setLevel(int level) {
        this.level = level;
        xp = 0;
        hp = getMaxhp();
    }

    public int getLevel() {
        return level;
    }

    @Override
    public LinkedList<Skill> getSkills() {
        LinkedList<Skill> list = new LinkedList<>();
        list.add(new MeleeAttack());
        return list;
    }

    public CharacterKlass getKlass() {
        return new FighterKlass();
    }

    public int getXp() {
        return xp;
    }

    public int xpToNextLevel() {
        return level * 1000;
    }

    public void earnXp(int bonusXp) {
        xp += bonusXp;
        if (xp >= xpToNextLevel()) {
            xp = xp - xpToNextLevel();
            level += 1;
            healsPercent(100);
        }
    }

    public int getConstitution() {
        return 5+(level * 5);
    }

    public int getStrength() {
        return 10+(level * 10);
    }

    public int getDamage() {
        int dmg = (int)(Math.random() * 10) + 5;
        return dmg * getStrength() / 100;
    }

    @Override
    public int getMaxhp() {
        return  10 + getConstitution() * 2;
    }
}
