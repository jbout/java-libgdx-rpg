package lu.bout.rpg.battler.party;

import lu.bout.rpg.engine.character.Character;

public class PlayerCharacter extends Character {

    private String name;

    // to serialize
    public PlayerCharacter() {
    }

    public PlayerCharacter(String playerName) {
        name = playerName;
    }

    @Override
    public int getMaxhp() {
        return 200;
    }
}
