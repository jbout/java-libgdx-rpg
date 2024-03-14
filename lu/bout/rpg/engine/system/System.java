package lu.bout.rpg.engine.system;

import lu.bout.rpg.engine.character.Beastiarum;
import lu.bout.rpg.engine.character.CharacterSheet;

public interface System {
    public CharacterSheet getNewPlayerCharacter();

    public Beastiarum getBeastiarum();
}
