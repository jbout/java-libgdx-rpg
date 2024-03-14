package lu.bout.rpg.engine.system.simplejrpg;

import java.util.ArrayList;
import java.util.List;

import lu.bout.rpg.engine.character.Attribute;
import lu.bout.rpg.engine.character.Beastiarum;
import lu.bout.rpg.engine.character.CharacterSheet;
import lu.bout.rpg.engine.character.CharacterKlass;
import lu.bout.rpg.engine.system.System;
import lu.bout.rpg.engine.system.simplejrpg.character.FighterKlass;
import lu.bout.rpg.engine.system.simplejrpg.character.SimpleFighter;
import lu.bout.rpg.engine.system.simplejrpg.monster.SimpleBeastiarum;

public class SimpleJrpgSystem implements System {

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

    public CharacterSheet getNewPlayerCharacter() {
        SimpleFighter player = new SimpleFighter();
        player.setLevel(8);
        return player;
    }

    public Beastiarum getBeastiarum() {
        return new SimpleBeastiarum();
    }

}
