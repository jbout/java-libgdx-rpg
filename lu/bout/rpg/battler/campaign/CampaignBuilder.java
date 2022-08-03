package lu.bout.rpg.battler.campaign;

import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.battler.campaign.chapter.FreeRoamChapter;
import lu.bout.rpg.battler.campaign.chapter.NarrativeChapter;
import lu.bout.rpg.battler.campaign.chapter.VictoryChapter;
import lu.bout.rpg.battler.campaign.storyAction.AddNpcAction;
import lu.bout.rpg.battler.campaign.storyAction.GoToChapterAction;
import lu.bout.rpg.battler.map.EncounterFactory;
import lu.bout.rpg.battler.map.MapFactory;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.world.city.DungeonLocation;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.LocationMap;

public class CampaignBuilder {

    public static PlayerCharacter getRandomNpc() {
        PortraitService p = new PortraitService();
        PlayerCharacter player = new PlayerCharacter("Friend", p.getRandomIds(1)[0], 5);
        player.setBattleMini("enemy/sample-hero2.png");
        return player;
    }

    public Campaign build2stepDungeon() {
        EncounterFactory e = new EncounterFactory();
        NarrativeChapter intro = new NarrativeChapter("intro", "Welcome to this simple dungeon crawl.");

        MapFactory mapper = new MapFactory(e,5, 0, 2, 6);
        DungeonChapter smallDungeon = new DungeonChapter("s", mapper.generate());

        NarrativeChapter intermezzo = new NarrativeChapter("inter", "You made it this far, and a friend comes to your aid for the dungeon ahead.");

        mapper = new MapFactory(e,15, 5, 5, 12);
        DungeonChapter dungeon = new DungeonChapter("d", mapper.generate());

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

    public Campaign buildFreeRoamCampaign() {
        Campaign campaign = new Campaign();
        VictoryChapter v = new VictoryChapter("v");
        LocationMap map = new LocationMap();
        Location village = new Location();
        village.name = "Village";
        Location inn = new Location();
        inn.name = "Inn";

        EncounterFactory e = new EncounterFactory();
        MapFactory mapper = new MapFactory(e,15, 5, 5, 12);
        Location dungeon = new DungeonLocation(mapper.generate(), new GoToChapterAction("V"));
        dungeon.name = "Dungeon";
        map.addLocation(village);
        map.addLocation(inn);
        map.addLocation(dungeon);
        map.linkLocations(village, inn);
        map.linkLocations(inn, village);
        map.linkLocations(village, dungeon);
        map.linkLocations(dungeon, village);
        FreeRoamChapter free = new FreeRoamChapter("free", map, village);
        campaign.setStartChapter(free);
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
