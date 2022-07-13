package lu.bout.rpg.battler.campaign.storyAction;

import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.PlayerParty;

public class AddNpcAction extends StoryAction {

    PlayerCharacter character;

    public AddNpcAction(PlayerCharacter character) {
        this.character = character;
    }

    public void run(PlayerParty party) {
        party.addMember(character);
    }
}
