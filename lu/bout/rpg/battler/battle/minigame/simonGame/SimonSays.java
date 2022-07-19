package lu.bout.rpg.battler.battle.minigame.simonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import lu.bout.rpg.battler.battle.minigame.MiniGameFeedback;
import lu.bout.rpg.battler.battle.minigame.MiniGame;
import lu.bout.rpg.engine.combat.command.CombatCommand;

public class SimonSays extends Rectangle implements MiniGame {

    private enum Mode {show, play, finished};

    private Array<SimonButton> buttons;
    private List<SimonButton> correctOrder;

    private Mode mode;
    private float elapsed;
    private float showDuration;

    Texture buttonUp;
    Texture buttonDown;
    TextureAtlas runes;
    SimonSprite sprite;

    private MiniGameFeedback callback;
    private CombatCommand commandToRun;

    public SimonSays(MiniGameFeedback callback, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.callback = callback;

        // load images
        buttonUp = new Texture("button_up_badlicence_146x170.png");
        buttonDown = new Texture("button_down_badlicence_146x170.png");
        runes = new TextureAtlas("runes_70x90_bluegray.atlas");
        sprite = new SimonSprite();

        buttons = generateButtons(8);
    }

    public void init(int difficulty, CombatCommand command) {
        commandToRun = command;
        if (difficulty > buttons.size) {
            difficulty = buttons.size;
        }
        if (difficulty < 2) {
            difficulty = 2;
        }
        showDuration = 3 - (0.5f * Math.max(0, 5-difficulty));
        HashSet<Integer> uniqueRuneIds = new HashSet<>();
        while (uniqueRuneIds.size() < buttons.size){
            uniqueRuneIds.add(MathUtils.random(0, 23));
        }
        List<SimonButton> clonedButtons = new ArrayList<SimonButton>();
        for(Integer runeId : uniqueRuneIds) {
            SimonButton button = buttons.get(clonedButtons.size());
            button.setRune(runeId);
            button.setDown(false);
            clonedButtons.add(button);
        }

        Collections.shuffle(clonedButtons);
        correctOrder = clonedButtons.subList(0, difficulty);
        mode = Mode.show;
        elapsed = 0;
    }

    public Array<SimonButton> generateButtons(int nr) {
        Array<SimonButton> circleButtons = new Array<SimonButton>();
        double step = 2 * Math.PI / nr;
        int radius = (int) ((Math.min(width- SimonButton.RENDER_WIDTH, height - SimonButton.RENDER_HEIGHT)) * 0.4);
        for (int i = 0; i < nr; i++) {
            SimonButton circle = new SimonButton();
            circle.radius = 30;
            circle.x = Math.round((width / 2) + (Math.sin((i+0.5)*step) * radius));
            circle.y = Math.round((height / 2) + (Math.cos((i+0.5)*step) * radius));
            circleButtons.add(circle);
        }
        return circleButtons;
    }

    public void render(SpriteBatch batch, float delta, Vector2 inputVector) {

        for(SimonButton button: buttons) {
            button.render(this, batch);
            if (mode == Mode.play && inputVector != null && button.contains(inputVector)) {
                if (!button.isDown()) {
                    buttonPressed(button);
                }
            }
        }
        if (mode == Mode.show) {
            renderShow(batch, delta);
        }
    }

    public void renderShow(SpriteBatch batch, float delta) {
        elapsed += delta;
        if (elapsed  > showDuration) {
            mode = Mode.play;
        } else {
            float legDuration = (showDuration  / (correctOrder.size()));
            int leg = (int)(elapsed / legDuration);
            float legPercentage = (elapsed - (leg*legDuration))/legDuration;
            SimonButton from = correctOrder.get(Math.max(0, leg-1));
            SimonButton to = correctOrder.get(leg);
            if (leg == 0) {
                sprite.alpha = legPercentage;
            }
            sprite.x = (int) ((from.x * (1-legPercentage)) + (to.x * legPercentage));
            sprite.y = (int) ((from.y * (1-legPercentage)) + (to.y * legPercentage));
            sprite.render(batch);
        }
    }

    private void buttonPressed(SimonButton button) {
        if (button.equals(correctOrder.get(0))) {
            // correct
            button.setDown(true);
            Gdx.input.vibrate(30);
            correctOrder.remove(0);
        } else {
            // failure
            mode = Mode.finished;
            Gdx.input.vibrate(200);
            callback.minigameEnded(false, this, 0);
        }
        if (correctOrder.size() == 0) {
            // success
            mode = mode.finished;
            callback.minigameEnded(true, this, 0);
        }
    }

    @Override
    public CombatCommand getCommandToRun() {
        return commandToRun;
    }

    public void dispose() {
        buttonUp.dispose();
        buttonDown.dispose();
        sprite.dispose();
    }
}
