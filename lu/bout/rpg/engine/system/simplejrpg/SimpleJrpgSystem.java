package lu.bout.rpg.engine.system.simplejrpg;

import java.util.ArrayList;
import java.util.List;

import lu.bout.rpg.engine.system.Attribute;
import lu.bout.rpg.engine.system.CharacterKlass;
import lu.bout.rpg.engine.system.System;

public class SimpleJrpgSystem extends System {

    public List<Attribute> getAttributes() {
        List<Attribute> attributes = new ArrayList();
        attributes.add(new Attribute("Strength", "str"));
        attributes.add(new Attribute("Constitution", "con"));
        return attributes;
    }

    public List<CharacterKlass> getClasses() {
        List<CharacterKlass> classes = new ArrayList();
        classes.add(new FighterKlass());
        return classes;
    }

    public boolean supportsMultiClass() {
        return false;
    }

}
