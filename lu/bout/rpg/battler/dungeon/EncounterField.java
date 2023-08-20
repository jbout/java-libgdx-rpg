package lu.bout.rpg.battler.dungeon;

import lu.bout.rpg.engine.character.Party;

public class EncounterField extends Field{

    private Party enemies;

    // for serialization purpose only
    public EncounterField() {
    }

    public EncounterField(int x, int y, Party enemies){
        super(x,y,Field.TYPE_MONSTER);
        this.enemies = enemies;
    }

    public Party getEnemiesParty() {
        return enemies;
    }
}
