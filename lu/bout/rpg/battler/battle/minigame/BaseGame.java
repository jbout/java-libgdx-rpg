package lu.bout.rpg.battler.battle.minigame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import lu.bout.rpg.engine.combat.command.CombatCommand;

public abstract class BaseGame extends Rectangle implements MiniGame {

    private CombatCommand commandToRun;
    private MiniGameFeedback callback;

    public BaseGame(MiniGameFeedback callback, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.callback = callback;
    }

    public void init(CombatCommand command) {
        this.commandToRun = command;
    }

    @Override
    public void changeCommand(CombatCommand command) {
        this.commandToRun = command;
    }

    @Override
    public CombatCommand getCommandToRun() {
        return commandToRun;
    }

    @Override
    public void dispose() {

    }

    protected void finish(boolean success, long timeElapsed) {
        callback.minigameEnded(success, this, timeElapsed);
    }
}
