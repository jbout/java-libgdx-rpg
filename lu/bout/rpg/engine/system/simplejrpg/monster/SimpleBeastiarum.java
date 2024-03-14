package lu.bout.rpg.engine.system.simplejrpg.monster;

import com.badlogic.gdx.math.MathUtils;

import java.util.Collections;
import java.util.LinkedList;

import lu.bout.rpg.engine.character.Beastiarum;
import lu.bout.rpg.vocabulary.Animal;

public class SimpleBeastiarum implements Beastiarum {

    private MonsterTemplate[] templates;

    public SimpleBeastiarum() {
        templates = new MonsterTemplate[] {
                new MonsterTemplate(1,3, new String[] {Animal.BAT}),
                new MonsterTemplate(3,5, new String[] {Animal.BEETLE}),
                new MonsterTemplate(5,7, new String[] {Animal.SNAKE}),
                new MonsterTemplate(7,9, new String[] {Animal.WULF}),
                new MonsterTemplate(10,20, new String[] {Animal.GRIFFIN}),
        };
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
