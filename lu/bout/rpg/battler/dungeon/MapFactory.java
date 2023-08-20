package lu.bout.rpg.battler.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapFactory {

    EncounterFactory encounterFactory;
    int width;
    int depth;
    int nrDeadEnds;
    int minlvl;
    int maxlvl;

    Field start;
    Field[][] map;
    DungeonMap dungeonMap;

    public MapFactory(EncounterFactory e, int depth, int nrDeadEnds, int minlvlEnemies, int maxlvlEnemies) {
        Gdx.app.log("Game", "Building dungeon with enemies " + minlvlEnemies + "-" + maxlvlEnemies);
        this.encounterFactory = e;
        this.width = 5;
        this.depth = depth;
        this.nrDeadEnds = nrDeadEnds;
        minlvl = minlvlEnemies;
        maxlvl = maxlvlEnemies;
        start = new Field(2, 0);
        map = new Field[width][depth+1];
    }

    public DungeonMap generate() {
        if (dungeonMap == null) {
            int distance = depth / 5;
            dungeonMap = new DungeonMap(start, depth + 1);
            // generate the correct path
            Field endOfPath = generatePath(start, depth-1, false);
            endOfPath.connectTo(0, createField(endOfPath, 0, Field.TYPE_FINISH));
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
        }
        return dungeonMap;
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
                current.connectTo(dir, createField(x, y, Field.TYPE_RETURN_FIELD));
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
        return createField(x, y, type);
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
                from.connectTo(dir, createField(from, dir, Field.TYPE_RETURN_FIELD));
                break;
            }
        }
    }

    protected Field createField(Field from, int dir, int type) {
        return createField(from.getMapPosX() + dir, from.getMapPosY()+1, type);
    }

    protected Field createField(int x, int y, int type) {
        int lvl = minlvl + Math.round(((float)y / depth) * (maxlvl-minlvl));
        Field newField = type == Field.TYPE_MONSTER
                ? new EncounterField(x, y, encounterFactory.generateEnemyParty(lvl))
                : new Field(x, y, type);
        addField(newField);
        return newField;
    }

    protected void addField(Field field) {
        map[field.getMapPosX()][field.getMapPosY()] = field;
        dungeonMap.addField(field);
    }
}
