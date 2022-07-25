package lu.bout.rpg.engine.character;

public class Monster extends Character {

    int level;

    // for serialization only
    public Monster() {
    }

    public Monster(int level) {
        this.setLevel(level);
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
}
