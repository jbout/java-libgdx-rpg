package lu.bout.rpg.battler.map;

import com.badlogic.gdx.math.MathUtils;

import java.util.Arrays;
import java.util.List;

public class MapFactory {

    int width;
    int depth;
    int nrDeadEnds;

    Field start;
    Field[][] map;

    public MapFactory(int depth, int nrDeadEnds) {
        this.width = 5;
        this.depth = depth;
        this.nrDeadEnds = nrDeadEnds;
        start = new Field(2, 0);
        map = new Field[width][depth+1];
    }

    public DungeonMap generate() {
        int distance = depth / 5;
        // generate the correct path
        Field goal = generatePath(start, depth, false);
        goal.connectTo(0, new Field(goal.getMapPosX(), goal.getMapPosY()+1, Field.TYPE_FINISH));
        // overlap a second path with a dead-end
        Field second = generatePath(start, depth - distance, false);
        tryAddDeadEnd(second);
        // overlap a third path with multiple deadends
        Field third = generatePath(start, depth - distance * 2, true);

        return new DungeonMap(start, depth + 1);
    }

    protected Field generatePath(Field from, int steps, boolean withDeadEnds) {
        Field current = start;
        int lastDir = 0;
        boolean lastNew = false;
        for (int i = 0; i < steps; i++) {
            int x, dir;
            int y = current.getMapPosY() + 1;
            // to prevent ugly left/right turns
            boolean isTriangle;
            do {
                dir = MathUtils.random(2) - 1;
                x = current.getMapPosX() + dir;
                isTriangle = ((lastDir == -1 && dir == 1) || (lastDir == 1 && dir == -1));
            } while (isTriangle || x < 0 || x >= 5);
            if (withDeadEnds && lastNew == true && map[x][y] ==  null) {
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
                    generateRandomField(x,y);
                    lastNew = true;
                } else {
                    lastNew = false;
                }
                current.connectTo(dir, map[x][y]);
                current = map[x][y];
                lastDir = dir;
            }
        }
        return current;
    }

    protected Field generateRandomField(int x, int y) {
        float randomtype = MathUtils.random();
        int type = Field.TYPE_EMPTY; // 40%
        if (randomtype < 0.1f && y > 3) {
            type = Field.TYPE_TREASURE; // 10 %
        } else {
            if (randomtype > 0.5f) {
                type = Field.TYPE_MONSTER; // 50%
            }
        }
        map[x][y] = new Field(x, y, type);
        return map[x][y];
    }

    protected Field tryAddDeadEnd(Field from) {
        Integer[] directions={-1, 0, 1};
        List<Integer> possibleDirections = Arrays.asList(directions);

        int emptyDir = 99;
        for (int dir: possibleDirections) {
            boolean inBounds = (from.getMapPosX() + dir) >= 0 && (from.getMapPosX() + dir) <= 4;
            if (inBounds && map[from.getMapPosX() + dir][from.getMapPosY() + 1] == null) {
                emptyDir = dir;
                break;
            }
        }
        Field deadend = null;
        if (emptyDir != 99) {
            deadend = new Field(from.getMapPosX() + emptyDir, from.getMapPosY()+1, Field.TYPE_RETURN_FIELD);
            map[from.getMapPosX() + emptyDir][from.getMapPosY() + 1] = deadend;
            from.connectTo(emptyDir, deadend);
        }
        return deadend;
    }
}
