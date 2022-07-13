package lu.bout.rpg.battler.campaign.chapter;

import lu.bout.rpg.battler.map.DungeonMap;

public class DungeonChapter extends Chapter {

    public DungeonMap map;
    public String onSuccessChapterId;

    public DungeonChapter() {
    }

    public DungeonChapter(String id, DungeonMap map) {
        this.id = id;
        this.map = map;
    }

    public void setOnSuccess(Chapter next) {
        onSuccessChapterId = next.getId();
    }

}
