package lu.bout.rpg.battler.party;

import java.util.LinkedList;

import lu.bout.rpg.engine.character.CharacterSheet;
import lu.bout.rpg.engine.character.Party;

public class PlayerParty extends Party {

    Inventory partyInventory;

    private Person player;

    private LinkedList<Person> npcs = new LinkedList<>();

    // for serialization
    public PlayerParty() {
        super();
    }

    public PlayerParty(Person player) {
        super(player.getCharacter());
        this.player = player;
    }

    public Person getPlayer() {
        return player;
    }

    public void addNpc(Person person) {
        this.npcs.add(person);
        this.addMember(person.getCharacter());
    }

    public Person getPerson(CharacterSheet characterSheet) {
        if (player.getCharacter() == characterSheet) {
            return player;
        }
        for (Person p: npcs) {
            if (p.getCharacter().equals(characterSheet)) {
                return p;
            }
        }
        return null;
    }

    public Iterable<Person> getPersons() {
        LinkedList<Person> persons = new LinkedList<>();
        persons.add(player);
        persons.addAll(npcs);
        return persons;
    }

}
