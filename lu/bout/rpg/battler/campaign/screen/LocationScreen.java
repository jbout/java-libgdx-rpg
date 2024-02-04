package lu.bout.rpg.battler.campaign.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.menu.NewGameScreen;
import lu.bout.rpg.battler.menu.SettingsDialog;
import lu.bout.rpg.battler.menu.SettingsScreen;
import lu.bout.rpg.battler.shared.StageScreen;
import lu.bout.rpg.battler.world.city.DungeonLocation;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.PeopleEncounter;
import lu.bout.rpg.battler.world.city.LocationMap;
import lu.bout.rpg.utils.CameraZoomAction;

public class LocationScreen extends StageScreen {

    private static final AssetDescriptor FILE_BG = new AssetDescriptor("town/town_bg.jpg", Texture.class);
    private static final AssetDescriptor FILE_PLANK = new AssetDescriptor("town/plank.9.png", Texture.class);
    private static final AssetDescriptor FILE_DUNGEON = new AssetDescriptor("town/SGI_addons_173.png", Texture.class);
    private static final AssetDescriptor FILE_VILLAGE = new AssetDescriptor("town/SGI_112.png", Texture.class);
    private static final AssetDescriptor FILE_INN = new AssetDescriptor("town/SGI_114.png", Texture.class);
    private static final AssetDescriptor FILE_GENERIC = new AssetDescriptor("town/SGI_addons_178.png", Texture.class);

    private static final AssetDescriptor FILE_SETTINGS_ICON = new AssetDescriptor("settings.png", Texture.class);

    private Label title;
    private Table links;
    private Table encounters;

    private LocationMap map;

    private ImageButton settings;


    public static AssetDescriptor[] getRequiredFiles() {
        return new AssetDescriptor[]{
                FILE_BG,
                FILE_PLANK,
                FILE_DUNGEON,
                FILE_VILLAGE,
                FILE_INN,
                FILE_SETTINGS_ICON
        };
    }

    public LocationScreen(final RpgGame game) {
        super(game);
        game.getAssetService().preload(getRequiredFiles());
        init();
    }

    protected void init() {

        Table root = new Table();
        root.pad(20);
        root.setFillParent(true);
        root.setBackground(new TextureRegionDrawable((Texture)game.getAssetService().get(FILE_BG)));
        stage.addActor(root);

        Table plankTable = new Table();
        plankTable.setBackground(new TextureRegionDrawable((Texture) game.getAssetService().get(FILE_PLANK)));
        title = new Label("Town Name", game.getSkin(), "wood");
        plankTable.add(title).align(Align.center).pad(10);
        settings = new ImageButton(new TextureRegionDrawable((Texture) game.getAssetService().get(FILE_SETTINGS_ICON)));
        root.add().width(75);
        root.add(plankTable).expandX().align(Align.center);
        root.add(settings).width(75);
        root.row().expandY();
        settings.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                SettingsDialog dialog = new SettingsDialog(game);
                dialog.show(stage);
            }
        });

        links = new Table();
        links.defaults().pad(5);
        root.add(links).colspan(3).grow();
        root.row();
        encounters = new Table();
        encounters.align(Align.left);
        root.add(encounters).colspan(3).growX().pad(15);
        //new Label("noone here", game.getSkin())
    }

    public void showLocation(LocationMap map, final Location location) {
        this.map = map;
        title.setText(location.name);
        links.clearChildren();
        encounters.clearChildren();
        int count = 0;
        for (final Location loc : map.getConnections(location)) {
            final Actor icon = buildLocationIcon(loc);
            final Cell cell = links.add(icon);
            icon.addListener(new ChangeListener(){
                public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                    goToLocation(cell, loc.getId());
                }
            });
            if (++count % 2 == 0) {
                links.row();
            }
        }
        for (PeopleEncounter encounter: location.getEncounters()) {
            encounters.add(buildEncounterIcon(encounter));
        }
        getOrthographicCamera().zoom = 1;
        stage.getRoot().addAction(Actions.fadeIn(0.3f));
    }

    private Actor buildLocationIcon(Location location) {
        Table locationTable = new Table();
        locationTable.setBackground(getLocationIcon(location));
        Label label = new Label(location.name, game.getSkin(), "location");
        label.setAlignment(Align.center);
        locationTable.add(label).growX().align(Align.bottom).expandY().padBottom(10);
        return new Button(locationTable, game.getSkin());
    }

    private Drawable getLocationIcon(Location location) {
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

    private Actor buildEncounterIcon(final PeopleEncounter encounter) {
        PortraitService p = new PortraitService();
        Drawable drawable = new TextureRegionDrawable(p.getRoundPortrait(encounter.getActor().getPortaitId(), 149));
        Button icon = new Button(drawable);
        icon.addListener(new ChangeListener(){
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                encounter.getAction().run(game);
            }
        });
        return icon;
    }

    protected void goToLocation(final Cell cell, final int locationId) {
        //positionCamera(cell);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.parallel(fadeOut(0.7f), zoomTo(cell, 0.7f)));
        sequenceAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                map.getLocation(locationId).goTo(game, map);
            }
        }));
        stage.getRoot().addAction(sequenceAction);
    }

    protected CameraZoomAction zoomTo(final Cell cell, float duration) {
        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
        Vector2 screenPosition = cell.getActor().localToScreenCoordinates(new Vector2(0f, 0f));

        Rectangle destination = new Rectangle(
                screenPosition.x,
                Gdx.graphics.getHeight() - screenPosition.y,
                cell.getActorWidth(),
                cell.getActorHeight()
        );
        return new CameraZoomAction(getOrthographicCamera(), destination, duration);
    }
}
