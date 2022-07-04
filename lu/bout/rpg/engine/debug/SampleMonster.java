package lu.bout.rpg.engine.debug;

import lu.bout.rpg.engine.character.Monster;

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

    public String getName() {
        return name + "(" + getHp() + ")";
    }

}
