package lu.bout.rpg.battler.battle.minigame.japanese;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import lu.bout.rpg.battler.battle.minigame.BaseGame;
import lu.bout.rpg.battler.battle.minigame.MiniGameFeedback;
import lu.bout.rpg.battler.shared.PrecachedFont;
import lu.bout.rpg.engine.combat.command.CombatCommand;
import sun.font.TrueTypeFont;

public class WordSearchGame extends BaseGame {

    private static final int BORDER_CHAR = 32;

    private Challenge currentChallenge;

    private LinkedList<Challenge> precached = new LinkedList<>();
    private Sprite[][] grid;

    private PrecachedFont font;

    private int[] downChar = null;
    private int[] lastHover = null;

    public WordSearchGame(MiniGameFeedback callback, int x, int y, int width, int height, BitmapFont font) {
        super(callback, x, y, width, height);
        this.font = new PrecachedFont();
    }

    @Override
    public void init(int difficulty, CombatCommand command) {
        init(command);
        if (precached.isEmpty()) {
            // sync
            preload();
        }
        setCurrentChallenge(precached.removeFirst());
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render(SpriteBatch batch, float delta, Vector2 inputVector) {

        font.getCachedFont().draw(batch, currentChallenge.getPrompt(), 0, this.height - 40, this.width, Align.center, true);
        int hover[] = null;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y].draw(batch);
                if (inputVector != null && grid[x][y].getBoundingRectangle().contains(inputVector)) {
                    hover = new int[]{x, y};
                }
            }
        }
        if (inputVector != null && downChar == null && hover != null) {
            // mousedown
            downChar = hover;
        }
        if (inputVector == null && downChar != null) {
            // mouseup
            if (lastHover != null) {
                eval(downChar, lastHover);
            }
            downChar = null;
        }
        lastHover = hover;
        if (inputVector != null && downChar != null) {
            Vector2 tmpVector = new Vector2();
            // draw the line
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setColor(new Color(0.5f, 0.1f, 0.1f, 0.5f));
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            grid[downChar[0]][downChar[1]].getBoundingRectangle().getCenter(tmpVector);
            shapeRenderer.rectLine(tmpVector, inputVector, 20);
            if (hover != null) {
                for (int[] coord: getSelected(downChar, hover)) {
                    grid[coord[0]][coord[1]].getBoundingRectangle().getCenter(tmpVector);
                    shapeRenderer.circle(tmpVector.x, tmpVector.y, 64);
                }
            } else {
                //shapeRenderer.circle(tmpVector.x, tmpVector.y, 32);
            }
            //shapeRenderer.circle(inputVector.x, inputVector.y, 32);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();
        }
    }

    private int[][] getSelected(int[] from, int[] to) {
        boolean inLine = from[0] == to[0] || from[1] == to[1] || Math.abs(from[0]-to[0]) == Math.abs(from[1]-to[1]);
        if (!inLine || Arrays.equals(from,to)) {
            return new int[0][];
        }
        int length = Math.max(Math.abs(from[0]-to[0]), Math.abs(from[1]-to[1])) + 1;
        int[][] coordinates = new int[length][2];
        int dx = Integer.signum(to[0] - from[0]);
        int dy = Integer.signum(to[1] - from[1]);
        for (int i = 0; i < length; i++) {
            coordinates[i][0] = from[0]+(i*dx);
            coordinates[i][1] = from[1]+(i*dy);
        }
        return coordinates;
    }

    private void preload() {
        HashSet<String> toCache = new HashSet<>();
        for (int i=0; i < 7; i++) {
            Challenge c = ChallengeBuilder.buildChallenge();
            precached.add(c);
            toCache.addAll(c.getUsedCharacters());
        }
        String characters = "";
        for (String s: toCache) {
            characters = characters + s;
        }
        font.preCache(characters);
    }

    private void setCurrentChallenge(Challenge challenge) {
        this.currentChallenge = challenge;
        char[][] map = challenge.getMap();
        grid = new Sprite[map.length][map[0].length];
        // keep ratio between letters
        float delta = Math.min((width-2*BORDER_CHAR) / grid.length, (height-2*BORDER_CHAR) / grid[0].length);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = font.getSpriteFor(map[i][j]);
                grid[i][j].setCenter((float) (BORDER_CHAR + delta*(0.5+i)), (float) (BORDER_CHAR + delta*(0.5+j)));
            }
        }
    }

    private void eval(int[] from, int[] to) {
        int[][] path = getSelected(from, to);
        if (path.length > 0) {
            char[] selected = new char[path.length];
            for (int i = 0; i < path.length; i++) {
                selected[i] = currentChallenge.getMap()[path[i][0]][path[i][1]];
            }
            String response = String.valueOf(selected);
            finish(response.compareTo(currentChallenge.getAnswer()) == 0, 0);
        }
    }
}
