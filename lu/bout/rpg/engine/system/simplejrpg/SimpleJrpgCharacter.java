package lu.bout.rpg.engine.system.simplejrpg;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.system.Attribute;
import lu.bout.rpg.engine.system.CharacterClass;

public class SimpleJrpgCharacter extends Character {

    private CharacterClass klass;
    private int level;

    // equipment

    // stats
    private int strength;
    private int constitution;

    private int getStat(Attribute attribute) {
        return 0;
    }

    @Override
    public int getMaxhp() {
        return 0;
    }
}
