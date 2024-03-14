package lu.bout.rpg.engine.debug;

import lu.bout.rpg.engine.system.simplejrpg.monster.Monster;

public class SampleMonster extends Monster {
    private String name;

    public SampleMonster(String name, int level) {
        super(level * 5);
        this.name = name;
    }

    public String toString() {
        return name + "(" + getHp() + ")";
    }

}
