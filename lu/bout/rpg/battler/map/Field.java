package lu.bout.rpg.battler.map;

import java.util.LinkedList;

public class Field {

    public final static int TYPE_EMPTY = 0;
    public final static int TYPE_MONSTER = 1;
    public final static int TYPE_TREASURE = 2;
    public final static int TYPE_RETURN_FIELD = 3;
    public final static int TYPE_FINISH = 4;

    int posX, posY;
    LinkedList<Connection> connections;
    int type = TYPE_EMPTY;

    boolean isOpen = false;

    public Field(int x, int y) {
        this(x,y,TYPE_EMPTY);
    }

    public Field(int x, int y, int type) {
        posX = x;
        posY = y;
        this.type = type;
        connections = new LinkedList<Connection>();
    }

    public Field connect(int direction) {
        Field f = new Field(posX + direction, posY + 1);
        connections.add(new Connection(direction, f));
        return f;
    }

    public void connectTo(int direction, Field f) {
        connections.add(new Connection(direction, f));
    }


    public LinkedList<Connection> getConnections() {
        return  connections;
    }

    public int getMapPosX() {
        return posX;
    }

    public int getMapPosY() {
        return posY;
    }

    public int getType() {
        return type;
    }


    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        isOpen = true;
    }

}
