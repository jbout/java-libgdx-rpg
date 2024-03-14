package lu.bout.rpg.battler.battle.map;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

import lu.bout.rpg.engine.character.CharacterSheet;
import lu.bout.rpg.vocabulary.Animal;

public class MonsterArt {
    private static final String FALLBACK = "enemy/pipo-enemy001.png";

    private static final HashMap<String, String> LOOKUP;

    static {
        LOOKUP = new HashMap<>();
        LOOKUP.put(Animal.BAT, "enemy/pipo-enemy001.png");
        LOOKUP.put(Animal.BEETLE, "enemy/pipo-enemy004.png");
        LOOKUP.put(Animal.SNAKE, "enemy/pipo-enemy003.png");
        LOOKUP.put(Animal.WULF, "enemy/pipo-enemy002.png");
        LOOKUP.put(Animal.GRIFFIN, "enemy/pipo-enemy022.png");
    };

    private static final HashMap<String, Texture> cache = new HashMap<>();

    public static Texture getBestSpriteTexture(CharacterSheet characterSheet) {
        Texture texture = null;
        for (String tag: characterSheet.getTags()) {
            if (LOOKUP.containsKey(tag)) {
                texture = new Texture(LOOKUP.get(tag));
                break;
            }
        }
        if (texture == null) {
            texture = new Texture(FALLBACK);
        }
        return texture;
    }
}
