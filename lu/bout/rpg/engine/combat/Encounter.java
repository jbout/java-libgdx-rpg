package lu.bout.rpg.engine.combat;

import java.util.HashMap;

import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.engine.character.Party;

public class Encounter {

    // event initiated fight
    public final static int TYPE_EVENT = 0;
    // player spotted enemy and engages with advantage
    public final static int TYPE_ADVANTAGE = 1;
    // player and enemy spotted each other at the same time
    public final static int TYPE_BALANCED = 1;
    // enemy spotted player first, player is engaged by enemy
    public final static int TYPE_DISADVANTAGE = 3;
    // enemy prepared an ambush given a severe disadvantage
    public final static int TYPE_AMBUSH = 4;

    private Party playerParty;
    private Party opponents;
    private int encounterType;

    public Encounter(Party primaryParty, Party opponents, int type) {
        this.playerParty = primaryParty;
        this.opponents = opponents;
        encounterType = type;
    }

    public HashMap<Integer, Party> getParties() {
        HashMap<Integer, Party> parties = new HashMap<>();
        parties.put(Integer.valueOf(Combat.TEAM_PLAYER), playerParty);
        parties.put(Integer.valueOf(Combat.TEAM_ENEMY), opponents);
        return parties;
    }

    public Party getOpponentParty() {
        return opponents;
    }

    public int getEncounterType() {
        return encounterType;
    }
}
