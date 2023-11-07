package lu.bout.rpg.story.premade;

import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.campaign.Campaign;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.battler.campaign.chapter.HubChapter;
import lu.bout.rpg.battler.campaign.chapter.NarrativeChapter;
import lu.bout.rpg.battler.campaign.chapter.VictoryChapter;
import lu.bout.rpg.battler.campaign.storyAction.AddNpcAction;
import lu.bout.rpg.battler.campaign.storyAction.GoToChapterAction;
import lu.bout.rpg.battler.dungeon.CombatEncounterFactory;
import lu.bout.rpg.battler.dungeon.MapFactory;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.world.city.DungeonLocation;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.PeopleEncounter;
import lu.bout.rpg.battler.world.city.VillageLocation;
import lu.bout.rpg.battler.world.city.LocationMap;

public class CampaignBuilder {

    public static class CampaignReference {
        String id;
        String label;

        protected CampaignReference(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    public CampaignReference[] getAvailableCampagins() {
        return new CampaignReference[] {
                new CampaignReference("2step", "2 Step dungeon"),
                new CampaignReference("free", "Free Roam"),
        };
    }

    public Campaign getCampaign(CampaignReference ref) {
        switch (ref.id) {
            case "2step":
                return build2stepDungeon();
            case "free":
                return buildFreeRoamCampaign();
            default:
                throw new RuntimeException("Campaign " + ref.id + " not found");
        }
    }

    public Campaign build2stepDungeon() {
        CombatEncounterFactory e = new CombatEncounterFactory();
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
        PlayerCharacter friend = getRandomNpc();
        VictoryChapter v = new VictoryChapter("v");
        LocationMap map = new LocationMap();
        VillageLocation village = new VillageLocation();
        village.name = "Village";
        VillageLocation inn = new VillageLocation();
        inn.name = "Inn";
        PeopleEncounter meetFriend = new PeopleEncounter(friend);
        meetFriend.onClickOnce(new AddNpcAction(friend));
        inn.addEncounter(meetFriend);

        CombatEncounterFactory e = new CombatEncounterFactory();
        MapFactory mapper = new MapFactory(e,15, 5, 5, 12);
        Location dungeon = new DungeonLocation(mapper.generate(), new GoToChapterAction("V"));
        dungeon.name = "Dungeon";
        //inn.setAfterEnter(new AddNpcAction(friend));
        map.addLocation(village);
        map.addLocation(inn);
        map.addLocation(dungeon);
        map.doubleLink(village, inn);
        map.doubleLink(village, dungeon);
        HubChapter free = new HubChapter("free", map, village);
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

    protected static PlayerCharacter getRandomNpc() {
        PortraitService p = new PortraitService();
        PlayerCharacter player = new PlayerCharacter("Friend", p.getRandomIds(1)[0], 5);
        player.setBattleMini("enemy/sample-hero2.png");
        return player;
    }

}
