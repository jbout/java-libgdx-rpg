package lu.bout.rpg.engine.character;

public interface SupportsXp {

    public int getXp();

    public int xpToNextLevel();

    public void earnXp(int bonusXp);
}
