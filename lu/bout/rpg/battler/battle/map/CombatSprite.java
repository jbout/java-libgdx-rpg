package lu.bout.rpg.battler.battle.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import lu.bout.rpg.battler.battle.animation.AttackAnimation;
import lu.bout.rpg.battler.battle.animation.BattleAnimation;
import lu.bout.rpg.battler.battle.animation.DamagedAnimation;
import lu.bout.rpg.battler.battle.animation.DeathAnimation;
import lu.bout.rpg.battler.battle.grahics.SimpleBar;
import lu.bout.rpg.battler.party.Person;
import lu.bout.rpg.engine.combat.participant.Participant;

public class CombatSprite extends Sprite {

    public static final float ATTACK_DURATION = 1f;

    Skin skin;
    Participant participant;
    SimpleBar healthBar;
    SimpleBar unitTimer;
    StatusBar statusBar;
    Vector2 baseCenter;
    BattleAnimation animation = null;
    float lastHp;

    public static CombatSprite createPersonSprite(Person person, Participant participant, Skin skin) {
        Texture t = new Texture(person.getMiniTextureName());
        return new CombatSprite(t, participant, skin);

    }

    public static CombatSprite createMonsterSprite(Participant participant, Skin skin) {
        Texture t = MonsterArt.getBestSpriteTexture(participant.getCharacter());
        return new CombatSprite(t, participant, skin);
    }

    public CombatSprite(Texture t, Participant participant, Skin skin) {
        super(t);
        this.skin = skin;
        this.participant = participant;
        this.healthBar = new SimpleBar(Math.max(this.getWidth() / 2, 40), 10);
        this.unitTimer = new SimpleBar(Math.max(this.getWidth() / 3, 40), 5, SimpleBar.styleGrayOnGray);
        statusBar = new StatusBar();
        updateHealth();
    }

    @Override
    public void draw(Batch batch, float delta) {
        if (animation != null) {
            boolean done = animation.animate(delta);
            if (done) {
                animation = null;
            }
        }
        if (participant.isAlive() || animation != null) {
            final float xOffset = this.getX() + (this.getWidth() - healthBar.getWidth()) / 2;
            super.draw(batch);
            healthBar.draw(batch, xOffset, this.getY(), lastHp);
            unitTimer.draw(batch, xOffset, this.getY() + 10, participant.getCooldownPercentRemaining());
            statusBar.draw(batch, xOffset, this.getY() - StatusBar.ICON_HEIGHT);
            if (animation instanceof DamagedAnimation) {
                ((DamagedAnimation) animation).draw(batch);
            }
        }
    }

    public void drawDamaged(int damage) {
        animation = new DamagedAnimation(this, damage, skin);
    }

    public void drawDeath() {
        animation = new DeathAnimation(this);
    }

    public void drawAttack(CombatSprite enemy, int damage) {
        animation = new AttackAnimation(this, enemy, damage);
    }

    public void updateHealth() {
        float hp = participant.getCharacter().getHp();
        hp = hp < 0 ? 0 : hp;
        lastHp = hp / participant.getCharacter().getMaxhp();
        statusBar.setStatus(participant.getStatuses());
    }

    public void setBaseCenter(float x, float y) {
        setCenter(x,y);
        baseCenter = new Vector2(x,y);
    }

    public Vector2 getBaseCenter() {
        return new Vector2(baseCenter);
    }

    // Convenience helper
    public void setCenter(Vector2 center) {
        setCenter(center.x, center.y);
    }

    public Rectangle getHitbox() {
        return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public Participant getParticipant() {
        return participant;
    }
}