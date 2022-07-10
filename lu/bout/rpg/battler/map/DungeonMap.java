package lu.bout.rpg.battler.map;

import java.util.LinkedList;

public class DungeonMap {

    Field start;
    int depth;

    public DungeonMap() {
    }

    public DungeonMap(Field start, int depth) {
        this.start = start;
        this.depth = depth;
    }

    public LinkedList<Field> getAllFields() {
        LinkedList<Field> fields = new LinkedList<Field>();
        LinkedList<Field> todo = new LinkedList<Field>();
        todo.add(start);
        while (todo.size() > 0) {
            Field f = todo.pop();
            fields.add(f);
            for (Connection c: f.getConnections()) {
                todo.add(c.getDestination());
            }
        }
        return fields;
    }

    public Field getStart() {
        return start;
    }

    public int getDepth() {
        return depth;
    }
}
