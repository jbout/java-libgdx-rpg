package lu.bout.rpg.battler.world.city;

import lu.bout.rpg.battler.campaign.storyAction.StoryAction;
import lu.bout.rpg.battler.party.Person;

public class PeopleEncounter {

    Person character;
    private StoryAction action;

    /**
     * for deserialization
     */
    public PeopleEncounter() {
    }

    public PeopleEncounter(Person character) {
        this.character = character;
    }

    public void onClickOnce(StoryAction action) {
        // see RemoveEncounterAction
        this.action = action;
    }
    public Person getActor() {
        return character;
    }

    public StoryAction getAction() {
        return action;
    }

}
