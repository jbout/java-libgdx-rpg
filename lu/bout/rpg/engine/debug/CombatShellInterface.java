package lu.bout.rpg.engine.debug;

import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.CombatListener;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.event.AttackEvent;
import lu.bout.rpg.engine.combat.event.ParticipiantEvent;

public class CombatShellInterface implements CombatListener {

    public static void main(String args[])
    {
		Combat combat = Samples.getSampleCombat();
    	CombatShellInterface shell = new CombatShellInterface(combat);
    	combat.advanceTimer(500);
    }
    
    private Combat combat;
    
    public CombatShellInterface(Combat c) {
    	combat = c;
    	combat.addListener(this);
    }

	@Override
	public void receiveCombatEvent(Combat combat, CombatEvent a) {
		if (a.getClass() == AttackEvent.class) {
			
			System.out.println(((AttackEvent)a).getAttacker()
					+ " attacked " + ((AttackEvent)a).getTarget() + " for "
					+ ((AttackEvent)a).getDamage() + " damage.");
		} else {
			if (a instanceof ParticipiantEvent) {
				System.out.println(((ParticipiantEvent)a).getActor() + " has done " + a.getClass().getSimpleName());
			} else {
				System.out.println(a.getClass().getSimpleName() + " happened");
			}
		}
	}
    
}
