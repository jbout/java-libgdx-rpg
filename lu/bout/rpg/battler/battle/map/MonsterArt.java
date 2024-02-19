package lu.bout.rpg.battler.battle.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

import lu.bout.rpg.engine.character.Character;
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

    public static Texture getBestSpriteTexture(Character character) {
        String ref = FALLBACK;
        Gdx.app.log("Game", "Get Sprite for " + character.getLevel());
        for (String tag: character.getTags()) {
            Gdx.app.log("Game", "Checking Tag " + tag);
            if (LOOKUP.containsKey(tag)) {
                Gdx.app.log("Game", "Accepted Tag " + tag);
                ref = LOOKUP.get(tag);
                break;
            }
        }
        if (!cache.containsKey(ref)) {
            cache.put(ref, new Texture(ref));
        }
        return cache.get(ref);
    }
}
