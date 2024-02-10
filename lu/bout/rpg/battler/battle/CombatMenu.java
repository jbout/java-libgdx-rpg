package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.shared.SubScreen;

public class CombatMenu extends Table implements SubScreen {

    TextButton attackButton;
    TextButton useItemButton;
    TextButton fleeButton;
    BattleScreen screen;
    PlayerParty currentParty;

    public CombatMenu(final BattleScreen battleScreen) {
        screen = battleScreen;
        attackButton = new TextButton("Attack",battleScreen.game.getSkin());
        attackButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                attack();
            }
        });
        add(attackButton).fillX().pad(5);
        row();
        useItemButton = new TextButton("Use Item",battleScreen.game.getSkin());
        add(useItemButton).fillX().pad(5);
        row();
        fleeButton = new TextButton("Flee",battleScreen.game.getSkin());
        add(fleeButton).fillX().pad(5);
    }

    public void show(PlayerParty party) {
        useItemButton.setDisabled(true);
        fleeButton.setDisabled(true);
        currentParty = party;
        screen.map.setTargeting(party.getPlayerCharacter());
    }

    @Override
    public void render(SpriteBatch batch, float delta, Vector2 inputVector) {
        draw(batch, 1);
        if (inputVector != null) {
            final Rectangle attackRect = new Rectangle(attackButton.getX(), attackButton.getY(), attackButton.getWidth(), attackButton.getHeight());
            if (attackRect.contains(inputVector)) {
                attack();
            }
            //final Rectangle fleeRect = new Rectangle(fleeButton.getX(), fleeButton.getY(), fleeButton.getWidth(), fleeButton.getHeight());
        }
    }

    private void attack() {
        screen.launchAttackMinigame(screen.map.getTarget().getParticipant());
        screen.map.disableTargeting();
    }

}
