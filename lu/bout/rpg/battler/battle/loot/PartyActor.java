package lu.bout.rpg.battler.battle.loot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.LinkedList;

import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.character.Party;

public class PartyActor extends Table {

    HashMap<Character, CharacterXpBar> map;

    public PartyActor(Skin skin) {
        super(skin);
        map = new HashMap<>();
        defaults().align(Align.left).pad(5);
    }

    public void setParty(Party party) {
        LinkedList<Character> toAdd = new LinkedList<>();
        LinkedList<Character> toRemove = new LinkedList<>(map.keySet());
        for (Character player: party.getMembers()) {
            if (map.containsKey(player)) {
                toRemove.remove(player);
            } else {
                toAdd.add(player);
            }
        }
        for (Character player: toRemove) {
            removeActor(map.get(player));
            map.remove(player);
        }
        for (Character player: toAdd) {
            addRow((PlayerCharacter) player);
        }
    }
    public void addRow(PlayerCharacter player) {
        CharacterXpBar actor = new CharacterXpBar(player, getSkin());
        PortraitService p = new PortraitService();
        add(new Image(new TextureRegionDrawable(new Texture(p.getScaled(player.getPortaitId(), 100 , 100)))));
        add(actor).growX();
        row();
        map.put(player, actor);
    }

    public void gainXp(int xp) {
        for (CharacterXpBar actor: map.values()) {
            actor.gainXp(xp);
        }
    }
}
