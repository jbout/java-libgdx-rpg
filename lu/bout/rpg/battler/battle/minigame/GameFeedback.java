package lu.bout.rpg.battler.battle.minigame;

public interface GameFeedback {

    public void minigameEnded(boolean success, MiniGame game, long timeElapsed);
}
