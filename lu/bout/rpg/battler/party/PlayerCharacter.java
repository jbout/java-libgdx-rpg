package lu.bout.rpg.battler.party;

import lu.bout.rpg.battler.battle.BattleMini;
import lu.bout.rpg.engine.character.Character;

public class PlayerCharacter extends Character implements BattleMini {

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

    public String getTextureName() {
        return "enemy/sample-hero.png";
    }

    @Override
    public int getMaxhp() {
        return 200;
    }
}
