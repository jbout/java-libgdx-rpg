package lu.bout.rpg.battler.campaign.chapter;

import com.badlogic.gdx.Gdx;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.VillageLocation;
import lu.bout.rpg.battler.world.city.LocationMap;

public class HubChapter extends Chapter {

    public static final HubChapter fromGame(RpgGame game ) {
        Chapter chapter = game.state.getCurrentChapter();
        if (!(chapter instanceof HubChapter)) {
            Gdx.app.log("Game", "Attempt to retrieve FreeRoamChapter while not freeroaming");
            throw new RuntimeException("Not in FreeRoamChapter");
        }
        return (HubChapter) chapter;
    }

    private int currentLocation;
    private LocationMap map;

    public HubChapter() {};

    public HubChapter(String id, LocationMap map, VillageLocation start) {
        this.id = id;
        this.map = map;
        this.currentLocation = start.getId();
    };

    public LocationMap getMap() {
        return map;
    }

    public Location getCurrentLocation() {
        return map.getLocation(currentLocation);
    }

    public void goToLocation(RpgGame game, int locationId) {
        currentLocation = locationId;
        game.getSaveService().update(game.state);
        game.showLocation(map, map.getLocation(currentLocation));
    }
}
