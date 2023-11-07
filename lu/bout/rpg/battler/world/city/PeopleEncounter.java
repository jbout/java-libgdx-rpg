package lu.bout.rpg.battler.world.city;

import lu.bout.rpg.battler.campaign.storyAction.StoryAction;
import lu.bout.rpg.battler.party.PlayerCharacter;

public class PeopleEncounter {

    PlayerCharacter character;
    private StoryAction action;

    /**
     * for deserialization
     */
    public PeopleEncounter() {
    }

    public PeopleEncounter(PlayerCharacter character) {
        this.character = character;
    }

    public void onClickOnce(StoryAction action) {
        // see RemoveEncounterAction
        this.action = action;
    }
    public PlayerCharacter getActor() {
        return character;
    }

    public StoryAction getAction() {
        return action;
    }

}
