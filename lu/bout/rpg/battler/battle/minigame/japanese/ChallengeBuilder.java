package lu.bout.rpg.battler.battle.minigame.japanese;

import java.util.Random;

public class ChallengeBuilder {

    private static final String[] WORDS = {"関係","自転車","自動車","修理","出張","条件","先週","洗濯機","必要","便利","掃除機"};
    private static final char[] KANJI = {'壊','穴','借','拾','貸','着','破','売','服','変','返','用','弟','飽','周','履'};
    private static final int[][] DIRECTION = {
            {-1, 0},
            {-1, -1},
            {0, -1},
            {1, -1},
            {1, 0},
            {1, 1},
            {0, 1},
            {-1, 1}
    };
    private static Random random = new Random();

    public static Challenge buildChallenge() {
        int sizeX = 5;
        int sizeY = 3;
        String correct = WORDS[random.nextInt(WORDS.length)];
        char[][] map = new char[sizeX][sizeY];
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                map[x][y] = KANJI[random.nextInt(KANJI.length)];
            }
        }
        for (int i = 0; i < map.length; i++) {
            addWord(map, WORDS[random.nextInt(WORDS.length)]);
        }
        addWord(map, correct);

        return new Challenge("search " + correct, correct, map);
    }

    private static void addWord(char[][] map, String word) {
        boolean found = false;
        do {
            int randX = random.nextInt(map.length);
            int randY = random.nextInt(map[0].length);
            int[] dir = DIRECTION[random.nextInt(8)];
            int endX =  randX + (dir[0] * word.length());
            int endY =  randY + (dir[1] * word.length());
            if (endX >= 0 && endX < map.length && endY >= 0 && endY < map[0].length) {
                for (int i = 0; i < word.length(); i++) {
                    map[randX + (i*dir[0])][randY + (i*dir[1])] = word.charAt(i);
                }
                found = true;
            }
        } while (!found);
    }

}
