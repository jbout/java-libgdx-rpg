package lu.bout.rpg.battler.party;

import java.util.LinkedList;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.character.Party;

public class PlayerParty extends Party {

    Inventory partyInventory;

    // for serialization
    public PlayerParty() {
        super();
    }

    public PlayerParty(PlayerCharacter player) {
        super(player);
    }

    public PlayerCharacter getPlayerCharacter() {
        return (PlayerCharacter)members.getFirst();
    }
}
