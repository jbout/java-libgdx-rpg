package lu.bout.rpg.engine.system.simplejrpg.monster;

public class MonsterTemplate {
    private String name;
    private int minlevel;
    private int maxlevel;
    private String[] tags;

    public MonsterTemplate(int min, int max, String[] tags) {
        minlevel = min;
        maxlevel = max;
        this.tags = tags;
    }

    public Monster generateMonster() {
        return new Monster(minlevel + (int)(Math.random() * (maxlevel-minlevel+1)), tags);
    }

    public String getId() {
        return name;
    }

    public int[] getBoundaries() {
        return new int[]{minlevel, maxlevel};
    }

    public Monster generateMonster(int level) {
        return new Monster(level, tags);
    }

}
