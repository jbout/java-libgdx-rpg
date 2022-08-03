package lu.bout.rpg.battler.campaign.storyAction;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.CampaignState;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.PlayerParty;

public class AddNpcAction extends StoryAction {

    PlayerCharacter character;

    // Serializaton constructor
    public AddNpcAction() {
    }

    public AddNpcAction(PlayerCharacter character) {
        this.character = character;
    }

    public void run(RpgGame game, PlayerParty party) {
        party.addMember(character);
    }
}
