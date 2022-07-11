package lu.bout.rpg.battler.campaign.chapter;

import lu.bout.rpg.battler.map.DungeonMap;

public class DungeonChapter extends Chapter {

    public DungeonMap map;

    public DungeonChapter() {
    }

    public DungeonChapter(String id, DungeonMap map) {
        this.id = id;
        this.map = map;
    }

}
