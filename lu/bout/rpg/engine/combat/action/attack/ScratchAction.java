package lu.bout.rpg.engine.combat.action.attack;

import lu.bout.rpg.engine.character.CharacterSheet;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.participant.Participant;

public class ScratchAction extends HitAction{
    public ScratchAction(Participant actor, Participant target) {
        super(actor, target);
    }

    public void execute(Combat combat) {
        super.execute(combat);
        // target.addStatus(Bleed);
    }

    protected int calculateDamageAgainst(CharacterSheet target) {
        return super.calculateDamageAgainst(target) / 2;
    }
}
