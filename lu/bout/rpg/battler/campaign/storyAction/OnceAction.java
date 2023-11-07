package lu.bout.rpg.battler.campaign.storyAction;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.party.PlayerParty;

public abstract class OnceAction extends StoryAction {
    private boolean run = false;

    public abstract void runOnce(RpgGame game);

    public void run(RpgGame game) {
        if (run == false) {
            runOnce(game);
            run = true;
        }
    }
}
