package lu.bout.rpg.engine.system.simplejrpg.status.negative;

import java.util.Arrays;

import lu.bout.rpg.engine.Taggable;
import lu.bout.rpg.engine.combat.participant.Participant;
import lu.bout.rpg.engine.combat.status.DamageReceivedAffectingStatus;
import lu.bout.rpg.vocabulary.Status;

public class BrittleStatus implements DamageReceivedAffectingStatus, Taggable {
    @Override
    public int adjustReceivedDamage(Participant p, int damage) {
        p.removeStatus(this);
        return damage * 2;
    }
    @Override
    public Iterable<String> getTags() {
        return Arrays.asList(new String[]{Status.BRITTLE});
    }
}
