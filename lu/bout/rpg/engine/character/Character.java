package lu.bout.rpg.engine.character;

abstract public class Character {

    protected int hp;

    public Character() {
        hp = this.getMaxhp();
    }

    abstract public int getMaxhp();

    abstract public int getDamage();

    abstract public int getLevel();

    public int getHp() {
        return hp;
    }

    public void takesDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
    }

    public void healsPercent(float percent) {
        hp = Math.min(hp + (int)(getMaxhp() * percent), getMaxhp());
    }
}
