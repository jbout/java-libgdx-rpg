package lu.bout.rpg.battler.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.battle.BattleFeedback;
import lu.bout.rpg.battler.campaign.storyAction.StoryAction;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.world.Beastiarum;
import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.character.Party;

public class DungeonScreen implements Screen, GestureDetector.GestureListener, BattleFeedback {

    private static final AssetDescriptor FILE_CAVE_BG = new AssetDescriptor("map/cave_texture.jpg", Texture.class);
    private static final AssetDescriptor FILE_CAVE_ATLAS = new AssetDescriptor("map/cave_map.atlas", TextureAtlas.class);

    static final int Y_DISTANCE = 190;

    final RpgGame game;
    private final Viewport uiViewport;

    protected Stage uiStage;
    List<PlayerPortrait> portraits;

    OrthographicCamera camera;
    Viewport mapViewport;
    float maxScroll;

    private final Texture bgTexture;
    private final TextureRegion bg;
    private final TextureAtlas mapTextures;
    private final TextureRegion left;
    private final TextureRegion straight;
    private final TextureRegion right;
    private final TextureRegion marker;

    private StoryAction onSuccess;
    private StoryAction onFlee;

    DungeonMap dungeonMap;
    PlayerParty playerParty;
    LinkedList<FieldSprite> fieldSprites;
    Map<Circle, Field> options;
    Field current;
    Circle currentHitBox;

    Field movingTo = null;
    FieldSprite movingToSprite = null;
    float movingTime;
    float moveDuration = 0.5f;
    private Vector2 dragPos;

    public static AssetDescriptor[] getRequiredFiles() {
        return new AssetDescriptor[]{
                FILE_CAVE_BG,
                FILE_CAVE_ATLAS
        };
    }

    public DungeonScreen(final RpgGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        mapViewport = new ExtendViewport(RpgGame.WIDTH, RpgGame.HEIGHT, RpgGame.WIDTH, (int)(RpgGame.HEIGHT * 1.5), camera);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        uiViewport = new ScreenViewport();

        bgTexture = (Texture) game.getAssetService().get(FILE_CAVE_BG);
        bgTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture,0,0,bgTexture.getWidth(),Gdx.graphics.getHeight());

        mapTextures = (TextureAtlas) game.getAssetService().get(FILE_CAVE_ATLAS);

        left = mapTextures.findRegion("left");
        straight = mapTextures.findRegion("straight");
        right = mapTextures.findRegion("right");
        marker = mapTextures.findRegion("marker");
    }

    public void enterDungeon(PlayerParty party, DungeonMap map, StoryAction onSuccess) {
        playerParty = party;
        dungeonMap = map;
        this.onSuccess = onSuccess;
        onFlee = null;
        fieldSprites = new LinkedList<>();
        // 2*150 padding + 50 sprite height
        maxScroll = dungeonMap.getDepth() * Y_DISTANCE + 350;
        bg.setRegionHeight((int) maxScroll);
        addSprite(dungeonMap.getStart());
        unfoldAll();
        buildUi();
        arriveAt(dungeonMap.getStart());
    }

    private void buildUi() {
        if (uiStage != null) {
            uiStage.dispose();
            uiStage.clear();
        }
        uiStage = new Stage(uiViewport, game.batch);
        Table root = new Table();
        root.setFillParent(true);
        root.pad(10);
        uiStage.addActor(root);

        Table table = new Table();
        portraits = new LinkedList<>();
        for (Character character: playerParty.getMembers()) {
            if (character instanceof PlayerCharacter) {
                table.row();
                final PlayerPortrait portrait = new PlayerPortrait((PlayerCharacter) character, game.getSkin());
                portrait.addListener(new ClickListener() {
                    public void clicked (InputEvent event, float x, float y) {
                        showPlayer(portrait.getPlayer());
                    }
                });
                table.add(portrait);
                portraits.add(portrait);
            }
        }

        table.defaults().space(20);
        root.add(table).left().padTop(60).expand().align(Align.topLeft);
        root.row();
        if (onFlee != null) {
            TextButton fleeButton = new TextButton("Flee", game.getSkin());
            fleeButton.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    fleeDungeon();
                }
            });
            root.add(fleeButton).align(Align.bottomRight);
        }

    }

    private void showPlayer(PlayerCharacter character) {
        game.showCharacter(character);
    }

    private void fleeDungeon() {
        onFlee.run(game, playerParty);
    }

    private void unfoldAll() {
        for (Field f: dungeonMap.getAllFields()) {
            if (f.isOpen()) {
                showConnections(f);
            }
        }
    }

    public void goTo(Field field) {
        movingTo = field;
        movingToSprite = getSprite(field);
        movingTime = 0;
        options.clear();
    }

    public void arriveAt(Field field) {
        current = field;
        currentHitBox = getSprite(field).getBoundaries();
        movingTo = null;
        Gdx.app.log("Game", "Moved to " + current.getMapPosY() + "x" + current.getMapPosY()
                + " type:" + current.getType() + " "
                + (current.isOpen() ? "(open)" : "(closed)"));
        if (!current.isOpen()) {
            switch (current.getType()) {
                case Field.TYPE_FINISH:
                    onSuccess.run(game, playerParty);
                    break;
                case Field.TYPE_MONSTER:
                    triggerFight(field);
                    break;
                case Field.TYPE_TREASURE:
                    for (Character character : playerParty.getMembers()) {
                        character.healsPercent(0.5f);
                        updateUi();
                        open(current);
                    }
                    break;
                case Field.TYPE_RETURN_FIELD:
                    focusCamera(dungeonMap.getStart());
                    arriveAt(dungeonMap.getStart());
                    break;
                default:
                    open(current);
            }
        } else {
            showConnections(current);
        }
    }

    public void open(Field field) {
        Gdx.app.log("Game", "opening " + field.getMapPosY() + "x" + field.getMapPosY()
                + (field.isOpen() ? "(open)" : "(closed)"));

        field.open();
        getSprite(field).updateSprite();
        showConnections(field);
    }

    private void showConnections(Field field) {
        options = new HashMap<>();
        for (Connection connection :field.getConnections()) {
            FieldSprite sprite = addSprite(dungeonMap.getField(connection.getDestination()));
            options.put(sprite.getBoundaries(), sprite.getField());
        }
    }

    public void triggerFight(Field field) {
        Party monsterParty = null;
        if (field instanceof EncounterField) {
            monsterParty = ((EncounterField)field).getEnemiesParty();
        } else {
            Beastiarum beastiarum = Beastiarum.getInstance();
            monsterParty = new Party(beastiarum.getRandomMonster());
            if (MathUtils.random(2) == 1) {
                monsterParty.getMembers().add(beastiarum.getRandomMonster());
            }
            if (MathUtils.random(2) == 1) {
                monsterParty.getMembers().add(beastiarum.getRandomMonster());
            }
        }
        game.startBattle(playerParty, monsterParty, this);
    }

    private FieldSprite getSprite(Field field) {
        FieldSprite sprite = null;
        for (FieldSprite f: fieldSprites) {
            if (f.getField() == field) {
                sprite = f;
                break;
            }
        }
        return sprite;
    }

    private FieldSprite addSprite(Field field) {
        FieldSprite sprite = getSprite(field);
        if (sprite == null) {
            sprite = new FieldSprite(field, mapTextures);
            sprite.setPosition(133 + 133 * field.getMapPosX(), 150 + field.getMapPosY() * Y_DISTANCE);
            fieldSprites.add(sprite);
        }
        return sprite;
    }

    @Override
    public void combatEnded(boolean playerWon) {
        updateUi();
        if (playerWon) {
            open(current);
        } else {
            game.gameOver();
        }
    }

    private void updateUi() {
        for (PlayerPortrait portrait: portraits) {
            portrait.updateHp();
        }
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        mapViewport.apply();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.setColor(1, 1, 1, 1);
        game.batch.draw(bg, 0, 0 , RpgGame.WIDTH, maxScroll);
        for (FieldSprite f: fieldSprites) {
            if (f.getField().isOpen()) {
                game.batch.setColor(Color.BROWN);
                for (Connection c: f.getField().getConnections()) {
                    if (c.getDirection() == Connection.LEFT) {
                        game.batch.draw(left, f.getX() - 133, f.getY());
                    }
                    if (c.getDirection() == Connection.STRAIGHT) {
                        game.batch.draw(straight, f.getX(), f.getY());
                    }
                    if (c.getDirection() == Connection.RIGHT) {
                        game.batch.draw(right, f.getX(), f.getY());
                    }
                }
            }
            game.batch.setColor(1, 1, 1, 1);
            f.draw(game.batch);
        }
        FieldSprite spot = getSprite(current);
        if (movingTo != null) {
            movingTime += delta;
            float movePercentage = movingTime / moveDuration;
            float x = spot.getX() * (1-movePercentage) + (movingToSprite.getX() * movePercentage);
            float y = spot.getY() * (1-movePercentage) + (movingToSprite.getY() * movePercentage);
            moveCamera(delta / moveDuration * Y_DISTANCE);
            game.batch.draw(marker, x, y + 20);
            if (movePercentage >= 1) {
                arriveAt(movingTo);
            }
        } else {
            if (dragPos == null) {
                game.batch.draw(marker, spot.getX(), spot.getY() + 20);
            } else {
                game.batch.draw(marker, dragPos.x-20, dragPos.y - 20);
            }
        }
        game.batch.end();

        // UI

        uiStage.getViewport().apply();
        game.batch.setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        //uiStage.getRoot().draw(game.batch, 1);
        uiStage.draw();
        uiStage.act(delta);

    }

    private void focusCamera(Field field) {
        safeMoveCamera(getSprite(field).getY());
    }

    private void moveCamera(float deltaY) {
        safeMoveCamera(camera.position.y + deltaY);
    }

    private void safeMoveCamera(float y) {
        y = MathUtils.clamp(y, camera.viewportHeight/2, maxScroll - (camera.viewportHeight/2));
        camera.position.set(camera.viewportWidth/2, y,0);
    }

        @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        mapViewport.update(width, height);
        focusCamera(current);
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
        uiStage.dispose();
        bgTexture.dispose();
        mapTextures.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 raw = new Vector3(x, y, 0);
        mapViewport.unproject(raw);
        Vector2 mousePos = new Vector2(raw.x, raw.y);
        if (currentHitBox.contains(mousePos)) {
            dragPos = mousePos;
            return true;
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Field f = getFieldAtUnprojected(x, y);
        if (f != null) {
            goTo(f);
            return true;
        }
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
        if (dragPos != null) {
            Vector3 raw = new Vector3(x, y, 0);
            mapViewport.unproject(raw);
            dragPos = new Vector2(raw.x, raw.y);
        } else {
            moveCamera(deltaY);
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if (dragPos != null) {
            Field f = getFieldAtUnprojected(x, y);
            if (f != null) {
                arriveAt(f);
            }
        }
        dragPos = null;
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

    /**
     * Shared code from drag and drop or tap, to see if navigation is possible
     * @param x
     * @param y
     */
    private Field getFieldAtUnprojected(float x, float y) {
        dragPos = null;
        Vector3 raw = new Vector3(x, y, 0);
        mapViewport.unproject(raw);
        Vector2 mousePos = new Vector2(raw.x, raw.y);

        Field f = null;
        // destinations change on move so only move after loop
        for (Map.Entry<Circle, Field> destination: options.entrySet()) {
            if (destination.getKey().contains(mousePos)) {
                f = destination.getValue();
            }
        }
        return f;
    }

    public void allowFlee(StoryAction onFlee) {
        this.onFlee = onFlee;
        buildUi();
    }
}
