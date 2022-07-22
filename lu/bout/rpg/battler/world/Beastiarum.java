package lu.bout.rpg.battler.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;

import lu.bout.rpg.engine.character.Monster;

public class Beastiarum {

    final static String monsterjson = "{" +
            "  \"templates\" : [" +
            "    {" +
            "      \"name\" : \"bat\"," +
            "      \"level\" : 2," +
            "      \"texture\" : \"enemy/pipo-enemy001.png\"" +
            "    }," +
            "    {" +
            "      \"name\" : \"beetle\"," +
            "      \"level\" : 4," +
            "      \"texture\" : \"enemy/pipo-enemy004.png\"" +
            "    }," +
            "    {" +
            "      \"name\" : \"snake\"," +
            "      \"level\" : 6," +
            "      \"texture\" : \"enemy/pipo-enemy003.png\"" +
            "    }," +
            "    {" +
            "      \"name\" : \"wulf\"," +
            "      \"level\" : 8," +
            "      \"texture\" : \"enemy/pipo-enemy002.png\"" +
            "    }" +
            "  ]" +
            "}";

    MonsterTemplate[] templates;

    public static Beastiarum getInstance() {
        Json json = new Json();
        Beastiarum beastiarum = json.fromJson(Beastiarum.class, monsterjson);
        return beastiarum;
    }

    public Monster getRandomMonster() {
        MonsterTemplate template = templates[MathUtils.random(templates.length - 1)];
        return template.generateMonster();
    }
}
