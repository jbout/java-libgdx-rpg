package lu.bout.rpg.battler.world.city;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.chapter.HubChapter;
import lu.bout.rpg.battler.campaign.storyAction.GoToLocationAction;
import lu.bout.rpg.battler.campaign.storyAction.StoryAction;
import lu.bout.rpg.battler.dungeon.DungeonMap;

public class DungeonLocation extends Location {

    DungeonMap dungeonMap;
    StoryAction onSuccess = null;
    StoryAction onFailure = null;

    public DungeonLocation() {
        super();
    };

    public DungeonLocation(DungeonMap map, StoryAction onSuccess) {
        dungeonMap = map;
        this.onSuccess = onSuccess;
    };

    protected void enter(RpgGame game, LocationMap map) {
        Location current = HubChapter.fromGame(game).getCurrentLocation();
        game.gotoDungeon(dungeonMap, onSuccess, new GoToLocationAction(current));
    }
}
