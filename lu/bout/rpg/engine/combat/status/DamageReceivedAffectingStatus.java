package lu.bout.rpg.engine.combat.status;

import lu.bout.rpg.engine.combat.participant.Participant;

public interface DamageReceivedAffectingStatus extends CombatStatus {
    public int adjustReceivedDamage(Participant p, int damage);
}
