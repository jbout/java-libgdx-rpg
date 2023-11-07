package lu.bout.rpg.battler.campaign.storyAction;

import com.badlogic.gdx.Gdx;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.chapter.Chapter;
import lu.bout.rpg.battler.campaign.chapter.HubChapter;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.world.city.Location;

public class GoToLocationAction extends StoryAction {

    int locationId;

    // Serializaton constructor
    public GoToLocationAction() {
    }

    public GoToLocationAction(Location location) {
        this.locationId = location.getId();
    }

    public void run(RpgGame game) {
        Chapter chapter = game.state.getCurrentChapter();
        if (chapter instanceof HubChapter) {
            HubChapter free = (HubChapter) chapter;
            free.goToLocation(game, locationId);
        } else {
            Gdx.app.log("Game", "Unable to change location to " + locationId + " if not freeroaming");
        }
    }
}
