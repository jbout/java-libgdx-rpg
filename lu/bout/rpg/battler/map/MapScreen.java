package lu.bout.rpg.battler.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.world.Beastiarum;
import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.character.Party;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.Encounter;

public class MapScreen implements Screen, GestureDetector.GestureListener, BattleFeedback {

    static final int Y_DISTANCE = 190;

    final RpgGame game;
    private final Viewport uiViewport;

    protected Stage uiStage;
    List<PlayerPortrait> portraits;

    OrthographicCamera camera;
    Viewport mapViewport;
    float maxScroll;

    TextureRegion bg;

    Texture left;
    Texture straight;
    Texture right;
    Texture marker;
    Texture swords;
    Texture portal;
    Texture heal;

    Texture dot;

    private DungeonChapter chapter;
    DungeonMap dungeonMap;
    PlayerParty playerParty;
    LinkedList<FieldSprite> fieldSprites;
    Map<Circle, Field> options;
    Field current;

    Field movingTo = null;
    FieldSprite movingToSprite = null;
    float movingTime;
    float moveDuration = 0.5f;

    public MapScreen(final RpgGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        mapViewport = new ExtendViewport(RpgGame.WIDTH, RpgGame.HEIGHT, RpgGame.WIDTH, (int)(RpgGame.HEIGHT * 1.5), camera);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        uiViewport = new ScreenViewport();

        Texture bgTexture = new Texture("map/cave_texture.jpg");
        bgTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture,0,0,bgTexture.getWidth(),Gdx.graphics.getHeight());

        // TODO use atlas
        left = new Texture("map/left.png");
        straight = new Texture("map/straight.png");
        right = new Texture("map/right.png");
        marker = new Texture("map/marker.png");
        swords = new Texture("map/swords.png");
        portal = new Texture("map/portal2.png");
        heal = new Texture("map/heal.png");

        Pixmap pixmap = new Pixmap(49, 49, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(24,24, 20);
        dot = new Texture(pixmap);
        dot = new Texture("map/dot.png");
    }

    public void enterDungeon(PlayerParty party, DungeonChapter chapter) {
        playerParty = party;
        this.chapter = chapter;
        dungeonMap = chapter.map;
        fieldSprites = new LinkedList<>();
        // 2*150 padding + 50 sprite height
        maxScroll = dungeonMap.depth * Y_DISTANCE + 350;
        bg.setRegionHeight((int) maxScroll);
        addSprite(dungeonMap.getStart());
        //unfoldAll();
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
        table.defaults().space(20);
        root.add(table).left().padTop(100);
        root.row();
        root.add().growX().expandY().bottom();

        portraits = new LinkedList<>();
        for (Character character: playerParty.getMembers()) {
            if (character instanceof PlayerCharacter) {
                table.row();
                PlayerPortrait portrait = new PlayerPortrait((PlayerCharacter) character, game.getSkin());
                portrait.addListener(new ClickListener() {
                    public void clicked (InputEvent event, float x, float y) {
                        showPlayer(game.state.playerCharacter);
                    }
                });
                table.add(portrait);
                portraits.add(portrait);
            }
        }
    }

    private void showPlayer(PlayerCharacter character) {
        game.showCharacter(character);
    }

    private void unfoldAll() {
        for (Field f: dungeonMap.getAllFields()) {
            open(f);
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
        movingTo = null;
        Gdx.app.log("Game", "Moved to " + current.getMapPosY() + "x" + current.getMapPosY()
                + " type:" + current.getType() + " "
                + (current.isOpen ? "(open)" : "(closed)"));
        if (!current.isOpen()) {
            switch (current.getType()) {
                case Field.TYPE_FINISH:
                    game.goToChapter(chapter.onSuccessChapterId);
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
                + (field.isOpen ? "(open)" : "(closed)"));

        field.open();
        showConnections(field);
    }

    private void showConnections(Field field) {
        options = new HashMap<>();
        for (Connection connection :field.getConnections()) {
            FieldSprite sprite = addSprite(connection.getDestination());
            options.put(sprite.getBoundaries(), connection.getDestination());
        }
    }

    public void triggerFight(Field field) {
        Beastiarum beastiarum = Beastiarum.getInstance();
        Party monsterParty = new Party(beastiarum.getRandomMonster());
        if (MathUtils.random(2) == 1) {
            monsterParty.getMembers().add(beastiarum.getRandomMonster());
        }
        if (MathUtils.random(2) == 1) {
            monsterParty.getMembers().add(beastiarum.getRandomMonster());
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
            sprite = new FieldSprite(field, dot, swords, portal, heal);
            sprite.setPosition(133 + 133 * field.getMapPosX(), 150 + field.getMapPosY() * Y_DISTANCE);
            fieldSprites.add(sprite);
        }
        return sprite;
    }

    @Override
    public void combatEnded(Combat combat, boolean playerWon) {
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
            game.batch.draw(marker, spot.getX(), spot.getY() + 20);
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
        float offSetCurrent = getSprite(field).getY();
        camera.position.set(camera.viewportWidth/2,Math.max(camera.viewportHeight/2, offSetCurrent),0);
    }

    private void moveCamera(float deltaY) {
        float newY = camera.position.y + deltaY;
        newY = Math.max(camera.viewportHeight/2 , newY);
        newY = Math.min(maxScroll - (camera.viewportHeight/2) , newY);
        camera.position.set(camera.position.x,newY,0);
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
        // TODO lots of disposing
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 raw = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        mapViewport.unproject(raw);
        Vector2 mousePos = new Vector2(raw.x, raw.y);

        Field f = null;
        for (Map.Entry<Circle, Field> destination: options.entrySet()) {
            if (destination.getKey().contains(mousePos)) {
                f = destination.getValue();
            }
        }
        if (f != null) {
            goTo(f);
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
        moveCamera(deltaY);
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
