package lu.bout.rpg.battler.world;

import lu.bout.rpg.battler.battle.BattleMini;
import lu.bout.rpg.engine.character.Monster;

public class GameMonster extends Monster implements BattleMini {

    public String texture;

    public GameMonster(int level, String texture) {
        super(level);
        this.texture = texture;
    }

    public String getMiniTextureName() {
        return texture;
    }
}
