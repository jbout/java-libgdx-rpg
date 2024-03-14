package lu.bout.rpg.story.premade;

import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.campaign.Campaign;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.battler.campaign.chapter.HubChapter;
import lu.bout.rpg.battler.campaign.chapter.NarrativeChapter;
import lu.bout.rpg.battler.campaign.chapter.VictoryChapter;
import lu.bout.rpg.battler.campaign.storyAction.AddNpcAction;
import lu.bout.rpg.battler.campaign.storyAction.GoToChapterAction;
import lu.bout.rpg.battler.dungeon.generator.CombatEncounterFactory;
import lu.bout.rpg.battler.dungeon.generator.MapFactory;
import lu.bout.rpg.battler.party.Person;
import lu.bout.rpg.battler.world.city.DungeonLocation;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.PeopleEncounter;
import lu.bout.rpg.battler.world.city.VillageLocation;
import lu.bout.rpg.battler.world.city.LocationMap;
import lu.bout.rpg.engine.system.System;
import lu.bout.rpg.engine.system.simplejrpg.character.SimpleFighter;

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
                new CampaignReference("free", "Rogue like"),
//                new CampaignReference("2step", "2 Step dungeon"),
        };
    }

    public Campaign getCampaign(CampaignReference ref, System combatSystem) {
        switch (ref.id) {
            case "2step":
                return build2stepDungeon(combatSystem);
            case "free":
                return buildFreeRoamCampaign(combatSystem);
            default:
                throw new RuntimeException("Campaign " + ref.id + " not found");
        }
    }

    public Campaign build2stepDungeon(System combatSystem) {
        CombatEncounterFactory e = new CombatEncounterFactory(combatSystem);
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

    public Campaign buildFreeRoamCampaign(System combatSystem) {
        Campaign campaign = new Campaign();
        Person friend = getRandomNpc();
        VictoryChapter v = new VictoryChapter("v");
        LocationMap map = new LocationMap();
        VillageLocation village = new VillageLocation();
        village.name = "Village";
        VillageLocation inn = new VillageLocation();
        inn.name = "Inn";
        PeopleEncounter meetFriend = new PeopleEncounter(friend);
        meetFriend.onClickOnce(new AddNpcAction(friend));
        inn.addEncounter(meetFriend);

        CombatEncounterFactory e = new CombatEncounterFactory(combatSystem);
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

    protected static Person getRandomNpc() {
        PortraitService p = new PortraitService();
        // TODO get npc from System
        Person player = new Person("Friend", p.getRandomIds(1)[0], new SimpleFighter());
        player.setBattleMini("enemy/sample-hero2.png");
        return player;
    }

}
