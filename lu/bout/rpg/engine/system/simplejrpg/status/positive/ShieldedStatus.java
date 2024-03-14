package lu.bout.rpg.engine.system.simplejrpg.status.positive;

import java.util.Arrays;

import lu.bout.rpg.engine.Taggable;
import lu.bout.rpg.engine.combat.participant.Participant;
import lu.bout.rpg.engine.combat.status.AccumulatingStatus;
import lu.bout.rpg.engine.combat.status.CombatStatus;
import lu.bout.rpg.engine.combat.status.DamageReceivedAffectingStatus;
import lu.bout.rpg.vocabulary.Status;

public class ShieldedStatus implements AccumulatingStatus, DamageReceivedAffectingStatus, Taggable {

    private int strength;

    public ShieldedStatus(int strength) {
        this.strength = strength;
    }

    @Override
    public int adjustReceivedDamage(Participant p, int damage) {
        if (damage >= strength) {
            damage = damage - strength;
            p.removeStatus(this);
        } else {
            strength = strength - damage;
            damage = 0;
        }
        return damage;
    }
    @Override
    public Iterable<String> getTags() {
        return Arrays.asList(new String[]{Status.SHIELDED, Status.STRENGTHEN});
    }

    public void accumulate(CombatStatus status) {
        if (status instanceof ShieldedStatus) {
            this.strength = strength + ((ShieldedStatus) status).strength;
        }
    }
}
