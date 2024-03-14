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
import lu.bout.rpg.battler.party.Person;
import lu.bout.rpg.battler.party.PlayerParty;

public class PartyActor extends Table {

    HashMap<Person, CharacterXpBar> map;

    public PartyActor(Skin skin) {
        super(skin);
        map = new HashMap<>();
        defaults().align(Align.left).pad(5);
    }

    public void setParty(PlayerParty party) {
        LinkedList<Person> toAdd = new LinkedList<>();
        LinkedList<Person> toRemove = new LinkedList<>(map.keySet());
        for (Person player: party.getPersons()) {
            if (map.containsKey(player)) {
                toRemove.remove(player);
            } else {
                toAdd.add(player);
            }
        }
        for (Person player: toRemove) {
            removeActor(map.get(player));
            map.remove(player);
        }
        for (Person player: toAdd) {
            addRow(player);
        }
    }
    public void addRow(Person player) {
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
