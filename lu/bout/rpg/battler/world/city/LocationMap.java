package lu.bout.rpg.battler.world.city;

import java.util.HashMap;
import java.util.LinkedList;

public class LocationMap {

    // Integer gets transformed to string during serialization
    private HashMap<String, Location> locations = new HashMap<>();
    private HashMap<String, Integer[]> connections = new HashMap<>();

    public Location getLocation(int id) {
        return locations.get("" + id);
    }

    public void addLocation(Location location) {
        location.id = locations.size();
        locations.put("" + location.id, location);
    }

    public void doubleLink(Location from, Location to) {
        linkLocations(from, to);
        linkLocations(to, from);
    }

    public void linkLocations(Location from, Location to) {
        Integer[] oldConnect = connections.containsKey("" + from.id) ? connections.get("" + from.id) : new Integer[0];
        Integer[] newConnect = new Integer[oldConnect.length + 1];
        System.arraycopy(oldConnect, 0, newConnect, 0, oldConnect.length);
        newConnect[newConnect.length-1] = to.id;
        connections.put("" + from.id, newConnect);
    }

    public LinkedList<Location> getConnections(Location location) {
        LinkedList<Location> destinations = new LinkedList<>();
        for (int id: connections.get("" + location.id)) {
            destinations.add(getLocation(id));
        }
        return destinations;
    }
}
