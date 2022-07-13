package lu.bout.rpg.battler.party;

import lu.bout.rpg.engine.character.Character;

public class PlayerCharacter extends Character {

    private String name;

    private int portraitId;

    // to serialize
    public PlayerCharacter() {
    }

    public PlayerCharacter(String playerName, int portraitId) {
        name = playerName;
        this.portraitId = portraitId;
    }

    public String getName() {
        return name;
    }

    public int getPortaitId() {
        return portraitId;
    }

    @Override
    public int getMaxhp() {
        return 200;
    }
}
