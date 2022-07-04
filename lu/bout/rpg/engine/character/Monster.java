package lu.bout.rpg.engine.character;

public class Monster extends Character {

    int level;

    public Monster(int level) {
        this.setLevel(level);
    }

    protected void setLevel(int level)
    {
        this.level = level;
        this.hp = getMaxhp();
    }

    public int getMaxhp()
    {
        return level * 5;
    }
}
