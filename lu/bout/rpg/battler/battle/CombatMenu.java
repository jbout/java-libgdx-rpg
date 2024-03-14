package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.shared.SubScreen;
import lu.bout.rpg.engine.character.Skill;
import lu.bout.rpg.engine.character.skill.CombatSkill;

public class CombatMenu extends Table implements SubScreen {

    BattleScreen screen;
    PlayerParty currentParty;

    public CombatMenu(final BattleScreen battleScreen) {
        super(battleScreen.game.getSkin());
        screen = battleScreen;
    }

    public void show(PlayerParty party) {
        if (!party.equals(currentParty)) {
            init(party);
            currentParty = party;
        }
        screen.map.setTargeting(party.getPlayer().getCharacter());
    }

    private void init(PlayerParty party) {
        clear();
        for (Skill skill: party.getPlayer().getCharacter().getSkills()) {
            if (skill instanceof CombatSkill) {
                final CombatSkill skillReference = (CombatSkill) skill;
                TextButton button = new TextButton(skill.getClass().getSimpleName(),getSkin());
                button.addListener(new ChangeListener() {
                    public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                        attack(skillReference);
                    }
                });
                add(button).fillX().pad(5);
                row();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta, Vector2 inputVector) {
        act(delta);
        draw(batch, 1);
    }

    private void attack(CombatSkill skill) {
        screen.launchAttackMinigame(screen.map.getTarget().getParticipant(), skill);
        screen.map.disableTargeting();
    }

}
