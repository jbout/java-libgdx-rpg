package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import org.w3c.dom.Text;

import lu.bout.rpg.battler.RpgBattler;
import lu.bout.rpg.battler.world.Beastiarum;
import lu.bout.rpg.character.Party;
import lu.bout.rpg.character.Player;
import lu.bout.rpg.combat.Encounter;
import lu.bout.rpg.debug.Samples;

public class HomeScreen implements Screen, GestureDetector.GestureListener {

    final RpgBattler game;

    OrthographicCamera camera;
    Texture bg;

	public HomeScreen(final RpgBattler game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, RpgBattler.WIDTH, RpgBattler.HEIGHT);
        bg = new Texture("bg_01.png");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(bg, 0, 0 , RpgBattler.WIDTH, RpgBattler.HEIGHT);
        game.font.draw(game.batch, "Welcome to the Battle emulator ", 100, (int)(RpgBattler.HEIGHT*0.6));
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, (int)(RpgBattler.HEIGHT*0.3));
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        game.launchDungeon();
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
