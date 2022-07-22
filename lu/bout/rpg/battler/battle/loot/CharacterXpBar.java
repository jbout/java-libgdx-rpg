package lu.bout.rpg.battler.battle.loot;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.party.PlayerCharacter;

public class CharacterXpBar extends Table {

    private static final float XP_FILL_DURATION = 1;

    private final PlayerCharacter player;

    private final Label characterName;
    private final Label characterHealth;
    private final ProgressBar healthBar;
    private final Label characterXp;
    private final ProgressBar xpBar;

    private float animationRemaining;
    private int xpRemaining;
    private int totalXp;

    public CharacterXpBar(PlayerCharacter player, Skin skin) {
        super();
        this.player = player;

        characterName = new Label(player.getName(), skin, "small");

        characterHealth = new Label("", skin, "small");
        healthBar = new ProgressBar(0, player.getMaxhp(), 1, false, skin, "health");
        healthBar.setAnimateDuration(0);
        characterXp = new Label("", skin, "small");
        xpBar = new ProgressBar(0, player.xpToNextLevel(), 1, false, skin, "xp");
        xpBar.setAnimateDuration(0);
        xpBar.setValue(player.getXp());

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
        characterName.setText(player.getName() + " (lvl: " + player.getLevel() + ")");
        //= new Label(player.getName(), skin, "small");

        //characterKlass.setText(player.getKlass().getName() + " lvl: " + player.getLevel());
        characterHealth.setText("HP: " + player.getHp() + " / " + player.getMaxhp());
        healthBar.setRange(0, player.getMaxhp());
        healthBar.setValue(player.getHp());
        characterXp.setText("XP: " + player.getXp() + " / " + player.xpToNextLevel());
        xpBar.setAnimateDuration(0);
        xpBar.setRange(0, player.xpToNextLevel());
        xpBar.setValue(player.getXp());
    }

    public void gainXp(int xp) {
        totalXp = xp;
        updateFields();
        animateXp(xp);
    }

    public void animateXp(int xp) {
        int xpToLevel = player.xpToNextLevel() - player.getXp();
        if (xp > xpToLevel) {
            xpRemaining = xp - xpToLevel;
            xp = xpToLevel;
        } else {
            xpRemaining = 0;
        }
        player.earnXp(xp);
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

    public PlayerCharacter getPlayer() {
        return player;
    }
}
