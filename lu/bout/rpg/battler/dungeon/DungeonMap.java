package lu.bout.rpg.battler.dungeon;

import java.util.HashMap;
import java.util.LinkedList;

public class DungeonMap {

    private int depth;
    private HashMap<String, Field> fields;
    private String startId;

    /**
     * Used by serializer/deserializer
     */
    public DungeonMap() {
    }

    public DungeonMap(Field start, int depth) {
        this.startId = start.getMapPos();
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
        return getField(startId);
    }

    public int getDepth() {
        return depth;
    }
}