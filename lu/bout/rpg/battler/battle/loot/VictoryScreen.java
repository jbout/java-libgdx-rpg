package lu.bout.rpg.battler.battle.loot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.battle.BattleFeedback;
import lu.bout.rpg.battler.battle.BattleScreen;
import lu.bout.rpg.engine.character.Party;

public class VictoryScreen implements Screen {

    private final RpgGame game;
    Stage stage;
    Image bgImage;
    Screen next;
    PartyActor partyList;
    private Dialog dialog;

    public VictoryScreen(RpgGame rpgGame) {
        game = rpgGame;
    }

    public void init() {
        stage = new Stage(new ScreenViewport());
        bgImage = new Image();
        stage.addActor(bgImage);
        dialog = new Dialog("", game.getSkin(), "dialog") {
            public void result(Object obj) {
                closeLootScreen();
            }
        };
        partyList = new PartyActor(game.getSkin());
        Label title = new Label("Victory", game.getSkin(), "title");
        dialog.getContentTable().add(title).expandX().align(Align.center);
        dialog.getContentTable().row();
        dialog.getContentTable().add(partyList).growX();
        dialog.button("OK").pad(25);
    }

    public void showLoot(final Party party, int xp, final Screen nextScreen) {
        if (stage == null) {
            init();
        }
        next = nextScreen;
        Pixmap screenShot = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TextureRegion region = new TextureRegion(new Texture(screenShot));
        region.flip(false, true);
        bgImage.setDrawable(new TextureRegionDrawable(region));
        bgImage.setBounds(
                0,0,
                screenShot.getWidth(),
                screenShot.getHeight()
        );
        screenShot.dispose();
        partyList.setParty(party);
        dialog.show(stage);
        partyList.gainXp(xp);
    }

    private void closeLootScreen() {
        if (next instanceof BattleFeedback) {
            ((BattleFeedback)next).combatEnded(true);
        }
        game.setScreen(next);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.getViewport().apply();
        ScreenUtils.clear(0, 0, 0, 1);
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

    }
}
