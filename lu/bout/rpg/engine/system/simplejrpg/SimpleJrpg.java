package lu.bout.rpg.engine.system.simplejrpg;

import java.util.ArrayList;
import java.util.List;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.system.Attribute;
import lu.bout.rpg.engine.system.CharacterClass;
import lu.bout.rpg.engine.system.System;

public class SimpleJrpg extends System {

    public List<Attribute> getAttributes() {
        List<Attribute> attributes = new ArrayList();
        attributes.add(new Attribute("Strength", "str"));
        attributes.add(new Attribute("Constitution", "con"));
        return attributes;
    }

    public List<CharacterClass> getClasses() {
        List<CharacterClass> classes = new ArrayList();
        classes.add(new FighterClass());
        return classes;
    }

    public boolean supportsMultiClass() {
        return false;
    }

    public Character getLvl10Fighter()
    {
        return new Character() {
            @Override
            public int getMaxhp() {
                return 0;
            }
        };
    }
}
