package lu.bout.rpg.battler.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;

import lu.bout.rpg.character.Monster;

public class Beastiarum {

    final static String monsterjson = "{\n" +
            "  \"templates\" : [\n" +
            "    {\n" +
            "      \"name\" : \"bat\",\n" +
            "      \"level\" : 2,\n" +
            "      \"texture\" : \"enemy/pipo-enemy001.png\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\" : \"beetle\",\n" +
            "      \"level\" : 4,\n" +
            "      \"texture\" : \"enemy/pipo-enemy004.png\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\" : \"snake\",\n" +
            "      \"level\" : 6,\n" +
            "      \"texture\" : \"enemy/pipo-enemy003.png\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\" : \"wulf\",\n" +
            "      \"level\" : 8,\n" +
            "      \"texture\" : \"enemy/pipo-enemy002.png\"\n" +
            "    }\n" +
            "  ]\n" +
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
