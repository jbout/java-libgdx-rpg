package lu.bout.rpg.battler.battle.minigame.japanese;

import java.util.HashSet;

public class Challenge {

    private String prompt;
    private String answer;
    private char[][] map;

    public Challenge(String prompt, String answer, char[][] map) {
        this.prompt = prompt;
        this.answer = answer;
        this.map = map;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getAnswer() {
        return answer;
    }

    public char[][] getMap() {
        return map;
    }

    public HashSet<String> getUsedCharacters() {
        HashSet<String> characters= new HashSet<>();
        for (int i = 0; i < prompt.length(); i++) {
            characters.add(prompt.substring(i,i+1));
        }
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                characters.add(String.valueOf(map[x][y]));
            }
        }
        return characters;
    }
}
