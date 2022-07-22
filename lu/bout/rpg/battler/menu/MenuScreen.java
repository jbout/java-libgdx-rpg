package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.assets.AssetConsumer;

public abstract class MenuScreen implements Screen, AssetConsumer {

    private static final AssetDescriptor FILE_MENU_BG = new AssetDescriptor("bg_01.png", Texture.class);

    protected final RpgGame game;

    protected Stage stage;

    private Image bgImage;


    public AssetDescriptor[] getRequiredFiles() {
        return new AssetDescriptor[]{
                FILE_MENU_BG
        };
    }

	public MenuScreen(final RpgGame game) {
        this.game = game;
    }

    protected void init() {
        stage = new Stage(new ScreenViewport());

        bgImage = new Image((Texture) game.getAssetService().get(FILE_MENU_BG));
        bgImage.setPosition(
                (Gdx.graphics.getWidth() - bgImage.getWidth()) / 2,
                (Gdx.graphics.getHeight() - bgImage.getHeight()) / 2
        );
        stage.addActor(bgImage);
    }

    protected Table getRootTable() {
        Table root = new Table();
        //root.defaults().expand();
        Rectangle bounds = this.getUsableSurface();
        root.setBounds(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight());
        stage.addActor(root);
        return root;
    }

    protected Rectangle getUsableSurface() {
        return new Rectangle(bgImage.getX()+68, bgImage.getY()+80, bgImage.getWidth()-130, bgImage.getHeight()-135);
    }

    @Override
    public void show() {
        if (stage == null) {
            init();
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.776f, 0.643f, 0.466f, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }
}
