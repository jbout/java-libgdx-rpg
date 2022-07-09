package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Map;

import lu.bout.rpg.battler.world.GameMonster;
import lu.bout.rpg.engine.character.Player;
import lu.bout.rpg.engine.combat.participant.Participant;

public class CombatSprite extends Sprite {

    public static final float ATTACK_DURATION = 1f;

    private Map<Integer, String> textureMap;

    Participant participant;
    SimpleHealthbar healthBar;
    float shakeDuration = 0;
    float baseX, baseY;
    BattleAnimation animation = null;
    float lastHp;

    public static CombatSprite createSprite(Participant participant, boolean isPlayer) {
        Texture t;
        if (isPlayer) {
            t = new Texture("enemy/sample-hero.png");
        } else {
            if (participant.getCharacter() instanceof GameMonster) {
                t = new Texture(((GameMonster)participant.getCharacter()).texture);
            } else {
                t = new Texture("enemy/pipo-enemy001.png");
            }
        }
        return new CombatSprite(t, participant);
    }

    public CombatSprite(Texture t, Participant participant) {
        super(t);
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
        }
    }

    public void drawShake(float duration) {
        animation = new ShakeAnimation(this, duration);
    }

    public void drawDeath() {
        animation = new DeathAnimation(this);
    }

    public void drawAttack(CombatSprite enemy) {
        animation = new AttackAnimation(this, enemy);
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
