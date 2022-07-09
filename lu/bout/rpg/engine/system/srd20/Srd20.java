package lu.bout.rpg.engine.system.srd20;

import java.util.ArrayList;
import java.util.List;

import lu.bout.rpg.engine.system.Attribute;
import lu.bout.rpg.engine.system.CharacterClass;
import lu.bout.rpg.engine.system.System;

public class Srd20 extends System {

    public List<Attribute> getAttributes() {
        List<Attribute> attributes = new ArrayList();
        attributes.add(new Attribute("Strength", "str"));
        attributes.add(new Attribute("Dexterity", "dex"));
        attributes.add(new Attribute("Constitution", "con"));
        attributes.add(new Attribute("Intelligence", "int"));
        attributes.add(new Attribute("Wisdom", "wis"));
        attributes.add(new Attribute("Charisma", "cha"));
        return attributes;
    }

    public List<CharacterClass> getClasses() {
        List<CharacterClass> classes = new ArrayList();
        return classes;
    }

    public boolean supportsMultiClass() {
        return true;
    }
}
