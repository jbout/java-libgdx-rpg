package lu.bout.rpg.battler.campaign.chapter;

import lu.bout.rpg.battler.campaign.storyAction.GoToChapterAction;
import lu.bout.rpg.battler.campaign.storyAction.StoryAction;
import lu.bout.rpg.battler.dungeon.DungeonMap;

public class DungeonChapter extends Chapter {

    private DungeonMap map;
    private StoryAction onSuccess = null;

    public DungeonChapter() {
    }

    public DungeonChapter(String id, DungeonMap map) {
        this.id = id;
        this.map = map;
    }

    public DungeonMap getMap() {
        return map;
    }

    public void setOnSuccess(Chapter next) {
        onSuccess = new GoToChapterAction(next.getId());
    }

    public void setOnSuccess(StoryAction action) {
        onSuccess = action;
    }

    public StoryAction getOnSuccessAction() {
        return onSuccess;
    }

}
