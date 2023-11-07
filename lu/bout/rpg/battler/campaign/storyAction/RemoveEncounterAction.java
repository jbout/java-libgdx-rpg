package lu.bout.rpg.battler.campaign.storyAction;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.PeopleEncounter;

public class RemoveEncounterAction extends StoryAction {

    // TODO cannot serialize properties, needs to be changed to references
    private Location location;
    private PeopleEncounter encounter;

    private StoryAction nextAction;

    public void run(RpgGame game) {
        location.getEncounters().remove(encounter);
        if (nextAction != null) {
            nextAction.run(game);
        }
    }
}
