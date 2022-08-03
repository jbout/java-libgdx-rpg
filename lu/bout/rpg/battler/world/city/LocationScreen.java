package lu.bout.rpg.battler.world.city;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lu.bout.rpg.battler.RpgGame;

public class LocationScreen implements Screen {

    private static final AssetDescriptor FILE_BG = new AssetDescriptor("town/town_bg.jpg", Texture.class);
    private static final AssetDescriptor FILE_PLANK = new AssetDescriptor("town/plank.9.png", Texture.class);
    private static final AssetDescriptor FILE_DUNGEON = new AssetDescriptor("town/SGI_addons_173.png", Texture.class);
    private static final AssetDescriptor FILE_VILLAGE = new AssetDescriptor("town/SGI_112.png", Texture.class);
    private static final AssetDescriptor FILE_INN = new AssetDescriptor("town/SGI_114.png", Texture.class);
    private static final AssetDescriptor FILE_GENERIC = new AssetDescriptor("town/SGI_addons_178.png", Texture.class);

    protected final RpgGame game;

    protected Stage stage;

    private Label title;
    private Table links;

    private LocationMap map;
    private Location current;


    public static AssetDescriptor[] getRequiredFiles() {
        return new AssetDescriptor[]{
                FILE_BG,
                FILE_PLANK,
                FILE_DUNGEON,
                FILE_VILLAGE,
                FILE_INN
        };
    }

    public LocationScreen(final RpgGame game) {
        this.game = game;
        game.getAssetService().preload(getRequiredFiles());
        init();
    }

    protected void init() {
        stage = new Stage(new ScreenViewport());

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(new TextureRegionDrawable((Texture)game.getAssetService().get(FILE_BG)));
        stage.addActor(root);

        Table plankTable = new Table();
        plankTable.setBackground(new TextureRegionDrawable((Texture) game.getAssetService().get(FILE_PLANK)));
        title = new Label("Town Name", game.getSkin(), "wood");
        plankTable.add(title).align(Align.center).pad(10);
        root.add(plankTable).colspan(2).expandX().align(Align.center).pad(10);
        root.row().expandY();

        links = new Table();
        links.defaults().pad(5);
        root.add(links).grow();
        root.row();
        root.add(new Label("noone here", game.getSkin())).colspan(2).growX();
    }

    public void showLocation(LocationMap map, final Location location) {
        this.map = map;
        current = location;
        title.setText(location.name);
        links.clearChildren();
        int count = 0;
        for (final Location loc : map.getConnections(location)) {
            Actor icon = buildLocationIcon(loc);
            icon.addListener(new ChangeListener(){
                public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                    goToLocation(loc.getId());
                }
            });
            links.add(icon);
            if (++count % 2 == 0) {
                links.row();
            }
        }
    }

    private Actor buildLocationIcon(Location location) {
        Table locationTable = new Table();
        locationTable.setBackground(getIcon(location));
        Label label = new Label(location.name, game.getSkin(), "location");
        label.setAlignment(Align.center);
        locationTable.add(label).growX().align(Align.bottom).expandY().padBottom(10);
        return new Button(locationTable, game.getSkin());
    }

    private Drawable getIcon(Location location) {
        Texture bg;
        if (location instanceof DungeonLocation) {
            bg = (Texture) game.getAssetService().get(FILE_DUNGEON);
        } else {
            switch (location.name) {
                case "Inn" :
                    bg = (Texture) game.getAssetService().get(FILE_INN);
                    break;
                case "Village" :
                    bg = (Texture) game.getAssetService().get(FILE_VILLAGE);
                    break;
                default:
                    bg = (Texture) game.getAssetService().get(FILE_GENERIC);
            }
        }

        return (new TextureRegionDrawable(bg));
    }

    private Table createButton(Location location) {
        Table locationTable = new Table();
        Texture bg = (Texture) game.getAssetService().get(FILE_DUNGEON);
        locationTable.setBackground(new TextureRegionDrawable(bg));
        Label label = new Label(location.name, game.getSkin(), "location");
        label.setAlignment(Align.center);
        locationTable.add(label).growX().align(Align.bottom).expandY().padBottom(10);
        return locationTable;
    }

    protected void goToLocation(int locationId) {
        current = map.getLocation(locationId);
        current.enter(game, map);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
