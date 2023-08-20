package lu.bout.rpg.battler.world.city;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.storyAction.StoryAction;

public class VillageLocation extends Location {

    protected void enter(RpgGame game, LocationMap map) {
        game.showLocation(map, this);
    }
}
