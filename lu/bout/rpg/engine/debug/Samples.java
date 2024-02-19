package lu.bout.rpg.engine.debug;

import lu.bout.rpg.engine.character.Party;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.Encounter;

public abstract class Samples {

    public static Combat getSampleCombat()
    {
        return new Combat(getSampleEncounter());
    }

    public static Encounter getSampleEncounter()
    {
        //PlayerCharacter p1 = new PlayerCharacter("Player", 0 , 3);
        SampleMonster p1 = new SampleMonster("Goblin", 3);
        SampleMonster m2 = new SampleMonster("Hobgoblin", 5);
        System.out.println("Setting up fight between " + p1.toString() + " and " + m2.toString());

        return new Encounter(new Party(p1), new Party(m2), Encounter.TYPE_BALANCED);
    }
/*
    public static Encounter getSampleEncounter(Character player)
    {
        Party monsterParty = new Party(new Monster(3));
        monsterParty.getMembers().add(new Monster(5));
        return new Encounter(new Party(player), monsterParty, Encounter.TYPE_BALANCED);
    }

 */
}
