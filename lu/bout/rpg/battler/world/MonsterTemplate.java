package lu.bout.rpg.battler.world;

public class MonsterTemplate {
    private String name;
    private int minlevel;
    private int maxlevel;
    private String texture;

    public GameMonster generateMonster() {
        return new GameMonster(minlevel + (int)(Math.random() * (maxlevel-minlevel+1)), texture);
    }

    public String getId() {
        return name;
    }

    public int[] getBoundaries() {
        return new int[]{minlevel, maxlevel};
    }

    public GameMonster generateMonster(int level) {
        return new GameMonster(level, texture);
    }

    public String getMiniTextureName() {
        return texture;
    }
}
