package lu.bout.rpg.battler.world;

public class MonsterTemplate {
    private String name;
    private int level;
    private String texture;

    public GameMonster generateMonster() {
        return new GameMonster(level, texture);
    }
}
