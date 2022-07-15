package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Map;

import lu.bout.rpg.engine.combat.participant.Participant;

public class CombatSprite extends Sprite {

    public static final float ATTACK_DURATION = 1f;

    private Map<Integer, String> textureMap;

    Participant participant;
    SimpleHealthbar healthBar;
    float baseX, baseY;
    BattleAnimation animation = null;
    float lastHp;
    Skin skin;

    public static CombatSprite createSprite(Participant participant, Skin skin) {
        Texture t;
        if (participant.getCharacter() instanceof BattleMini) {
            t = new Texture(((BattleMini)participant.getCharacter()).getMiniTextureName());
        } else {
            t = new Texture("enemy/pipo-enemy001.png");
        }
        return new CombatSprite(t, participant, skin);
    }

    public CombatSprite(Texture t, Participant participant, Skin skin) {
        super(t);
        this.skin = skin;
        this.participant = participant;
        this.healthBar = new SimpleHealthbar(this.getWidth() / 2, 10);
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
            super.draw(batch);
            healthBar.draw(batch, this.getX() + this.getWidth() / 4, this.getY(), lastHp);
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
    }

    public void setCenterXY(float x, float y) {
        setCenter(x,y);
        this.baseX = getX();
        this.baseY = getY();
    }

    public Participant getParticipant() {
        return participant;
    }
}
