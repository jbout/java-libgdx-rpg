package lu.bout.rpg.battler.dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.party.Person;

public class PlayerPortrait extends Table {
    Person player;
    Image portrait;
    ProgressBar healthBar;

    public PlayerPortrait(Person person, Skin skin) {
        super();
        player = person;
        PortraitService p = new PortraitService();
        portrait = new Image(p.getRoundPortrait(player.getPortaitId(), 99));
        add(portrait);
        Table subTable = new Table();
        Label playerName = new Label(player.getName(), skin, "white");
        playerName.setColor(Color.WHITE);
        subTable.add(playerName).left();

        healthBar = new ProgressBar(0, player.getCharacter().getMaxhp(), 1, false, skin, "health");
        healthBar.setAnimateDuration(0.0f);
        subTable.row();
        subTable.add(healthBar);
        add(subTable).spaceLeft(10);

        updateHp();
    }

    public void updateHp() {
        healthBar.setValue(player.getCharacter().getHp());
    }

    public Person getPerson() {
        return player;
    }
}
