package lu.bout.rpg.battler.battle.minigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import lu.bout.rpg.engine.combat.command.CombatCommand;

public class SimonSays extends Rectangle implements MiniGame {

    final static int MODE_SHOW = 1;
    final static int MODE_PLAY = 2;
    final static int MODE_FINISHED = 3;

    private Array<SimonButton> buttons;
    private List<SimonButton> correctOrder;

    private int mode;
    private long showStart;
    private long showDuration;

    Texture buttonUp;
    Texture buttonDown;
    TextureAtlas runes;
    SimonSprite sprite;

    private GameFeedback callback;
    private CombatCommand commandToRun;

    public SimonSays(GameFeedback callback, int x, int y, int width, int height) {
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
        showDuration = 3000000000L - (500000000L * Math.max(0, 5-difficulty));
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
        mode = MODE_SHOW;
        showStart = TimeUtils.nanoTime();
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
            if (mode == MODE_PLAY && inputVector != null && button.contains(inputVector)) {
                if (!button.isDown()) {
                    buttonPressed(button);
                }
            }
        }
        if (mode == MODE_SHOW) {
            renderShow(batch, delta);
        }
    }

    public void renderShow(SpriteBatch batch, float delta) {
        long elapsedTime = TimeUtils.nanoTime() - showStart;
        if (elapsedTime  > showDuration) {
            mode = MODE_PLAY;
        } else {
            float legDuration = (showDuration  / (correctOrder.size()));
            int leg = (int)(elapsedTime / legDuration);
            float legPercentage = (elapsedTime - (leg*legDuration))/legDuration;
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
            mode = MODE_FINISHED;
            Gdx.input.vibrate(200);
            callback.minigameEnded(false, this, 0);
        }
        if (correctOrder.size() == 0) {
            // success
            mode = MODE_FINISHED;
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
