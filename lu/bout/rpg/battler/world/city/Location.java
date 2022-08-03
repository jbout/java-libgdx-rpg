package lu.bout.rpg.battler.world.city;

import java.util.LinkedList;

import lu.bout.rpg.battler.RpgGame;

public class Location {

    public String name;
    int id;

    public int getId() {
        return id;
    }

    public void enter(RpgGame game, LocationMap map) {
        game.showLocation(map, this);
    }
}
