package lu.bout.rpg.battler.campaign;

import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.battler.campaign.chapter.NarrativeChapter;
import lu.bout.rpg.battler.campaign.chapter.VictoryChapter;
import lu.bout.rpg.battler.campaign.storyAction.AddNpcAction;
import lu.bout.rpg.battler.map.MapFactory;
import lu.bout.rpg.battler.party.PlayerCharacter;

public class CampaignBuilder {

    public static PlayerCharacter getRandomNpc() {
        PortraitService p = new PortraitService();
        return new PlayerCharacter("Friend", p.getRandomIds(1)[0]);
    }

    public Campaign build2stepDungeon() {
        NarrativeChapter intro = new NarrativeChapter("intro", "Welcome to this simple dungeon crawl.");
        NarrativeChapter intermezzo = new NarrativeChapter("inter", "You made it this far, and a friend comes to your aid for the dungeon ahead.");
        MapFactory mapper = new MapFactory(15, 5);
        DungeonChapter dungeon = new DungeonChapter("d", mapper.generate());
        mapper = new MapFactory(5, 0);
        DungeonChapter smallDungeon = new DungeonChapter("s", mapper.generate());
        VictoryChapter v = new VictoryChapter("v");
        Campaign campaign = new Campaign();
        campaign.name = "Dungeon Crawl";
        campaign.setStartChapter(intro);
        campaign.addChapter(smallDungeon);
        campaign.addChapter(intermezzo);
        intermezzo.addAfterAction(new AddNpcAction(getRandomNpc()));
        campaign.addChapter(dungeon);
        campaign.addChapter(v);
        intro.setNext(smallDungeon);
        smallDungeon.setOnSuccess(intermezzo);
        intermezzo.setNext(dungeon);
        dungeon.setOnSuccess(v);
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
