package lu.bout.rpg.battler.campaign.screen;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.shared.StageScreen;
import lu.bout.rpg.battler.world.city.DungeonLocation;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.PeopleEncounter;
import lu.bout.rpg.battler.world.city.LocationMap;

public class LocationScreen extends StageScreen {

    private static final AssetDescriptor FILE_BG = new AssetDescriptor("town/town_bg.jpg", Texture.class);
    private static final AssetDescriptor FILE_PLANK = new AssetDescriptor("town/plank.9.png", Texture.class);
    private static final AssetDescriptor FILE_DUNGEON = new AssetDescriptor("town/SGI_addons_173.png", Texture.class);
    private static final AssetDescriptor FILE_VILLAGE = new AssetDescriptor("town/SGI_112.png", Texture.class);
    private static final AssetDescriptor FILE_INN = new AssetDescriptor("town/SGI_114.png", Texture.class);
    private static final AssetDescriptor FILE_GENERIC = new AssetDescriptor("town/SGI_addons_178.png", Texture.class);

    private Label title;
    private Table links;
    private Table encounters;

    private LocationMap map;


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
        super(game);
        game.getAssetService().preload(getRequiredFiles());
        init();
    }

    protected void init() {

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
        encounters = new Table();
        encounters.align(Align.left);
        root.add(encounters).colspan(2).growX().pad(15);
        //new Label("noone here", game.getSkin())
    }

    public void showLocation(LocationMap map, final Location location) {
        this.map = map;
        title.setText(location.name);
        links.clearChildren();
        encounters.clearChildren();
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
        for (PeopleEncounter encounter: location.getEncounters()) {
            encounters.add(buildEncounterIcon(encounter));
        }
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

    protected void goToLocation(int locationId) {
        map.getLocation(locationId).goTo(game, map);
    }

}
