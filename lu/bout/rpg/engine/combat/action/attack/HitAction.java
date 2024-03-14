package lu.bout.rpg.engine.combat.action.attack;

import lu.bout.rpg.engine.character.CharacterSheet;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.action.SingleTargetAction;
import lu.bout.rpg.engine.combat.event.AttackEvent;
import lu.bout.rpg.engine.combat.participant.Participant;

public class HitAction extends SingleTargetAction {
    public HitAction(Participant actor, Participant target) {
        super(actor, target);
    }

    public void execute(Combat combat) {
        int dmg = calculateDamageAgainst(target.getCharacter());
        int actualDamage = target.takesDamage(combat, dmg);
        combat.trigger(new AttackEvent(combat, actor, target, actualDamage));

    }

    protected int calculateDamageAgainst(CharacterSheet target) {
        return actor.getCharacter().getDamage();
    }
}
