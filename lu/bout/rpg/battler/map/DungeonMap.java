package lu.bout.rpg.battler.map;

import com.badlogic.gdx.math.MathUtils;

import java.util.LinkedList;

public class DungeonMap {

    Field start;

    public static DungeonMap generateMap(int depth, int deadends) {
        Field start = new Field(2, 0);
        Field[][] map = new Field[5][depth+1];
        // finish is also a deadend
        int averageDeadEndDistance = depth / (deadends + 1);
        for (int path = 0; path < 3; path++) {
            Field current = start;
            int lastDir = 0;
            boolean lastNew = false;
            for (int i = 0; i < (depth - (path * averageDeadEndDistance)); i++) {
                int x, dir;
                int y = current.getMapPosY() + 1;
                // to prevent ugly left/right turns
                boolean isTriangle;
                do {
                    dir = MathUtils.random(2) - 1;
                    x = current.getMapPosX() + dir;
                    isTriangle = ((lastDir == -1 && dir == 1) || (lastDir == 1 && dir == -1));
                } while (isTriangle || x < 0 || x >= 5);
                if (path == 3 && lastNew == true && map[x][y] ==  null) {
                    // drop a dead end and continue from path
                    int existingX;
                    do {
                        existingX = MathUtils.random(4);
                    } while (map[existingX][y] == null);
                    map[x][y] = new Field(x, y, Field.TYPE_RETURN_FIELD);
                    current.connectTo(dir, map[x][y]);
                    lastDir = 0;
                    lastNew = false;
                    current = map[existingX][y];
                } else {
                    if (map[x][y] ==  null) {
                        map[x][y] = new Field(x, y, MathUtils.random() >= 0.5f ? Field.TYPE_EMPTY : Field.TYPE_MONSTER);
                        lastNew = true;
                    } else {
                        lastNew = false;
                    }
                    current.connectTo(dir, map[x][y]);
                    current = map[x][y];
                    lastDir = dir;
                }
            }
            current.connectTo(0, new Field(current.getMapPosX(), current.getMapPosY() + 1, Field.TYPE_FINISH));
        }
        return new DungeonMap(start);
    }

    public static DungeonMap createMap() {
        Field start = new Field(2, 0);
        start.connect(Connection.LEFT).connect(Connection.LEFT).connect(Connection.STRAIGHT);
        Field inter = start.connect(Connection.STRAIGHT).connect(Connection.STRAIGHT).connect(Connection.STRAIGHT);
        inter.connect(Connection.STRAIGHT).connect(Connection.STRAIGHT);
        inter.connect(Connection.RIGHT).connect(Connection.STRAIGHT).connect(Connection.RIGHT).connect(Connection.STRAIGHT);
        return new DungeonMap(start);
    }

    public DungeonMap(Field start) {
        this.start = start;
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
}
