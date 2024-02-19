package lu.bout.rpg.engine.debug;

import lu.bout.rpg.engine.system.simplejrpg.character.Monster;

public class SampleMonster extends Monster {
    private String name;
    private int level;


    protected int dmg;

    public SampleMonster(String name, int level) {
        super(level * 5);
        this.dmg = level;
        this.name = name;
        this.level = level;
    }

    public String toString() {
        return name + "(" + getHp() + ")";
    }

}
