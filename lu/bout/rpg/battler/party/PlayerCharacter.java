package lu.bout.rpg.battler.party;

import lu.bout.rpg.battler.battle.map.CombatMini;
import lu.bout.rpg.engine.system.simplejrpg.SampleFighter;

public class PlayerCharacter extends SampleFighter implements CombatMini {

    private String name;

    private int portraitId;

    private String miniName = "enemy/sample-hero.png";

    // to serialize
    public PlayerCharacter() {
    }

    public PlayerCharacter(String playerName, int portraitId, int level) {
        name = playerName;
        this.portraitId = portraitId;
        this.setLevel(level);
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
}
