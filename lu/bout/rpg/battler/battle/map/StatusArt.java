package lu.bout.rpg.battler.battle.map;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

import lu.bout.rpg.engine.Taggable;
import lu.bout.rpg.engine.combat.status.CombatStatus;
import lu.bout.rpg.vocabulary.Status;

public class StatusArt {
    private static final String FALLBACK = "status_fallback.png";

    private static final HashMap<String, String> LOOKUP;

    static {
        LOOKUP = new HashMap<>();
        LOOKUP.put(Status.BRITTLE, "status_brittle.png");
        LOOKUP.put(Status.WEAKEN, "status_down.png");
        LOOKUP.put(Status.BLEED, "status_bleed.png");
        LOOKUP.put(Status.STRENGTHEN, "status_up.png");
    };

    public static Texture getBestSpriteTexture(CombatStatus status) {
        Texture texture = null;
        if (status instanceof Taggable){
            for (String tag : ((Taggable)status).getTags()) {
                if (LOOKUP.containsKey(tag)) {
                    texture = new Texture(LOOKUP.get(tag));
                    break;
                }
            }
        }
        if (texture == null) {
            texture = new Texture(FALLBACK);
        }
        return texture;
    }
}
