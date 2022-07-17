package lu.bout.rpg.battler.battle.animation;

public interface BattleAnimation {

    /**
     * Draws the animation
     * @param delta time elapsed since last draw
     * @return whenever the animation finished
     */
    public boolean animate(float delta);
}
