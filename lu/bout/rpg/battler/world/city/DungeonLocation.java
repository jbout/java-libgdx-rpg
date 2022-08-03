package lu.bout.rpg.battler.world.city;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.chapter.Chapter;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.battler.campaign.chapter.FreeRoamChapter;
import lu.bout.rpg.battler.campaign.storyAction.GoToChapterAction;
import lu.bout.rpg.battler.campaign.storyAction.GoToLocationAction;
import lu.bout.rpg.battler.campaign.storyAction.StoryAction;
import lu.bout.rpg.battler.map.DungeonMap;

public class DungeonLocation extends Location{

    DungeonMap dungeonMap;
    StoryAction onSuccess = null;
    StoryAction onFailure = null;

    public DungeonLocation() {};

    public DungeonLocation(DungeonMap map, StoryAction onSuccess) {
        dungeonMap = map;
        this.onSuccess = onSuccess;
    };

    public void enter(RpgGame game, LocationMap map) {
        Location current = FreeRoamChapter.fromGame(game).getCurrentLocation();
        game.gotoDungeon(dungeonMap, onSuccess, new GoToLocationAction(current));
    }
}
