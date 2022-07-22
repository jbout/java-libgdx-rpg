package lu.bout.rpg.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.HashMap;
import java.util.LinkedList;

import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.character.Party;

public class RecyclerView<T extends Actor> extends Table {
/*
    Party party;

    HashMap<ViewHolder, T> hashMap;
    LinkedList<T> actors;

    public RecyclerView(Skin skin) {
        super(skin);
        actors = new LinkedList<>();
    }

    public void addActor(Actor actor) {
    }

    public void setParty(Party party) {
        this.party = party;
        for (T player: actors) {
            this.removeActor(player);
        }
        actors = new LinkedList<>();
        for (Character player: party.getMembers()) {
            if (player instanceof PlayerCharacter) {
                T p = new T((PlayerCharacter)player, getSkin());
                actors.add(p);
                add(p);
            }
        }
    }

    static abstract class ViewHolder() {
        public abstract Actor getActor();
    }
 */
}
