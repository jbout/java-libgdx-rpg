package lu.bout.rpg.battler.battle.minigame;

public interface MiniGameFeedback {

    public void minigameEnded(boolean success, MiniGame game, long timeElapsed);
}
