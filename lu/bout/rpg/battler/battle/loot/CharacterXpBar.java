package lu.bout.rpg.battler.battle.loot;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.party.Person;
import lu.bout.rpg.engine.character.SupportsXp;

public class CharacterXpBar extends Table {

    private static final float XP_FILL_DURATION = 1;

    private final Person player;

    private final Label characterName;
    private final Label characterHealth;
    private final ProgressBar healthBar;
    private final Label characterXp;
    private final ProgressBar xpBar;

    private float animationRemaining;
    private int xpRemaining;
    private int totalXp;

    public CharacterXpBar(Person player, Skin skin) {
        super();
        this.player = player;
        if (!(player.getCharacter() instanceof SupportsXp)) {
            // TODO not the right place to check
            throw new RuntimeException("Non Xp supporting entity got XP");
        }

        characterName = new Label(player.getName(), skin, "small");

        characterHealth = new Label("", skin, "small");
        healthBar = new ProgressBar(0, player.getCharacter().getMaxhp(), 1, false, skin, "health");
        healthBar.setAnimateDuration(0);
        characterXp = new Label("", skin, "small");
        xpBar = new ProgressBar(0, ((SupportsXp)player.getCharacter()).xpToNextLevel(), 1, false, skin, "xp");
        xpBar.setAnimateDuration(0);
        xpBar.setValue(((SupportsXp)player.getCharacter()).getXp());

        defaults().expandY().padBottom(10);

        defaults();

        add(characterName);
        row();
        Table health = new Table();
        health.add(characterHealth).align(Align.left);
        health.row();
        health.add(healthBar).grow();
        add(health).growX();
        row();
        Table xp = new Table();
        xp.add(characterXp).align(Align.left);
        xp.row();
        xp.add(xpBar).grow();
        add(xp).growX();
        updateFields();
    }

    private void updateFields() {
        characterName.setText(player.getName() + " (lvl: " + player.getCharacter().getLevel() + ")");
        //= new Label(player.getName(), skin, "small");

        //characterKlass.setText(player.getKlass().getName() + " lvl: " + player.getLevel());
        characterHealth.setText("HP: " + player.getCharacter().getHp() + " / " + player.getCharacter().getMaxhp());
        healthBar.setRange(0, player.getCharacter().getMaxhp());
        healthBar.setValue(player.getCharacter().getHp());
        characterXp.setText("XP: " + ((SupportsXp)player.getCharacter()).getXp() + " / " + ((SupportsXp)player.getCharacter()).xpToNextLevel());
        xpBar.setAnimateDuration(0);
        xpBar.setRange(0, ((SupportsXp)player.getCharacter()).xpToNextLevel());
        xpBar.setValue(((SupportsXp)player.getCharacter()).getXp());
    }

    public void gainXp(int xp) {
        totalXp = xp;
        updateFields();
        animateXp(xp);
    }

    public void animateXp(int xp) {
        int xpToLevel = ((SupportsXp)player.getCharacter()).xpToNextLevel() - ((SupportsXp)player.getCharacter()).getXp();
        if (xp > xpToLevel) {
            xpRemaining = xp - xpToLevel;
            xp = xpToLevel;
        } else {
            xpRemaining = 0;
        }
        ((SupportsXp)player.getCharacter()).earnXp(xp);
        float animateDuration = XP_FILL_DURATION * xp / totalXp;
        animationRemaining = animateDuration;
        xpBar.setAnimateDuration(animateDuration);
        xpBar.setValue(xpBar.getValue() + xp);
    }

    public void act (float delta) {
        super.act(delta);
        if (animationRemaining > 0) {
            animationRemaining -= delta;
            if (animationRemaining < 0) {
                updateFields();
                if (xpRemaining > 0) {
                    animateXp(xpRemaining);
                }
            }
        }
    }
}
