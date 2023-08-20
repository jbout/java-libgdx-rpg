package lu.bout.rpg.battler.dungeon;

public class Connection {
    public static final int LEFT = -1;
    public static final int STRAIGHT = 0;
    public static final int RIGHT = +1;

    private String destination;
    private int direction;

    // for json serialization
    public Connection() {
    }

    public Connection(int direction, Field destination) {
        this.direction = direction;
        this.destination = destination.getMapPos();
    }

    public int getDirection() {
        return direction;
    }

    public String getDestination() {
        return destination;
    }
}
