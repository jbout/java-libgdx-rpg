package lu.bout.rpg.battler.campaign;

import lu.bout.rpg.battler.campaign.chapter.Chapter;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.battler.campaign.chapter.NarrativeChapter;
import lu.bout.rpg.battler.map.MapFactory;

public class CampaignBuilder {

    public Campaign justSingleDungeon() {
        NarrativeChapter intro = new NarrativeChapter("intro", "Welcome to this simple dungeon crawl.");
        MapFactory mapper = new MapFactory(15, 5);
        DungeonChapter dungeon = new DungeonChapter("d", mapper.generate());
        Campaign campaign = new Campaign();
        campaign.name = "Dungeon Crawl";
        campaign.setStartChapter(intro);
        campaign.addChapter(dungeon);
        intro.setNext(dungeon);
        return campaign;
    }

    public Campaign buildGhoulCampaign() {
        Campaign campaign = new Campaign();
        NarrativeChapter intro = new NarrativeChapter("intro",
                "You sit alone in your study when you suddenly hear a scratching coming from "
                + "the floor. As you gaze up you notice that the door has disappeared. You need to "
                + "find a way out of here.");
        campaign.setStartChapter(intro);
        campaign.name = "Night of the Zealot";
        return campaign;
    }
}
