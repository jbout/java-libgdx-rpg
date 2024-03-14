package lu.bout.rpg.engine.character;

import java.util.LinkedList;

public class Party {

    // for serialization
    public Party() {
    }

    protected LinkedList<CharacterSheet> members = new LinkedList<CharacterSheet>();

    public Party(CharacterSheet member)
    {
        members.add(member);
    }

    public Party(LinkedList<CharacterSheet> members)
    {
        this.members = members;
    }

    public LinkedList<CharacterSheet> getMembers()
    {
        return members;
    }

    public void addMember(CharacterSheet characterSheet)
    {
        members.add(characterSheet);
    }
}
