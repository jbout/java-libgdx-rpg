package lu.bout.rpg.engine.system.simplejrpg;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.system.CharacterKlass;

public class SampleFighter extends Character {

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
        if (xp > xpToNextLevel()) {
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
        return  50 + getConstitution() * 2;
    }
}
