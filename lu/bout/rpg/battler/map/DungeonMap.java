package lu.bout.rpg.battler.map;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.LinkedList;

public class DungeonMap {

    private int depth;
    private HashMap<String, Field> fields;
    private String start;

    public DungeonMap() {
    }

    public DungeonMap(Field start, int depth) {
        this.start = start.getMapPos();
        this.depth = depth;
        fields = new HashMap<>();
        addField(start);
    }

    public void addField(Field newField) {
        fields.put(newField.getMapPos(), newField);
    }

    public LinkedList<Field> getAllFields() {
        LinkedList<Field> fields = new LinkedList<Field>();
        LinkedList<Field> todo = new LinkedList<Field>();
        todo.add(getStart());
        while (todo.size() > 0) {
            Field f = todo.pop();
            fields.add(f);
            for (Connection c : f.getConnections()) {
                todo.add(getField(c.getDestination()));
            }
        }
        return fields;
    }

    public Field getField(String pos) {
        return fields.get(pos);
    }

    public Field getStart() {
        return getField(start);
    }

    public int getDepth() {
        return depth;
    }
}