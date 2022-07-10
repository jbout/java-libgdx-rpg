package lu.bout.rpg.battler.map;

public class Connection {
    public static final int LEFT = -1;
    public static final int STRAIGHT = 0;
    public static final int RIGHT = +1;

    private Field destination;
    private int direction;

    // for json serialization
    public Connection() {
    }

    public Connection(int direction, Field destination) {
        this.direction = direction;
        this.destination = destination;
    }

    public int getDirection() {
        return direction;
    }

    public Field getDestination() {
        return destination;
    }
}
