package lu.bout.rpg.battler.map;

import com.badlogic.gdx.math.MathUtils;

import java.util.Arrays;
import java.util.Collections;
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
        if (nrDeadEnds > 0) {
            tryAddDeadEnd(second);
        } else {
            // we shouldn't add a dead-end so lets try to connect
            if (!tryConnect(second)) {
                // nothing to connect to, dead-end it is
                tryAddDeadEnd(second);
            }
        }
        // overlap a third path with multiple deadends
        Field third = generatePath(start, depth - distance * 2, nrDeadEnds > 2);
        if (third.getConnections().size() == 0) {
            if (!tryConnect(third)) {
                tryAddDeadEnd(third);
            }
        }

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
            // two new path fragments? drop a dead end
            if (withDeadEnds && lastNew == true && map[x][y] ==  null) {
                int continueFromX;
                do {
                    // find a non null path to continue from
                    continueFromX = MathUtils.random(4);
                } while (map[continueFromX][y] == null);
                map[x][y] = new Field(x, y, Field.TYPE_RETURN_FIELD);
                current.connectTo(dir, map[x][y]);
                lastDir = 0;
                lastNew = false;
                current = map[continueFromX][y];
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

    protected boolean tryConnect(Field from) {
        boolean success = false;
        // try every direction in a random order
        Integer[] directions={-1, 0, 1};
        List<Integer> possibleDirections = Arrays.asList(directions);
        Collections.shuffle(possibleDirections);

        for (int dir: possibleDirections) {
            boolean inBounds = (from.getMapPosX() + dir) >= 0 && (from.getMapPosX() + dir) <= 4;
            if (inBounds && map[from.getMapPosX() + dir][from.getMapPosY() + 1] != null) {
                from.connectTo(dir, map[from.getMapPosX() + dir][from.getMapPosY() + 1]);
                success = true;
                break;
            }
        }
        return success;
    }

    /**
     * Try to find a spot to put down a dead-end
     * @param from
     * @return
     */
    protected void tryAddDeadEnd(Field from) {
        // try every direction in a random order
        Integer[] directions={-1, 0, 1};
        List<Integer> possibleDirections = Arrays.asList(directions);
        Collections.shuffle(possibleDirections);

        for (int dir: possibleDirections) {
            boolean inBounds = (from.getMapPosX() + dir) >= 0 && (from.getMapPosX() + dir) <= 4;
            if (inBounds && map[from.getMapPosX() + dir][from.getMapPosY() + 1] == null) {
                Field deadend = new Field(from.getMapPosX() + dir, from.getMapPosY()+1, Field.TYPE_RETURN_FIELD);
                map[from.getMapPosX() + dir][from.getMapPosY() + 1] = deadend;
                from.connectTo(dir, deadend);
                break;
            }
        }
    }
}
