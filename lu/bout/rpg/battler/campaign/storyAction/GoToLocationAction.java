package lu.bout.rpg.battler.campaign.storyAction;

import com.badlogic.gdx.Gdx;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.chapter.Chapter;
import lu.bout.rpg.battler.campaign.chapter.FreeRoamChapter;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.VillageLocation;

public class GoToLocationAction extends StoryAction {

    int locationId;

    // Serializaton constructor
    public GoToLocationAction() {
    }

    public GoToLocationAction(Location location) {
        this.locationId = location.getId();
    }

    public void run(RpgGame game, PlayerParty party) {
        Chapter chapter = game.state.getCurrentChapter();
        if (chapter instanceof FreeRoamChapter) {
            FreeRoamChapter free = (FreeRoamChapter) chapter;
            free.goToLocation(game, locationId);
        } else {
            Gdx.app.log("Game", "Unable to change location to " + locationId + " if not freeroaming");
        }
    }
}
