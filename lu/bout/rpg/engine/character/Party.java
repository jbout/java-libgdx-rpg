package lu.bout.rpg.engine.character;

import java.util.LinkedList;

public class Party {

    // for serialization
    public Party() {
    }

    protected LinkedList<Character> members;

    public Party(Character member)
    {
        members = new LinkedList<Character>();
        members.add(member);
    }

    public Party(LinkedList<Character> members)
    {
        this.members = members;
    }

    public LinkedList<Character> getMembers()
    {
        return members;
    }

    public void addMember(Character character)
    {
        members.add(character);
    }
}
