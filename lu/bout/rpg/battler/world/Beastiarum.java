package lu.bout.rpg.battler.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;

import java.util.Collections;
import java.util.LinkedList;

import lu.bout.rpg.engine.character.Monster;

public class Beastiarum {

    final static String monsterjson = "{" +
            "  \"templates\" : [" +
            "    {" +
            "      \"name\" : \"bat\"," +
            "      \"minlevel\" : 1," +
            "      \"maxlevel\" : 3," +
            "      \"texture\" : \"enemy/pipo-enemy001.png\"" +
            "    }," +
            "    {" +
            "      \"name\" : \"beetle\"," +
            "      \"minlevel\" : 3," +
            "      \"maxlevel\" : 5," +
            "      \"texture\" : \"enemy/pipo-enemy004.png\"" +
            "    }," +
            "    {" +
            "      \"name\" : \"snake\"," +
            "      \"minlevel\" : 5," +
            "      \"maxlevel\" : 7," +
            "      \"texture\" : \"enemy/pipo-enemy003.png\"" +
            "    }," +
            "    {" +
            "      \"name\" : \"wulf\"," +
            "      \"minlevel\" : 7," +
            "      \"maxlevel\" : 9," +
            "      \"texture\" : \"enemy/pipo-enemy002.png\"" +
            "    }," +
            "    {" +
            "      \"name\" : \"griffin\"," +
            "      \"minlevel\" : 10," +
            "      \"maxlevel\" : 20," +
            "      \"texture\" : \"enemy/pipo-enemy022.png\"" +
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

    public MonsterTemplate getTemplateById(String id) {
        for (MonsterTemplate template: templates) {
            if (template.getId() == id) {
                return template;
            }
        }
        return null;
    }

    public Monster getMonsterByLevel(int level) {
        LinkedList<MonsterTemplate> candidates = new LinkedList<>();
        for (int i = 0; i < templates.length; i++) {
            if (level >= templates[i].getBoundaries()[0] && level <= templates[i].getBoundaries()[1]) {
                candidates.add(templates[i]);
            }
        }
        Collections.shuffle(candidates);
        return candidates.pop().generateMonster(level);
    }
}
