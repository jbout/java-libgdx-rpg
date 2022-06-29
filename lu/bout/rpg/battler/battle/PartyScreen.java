package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

import lu.bout.rpg.battler.RpgBattler;
import lu.bout.rpg.battler.SubScreen;
import lu.bout.rpg.character.Character;
import lu.bout.rpg.character.Party;

public class PartyScreen implements SubScreen {

    LinkedList<Character> playerCharacters;
    LinkedList<CombatSprite> characters = new LinkedList<CombatSprite>();

    @Override
    public void render(SpriteBatch batch, Vector2 inputVector) {
        // nothing to render
    }

    public void setParty(Party playerParty) {
        playerCharacters = playerParty.getMembers();
    }
}
