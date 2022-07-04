package lu.bout.rpg.battler.battle.minigame;

import lu.bout.rpg.battler.SubScreen;
import lu.bout.rpg.engine.combat.command.CombatCommand;

public interface MiniGame extends SubScreen {

    public void init(int difficulty, CombatCommand command);

    public CombatCommand getCommandToRun();

    public void dispose();
}
