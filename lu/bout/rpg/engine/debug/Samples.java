package lu.bout.rpg.engine.debug;

import lu.bout.rpg.engine.character.Party;
import lu.bout.rpg.engine.character.Player;
import lu.bout.rpg.engine.character.Monster;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.Encounter;

public abstract class Samples {

    public static Combat getSampleCombat()
    {
        return new Combat(getSampleEncounter());
    }

    public static Encounter getSampleEncounter()
    {
        SampleMonster m1 = new SampleMonster("Goblin", 3);
        SampleMonster m2 = new SampleMonster("Hobgoblin", 5);
        System.out.println("Setting up fight between " + m1.getName() + " and " + m2.getName());

        return new Encounter(new Party(m1), new Party(m2), Encounter.TYPE_BALANCED);
    }

    public static Encounter getSampleEncounter(Player player)
    {
        Party monsterParty = new Party(new Monster(3));
        monsterParty.getMembers().add(new Monster(5));
        return new Encounter(new Party(player), monsterParty, Encounter.TYPE_BALANCED);
    }
}
