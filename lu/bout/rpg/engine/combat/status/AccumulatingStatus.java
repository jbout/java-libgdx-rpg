package lu.bout.rpg.engine.combat.status;

import lu.bout.rpg.engine.combat.participant.Participant;

public interface AccumulatingStatus extends CombatStatus {
    public void accumulate(CombatStatus status);
}
