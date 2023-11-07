package lu.bout.rpg.battler.world.city;

import java.util.LinkedList;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.storyAction.StoryAction;
import lu.bout.rpg.story.world.character.PlayerCharacter;

public abstract class Location {

    public String name;
    int id;
    private StoryAction beforeEnter;
    private StoryAction afterEnter;

    private LinkedList<PlayerCharacter> npcs = new LinkedList<>();

    private LinkedList<PeopleEncounter> encounters = new LinkedList<>();

    public Location() {
    }

    public int getId() {
        return id;
    }

    public void setBeforeEnter(StoryAction action) {
        beforeEnter = action;
    }

    public void setAfterEnter(StoryAction action) {
        afterEnter = action;
    }

    public void addEncounter(PeopleEncounter encounter) {
        encounters.add(encounter);
    }

    public void goTo(RpgGame game, LocationMap map) {
        if (beforeEnter != null) {
            beforeEnter.run(game);
        }
        enter(game, map);
        if (afterEnter != null) {
            afterEnter.run(game);
        }
    }

    public LinkedList<PlayerCharacter> getNpcs() {
        return npcs;
    }

    public LinkedList<PeopleEncounter> getEncounters() {
        return encounters;
    }

    protected abstract void enter(RpgGame game, LocationMap map);
}
