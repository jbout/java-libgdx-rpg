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
import lu.bout.rpg.engine.character.Party;

public class VictoryDialog extends Dialog {

    private final RpgGame game;
    Screen next;
    PartyActor partyList;

    public VictoryDialog(RpgGame game) {
        super("", game.getSkin(), "dialog");
        this.game = game;
        partyList = new PartyActor(game.getSkin());
        Label title = new Label("Victory", game.getSkin(), "title");
        getContentTable().add(title).expandX().align(Align.center);
        getContentTable().row();
        getContentTable().add(partyList).growX();
        button("OK").pad(25);
    }

    public void showLoot(final Party party, int xp, final Screen nextScreen) {
        next = nextScreen;
        partyList.setParty(party);
        partyList.gainXp(xp);
    }

    public void result(Object obj) {
        if (next instanceof BattleFeedback) {
            ((BattleFeedback)next).combatEnded(true);
        }
        game.setScreen(next);
    }
}
