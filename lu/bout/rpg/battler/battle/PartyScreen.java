package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

import lu.bout.rpg.battler.battle.map.CombatSprite;
import lu.bout.rpg.battler.shared.SubScreen;
import lu.bout.rpg.engine.character.CharacterSheet;
import lu.bout.rpg.engine.character.Party;

public class PartyScreen implements SubScreen {

    LinkedList<CharacterSheet> playerCharacterSheets;
    LinkedList<CombatSprite> characters = new LinkedList<CombatSprite>();

    @Override
    public void render(SpriteBatch batch, float delta, Vector2 inputVector) {
        // nothing to render
    }

    public void setParty(Party playerParty) {
        playerCharacterSheets = playerParty.getMembers();
    }
}
