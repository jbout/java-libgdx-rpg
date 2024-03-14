package lu.bout.rpg.battler.party;

import lu.bout.rpg.battler.battle.map.CombatMini;
import lu.bout.rpg.engine.character.CharacterSheet;

public class Person implements CombatMini {

    private String name;

    private int portraitId;

    private String miniName = "enemy/sample-hero.png";

    private CharacterSheet characterSheet;

    // to serialize
    public Person() {
    }

    public Person(String playerName, int portraitId, CharacterSheet characterSheet) {
        name = playerName;
        this.portraitId = portraitId;
        this.characterSheet = characterSheet;
    }

    public String getName() {
        return name;
    }

    public int getPortaitId() {
        return portraitId;
    }

    public void setBattleMini(String textureName) {
        miniName = textureName;
    }

    public String getMiniTextureName() {
        return miniName;
    }

    public CharacterSheet getCharacter() {
        return characterSheet;
    }
}
