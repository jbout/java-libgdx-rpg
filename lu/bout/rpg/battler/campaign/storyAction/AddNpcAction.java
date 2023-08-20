package lu.bout.rpg.battler.campaign.storyAction;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.party.PlayerCharacter;

public class AddNpcAction extends OnceAction {

    PlayerCharacter character;

    // Serializaton constructor
    public AddNpcAction() {
    }

    public AddNpcAction(PlayerCharacter character) {
        this.character = character;
    }

    public void runOnce(RpgGame game) {
        // TODO dialog not working because screen switches before (switches to new location)
        Dialog d = new Dialog("", game.getSkin(), "dialog");
        Label title = new Label(character.getName() + " has joined your party", game.getSkin());
        title.setWrap(true);
        d.getContentTable().add(title).grow().align(Align.center);
        d.button("Continue");
        game.showDialog(d);
        game.state.getPlayerParty().addMember(character);
    }
}
