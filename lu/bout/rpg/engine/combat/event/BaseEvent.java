package lu.bout.rpg.engine.combat.event;

import lu.bout.rpg.engine.combat.Combat;

public class BaseEvent implements CombatEvent {

    private final Combat c;

    public BaseEvent(Combat c) {
        this.c = c;
    }

    public Combat getCombat() {
        return c;
    }
}
