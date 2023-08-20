package lu.bout.rpg.battler.world.city;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.storyAction.StoryAction;

public abstract class Location {

    public String name;
    int id;
    private StoryAction beforeEnter;
    private StoryAction afterEnter;

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

    public void goTo(RpgGame game, LocationMap map) {
        if (beforeEnter != null) {
            beforeEnter.run(game, game.state.getPlayerParty());
        }
        enter(game, map);
        if (afterEnter != null) {
            afterEnter.run(game, game.state.getPlayerParty());
        }
    }

    protected abstract void enter(RpgGame game, LocationMap map);
}
