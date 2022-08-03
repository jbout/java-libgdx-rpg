package lu.bout.rpg.battler.campaign.storyAction;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.CampaignState;
import lu.bout.rpg.battler.party.PlayerParty;

public abstract class StoryAction {
    public abstract void run(RpgGame game, PlayerParty party);
}
