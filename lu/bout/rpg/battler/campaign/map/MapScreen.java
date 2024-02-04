package lu.bout.rpg.battler.campaign.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;


import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.assets.AssetConsumer;

public class MapScreen implements Screen, AssetConsumer, GestureDetector.GestureListener {

    RpgGame game;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private OrthographicCamera camera;

    /**
     * registered in asset manager for this
     */
    private static final AssetDescriptor FILE_TMX_MAP = new AssetDescriptor("map/desert.tmx", TiledMap.class);

    public MapScreen(final RpgGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
    }

    public void init() {
        map = (TiledMap) game.getAssetService().get(FILE_TMX_MAP);
        renderer = new OrthogonalTiledMapRenderer(map, 5 / 2f);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.7f, 0.7f, 1.0f, 1);
        renderer.setView(camera);
        renderer.render();
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
    public AssetDescriptor[] getRequiredFiles() {
        return new AssetDescriptor[]{FILE_TMX_MAP };
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
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
        camera.position.set(camera.position.x - deltaX, camera.position.y + deltaY,0);
        Gdx.app.log("Game", "Moved to " + camera.position);
        camera.update();
        return true;
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
