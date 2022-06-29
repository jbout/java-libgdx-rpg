package lu.bout.rpg.battler.world;

import lu.bout.rpg.character.Monster;

public class GameMonster extends Monster {

    public String texture;

    public GameMonster(int level, String texture) {
        super(level);
        this.texture = texture;
    }
}
