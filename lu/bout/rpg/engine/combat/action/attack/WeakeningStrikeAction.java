package lu.bout.rpg.engine.combat.action.attack;

import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.participant.Participant;
import lu.bout.rpg.engine.combat.status.negative.BrittleStatus;

public class WeakeningStrikeAction extends HitAction{
    public WeakeningStrikeAction(Participant actor, Participant target) {
        super(actor, target);
    }

    public void execute(Combat combat) {
        super.execute(combat);
        target.setStatus(new BrittleStatus());
    }

    protected int calculateDamageAgainst(Character target) {
        return super.calculateDamageAgainst(target) / 2;
    }
}
