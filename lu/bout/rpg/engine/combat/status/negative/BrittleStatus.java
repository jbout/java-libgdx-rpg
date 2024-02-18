package lu.bout.rpg.engine.combat.status.negative;

import lu.bout.rpg.engine.combat.participant.Participant;
import lu.bout.rpg.engine.combat.status.CombatStatus;
import lu.bout.rpg.engine.combat.status.DamageReceivedAffectingStatus;

public class BrittleStatus implements DamageReceivedAffectingStatus {
    @Override
    public int adjustReceivedDamage(Participant p, int damage) {
        p.removeStatus(this);
        return damage * 2;
    }
}
