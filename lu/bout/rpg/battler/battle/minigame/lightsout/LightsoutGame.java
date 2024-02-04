package lu.bout.rpg.battler.battle.minigame.lightsout;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;

import lu.bout.rpg.battler.battle.minigame.RuneButton;
import lu.bout.rpg.battler.battle.minigame.MiniGameFeedback;
import lu.bout.rpg.battler.battle.minigame.MiniGame;
import lu.bout.rpg.engine.combat.command.CombatCommand;

public class LightsoutGame extends Rectangle implements MiniGame {

    final static int SOLVE_TIME = 7;

    private RuneButton[][] buttons;

    private Vector2 lastTouched = null;

    Texture buttonUp;
    Texture buttonDown;
    TextureRegion timer;
    TextureAtlas runes;

    private MiniGameFeedback callback;
    private CombatCommand commandToRun;

    float timeleft;

    public LightsoutGame(MiniGameFeedback callback, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.callback = callback;

        // load images
        buttonUp = new Texture("button_up_badlicence_146x170.png");
        buttonDown = new Texture("button_down_badlicence_146x170.png");
        runes = new TextureAtlas("runes_70x90_bluegray.atlas");

        Texture timebg = new Texture("minigame/time.png");
        timebg.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat);
        timer = new TextureRegion(timebg,0,0,timebg.getWidth(),height);
    }

    public void init(int difficulty, CombatCommand command) {
        commandToRun = command;
        difficulty = Math.min(7, Math.max(3, difficulty));
        int burnout = 0;
        int sizeX = 2;
        int sizeY = 2;
        switch (difficulty) {
            case 3:
                sizeX = sizeY = 2;
                burnout = 0;
                break;
            case 4:
                sizeX = 3;
                sizeY = 2;
                burnout = 1;
                break;
            case 5:
                sizeX = sizeY = 3;
                burnout = 2;
                break;
            case 6:
                sizeX = 4;
                sizeY = 3;
                burnout = 3;
                break;
            case 7:
                sizeX = 4;
                sizeY = 4;
                burnout = 4;
                break;
        }
        HashSet<Integer> uniqueRuneIds = new HashSet<>();
        while (uniqueRuneIds.size() < sizeX * sizeY){
            uniqueRuneIds.add(MathUtils.random(0, 23));
        }
        Integer[] runeIds = uniqueRuneIds.toArray(new Integer[uniqueRuneIds.size()]);
        buttons = new RuneButton[sizeX][sizeY];
        float posx0 = (width - ((sizeX - 1) * 150)) / 2;
        float posy0 = (height - ((sizeY - 1) * 150)) / 2;
        int count = 0;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                buttons[x][y] = new RuneButton(buttonUp, buttonDown, runes);
                buttons[x][y].radius = 50;
                buttons[x][y].x = 150 * x + posx0;
                buttons[x][y].y = 150 * y + posy0;
                buttons[x][y].setRune(runeIds[count++]);
                buttons[x][y].setDown(true);
            }
        }
        for (int i = 0; i < burnout; i++) {
            buttons[MathUtils.random(0, sizeX-1)][MathUtils.random(0, sizeY-1)] = null;
        }
        randomize();
        timeleft = SOLVE_TIME;
    }

    public void changeCommand(CombatCommand command) {
        commandToRun = command;
    }

    private void clickButton(int x, int y) {
        buttons[x][y].toogle();
        int[][] toTry = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] pair : toTry) {
            int xpos = x + pair[0];
            int ypos = y + pair[1];
            if (xpos >= 0 && ypos >= 0 && xpos < buttons.length && ypos < buttons[0].length) {
                if (buttons[xpos][ypos] != null) {
                    buttons[xpos][ypos].toogle();
                }
            }
        }
    }

    private void randomize() {
        do {
            for (int x = 0; x < buttons.length; x++) {
                for (int y = 0; y < buttons[0].length; y++) {
                    if (buttons[x][y] != null && MathUtils.randomBoolean()) {
                        clickButton(x,y);
                    }
                }
            }
        } while (isVictory());
    }

    public void render(SpriteBatch batch, float delta, Vector2 inputVector) {
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                if (buttons[x][y] != null) {
                    buttons[x][y].render(batch);
                    if (inputVector != null && buttons[x][y].contains(inputVector)) {
                        if (lastTouched == null || !(lastTouched.x == x && lastTouched.y == y)) {
                            clickButton(x,y);
                            if (isVictory()) {
                                callback.minigameEnded(true, this, 0);
                            };
                            lastTouched = new Vector2(x,y);
                        }
                    }

                }
            }
        }
        if (inputVector == null) {
            lastTouched = null;
        }
        timeleft -= delta;
        if (timeleft <= 0) {
            callback.minigameEnded(false, this, 0);
        }
        int height = (int) (getHeight() * timeleft / SOLVE_TIME);
        timer.setRegionHeight(height);
        batch.draw(timer, width - timer.getRegionWidth(), 0, timer.getRegionWidth(), height);
    }

    private boolean isVictory() {
        boolean victory = true;
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                if (buttons[x][y] != null && !buttons[x][y].isDown()) {
                    victory = false;
                    break;
                }
            }
        }
        return victory;
    }

    @Override
    public CombatCommand getCommandToRun() {
        return commandToRun;
    }

    public void dispose() {
        buttonUp.dispose();
        buttonDown.dispose();
    }
}
