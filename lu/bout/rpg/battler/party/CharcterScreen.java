package lu.bout.rpg.battler.party;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.menu.MenuScreen;

public class CharcterScreen extends MenuScreen {

    private Screen returnTo;
    private PlayerCharacter character;
    private final Label characterName;
    private final Label characterKlass;
    private final Label characterHealth;
    private final ProgressBar healthBar;
    private final Label characterXp;
    private final ProgressBar xpBar;
    private final Image characterImage;

    private final HashMap<String, Label> attributeLabels;

	public CharcterScreen(final RpgGame game) {
        super(game);
        characterImage = new Image();
        characterName = new Label("name", game.getSkin(), "title");
        characterKlass = new Label("klass", game.getSkin());
        characterHealth = new Label("1/1", game.getSkin());
        healthBar = new ProgressBar(0, 1, 1, false, game.getSkin(), "health");
        healthBar.setAnimateDuration(0);
        characterXp = new Label("1/1", game.getSkin());
        xpBar = new ProgressBar(0, 1, 1, false, game.getSkin(), "xp");
        xpBar.setAnimateDuration(0);
        attributeLabels = new HashMap<>();
        attributeLabels.put("str", new Label("1", game.getSkin()));
        attributeLabels.put("con", new Label("1", game.getSkin()));
    }

    @Override
    protected void init() {
        super.init();

        Table root = getRootTable();
        root.defaults().expandY().pad(10);

        root.add(characterName).colspan(2);
        root.row();

        Table portrait = new Table();
        portrait.add(characterImage);
        portrait.row();
        portrait.add(characterKlass);


        root.add(portrait);

        Table details = new Table();
        details.defaults().space(20);

        Table health = new Table();
        health.add(characterHealth).align(Align.left);
        health.row();
        health.add(healthBar).grow();
        details.add(health).growX();
        details.row();
        Table xp = new Table();
        xp.add(characterXp).align(Align.left);
        xp.row();
        xp.add(xpBar).grow();
        details.add(xp).growX();
        root.add(details).grow();

        root.row();
        root.add(createAttributTable()).colspan(2).align(Align.left);

        root.row();
        Button button1 = new TextButton("Return",game.getSkin());
        button1.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                returnToPrevious();
            }
        });
        root.add(button1).colspan(2).align(Align.bottomRight);
    }

    public Table createAttributTable() {
        Table abilities = new Table();
        abilities.defaults().pad(5).align(Align.left);
        abilities.add(new Label("Abilities", game.getSkin())).colspan(2).padBottom(20);
        abilities.row();
        abilities.add(new Label("Strength", game.getSkin()));
        abilities.add(attributeLabels.get("str"));
        abilities.row();
        abilities.add(new Label("Constitution", game.getSkin()));
        abilities.add(attributeLabels.get("con"));
        return abilities;
    }

    public void showCharacter(final PlayerCharacter character) {
        this.character = character;
        characterName.setText(character.getName());
        PortraitService p = new PortraitService();
        characterImage.setDrawable(new TextureRegionDrawable(p.getPortrait(character.getPortaitId())));
        characterHealth.setText("HP: " + character.getHp() + " / " + character.getMaxhp());
        healthBar.setRange(0, character.getMaxhp());
        healthBar.setValue(character.getHp());
        characterXp.setText("XP: " + character.getXp() + " / " + character.xpToNextLevel());
        xpBar.setRange(0, character.xpToNextLevel());
        xpBar.setValue(character.getXp());
        characterKlass.setText(character.getKlass().getName() + " lvl: " + character.getLevel());
        attributeLabels.get("str").setText(character.getStrength());
        attributeLabels.get("con").setText(character.getConstitution());
        if (game.getScreen() != this) {
            returnTo = game.getScreen();
        }
        game.setScreen(this);
    }

    public void returnToPrevious() {
        game.setScreen(returnTo);
    }

}
