package lu.bout.rpg.battler.campaign.chapter;

import com.badlogic.gdx.Gdx;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.LocationMap;

public class FreeRoamChapter extends Chapter {

    public static final FreeRoamChapter fromGame(RpgGame game ) {
        Chapter chapter = game.state.getCurrentChapter();
        if (!(chapter instanceof FreeRoamChapter)) {
            Gdx.app.log("Game", "Attempt to retrieve FreeRoamChapter while not freeroaming");
            throw new RuntimeException("Not in FreeRoamChapter");
        }
        return (FreeRoamChapter) chapter;
    }

    private int currentLocation;
    private LocationMap map;

    public FreeRoamChapter() {};

    public FreeRoamChapter(String id, LocationMap map, Location start) {
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