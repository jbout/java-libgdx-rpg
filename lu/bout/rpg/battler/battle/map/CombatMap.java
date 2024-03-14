package lu.bout.rpg.battler.battle.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.IntFloatMap;
import com.badlogic.gdx.utils.IntIntMap;

import java.util.Iterator;
import java.util.LinkedList;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.party.Person;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.engine.character.CharacterSheet;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.CombatListener;
import lu.bout.rpg.engine.combat.event.AttackEvent;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.event.DeathEvent;
import lu.bout.rpg.engine.combat.participant.Participant;

public class CombatMap extends Widget implements CombatListener {

	private static final AssetDescriptor FILE_CAVE_BG = new AssetDescriptor("cave_bg.PNG", Texture.class);
	private static final AssetDescriptor FILE_UP_ARROW = new AssetDescriptor("battle/uparrow.png", Texture.class);

	public static AssetDescriptor[] getRequiredFiles() {
		return new AssetDescriptor[]{
				FILE_CAVE_BG,
				FILE_UP_ARROW
		};
	}

	RpgGame game;

	Combat combat;

	private Texture upArrow;
	private LinkedList<CombatSprite> sprites;
	private CombatSprite target = null;
	private boolean targeting = false;
	private float elapsedTime = 0;

	public CombatMap(RpgGame game) {
		sprites = new LinkedList<>();
		this.game = game;
		upArrow = (Texture) game.getAssetService().get(FILE_UP_ARROW);
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				onClicked(x + getX(), y + getY());
			}
		});
	}

	public void setCombatants(PlayerParty party, Combat combat) {
		this.combat = combat;
		for (CombatSprite sprite: sprites) {
			sprite.getTexture().dispose();
		}
		sprites = new LinkedList<CombatSprite>();
		for (Participant participant: combat.getParticipants()) {
			Person person = party.getPerson(participant.getCharacter());
			CombatSprite e = person != null
					? CombatSprite.createPersonSprite(person, participant, game.getSkin())
					: CombatSprite.createMonsterSprite(participant, game.getSkin());

			sprites.add(e);
		}
		positionCombatSprites();
		target = null;
	}

	private void positionCombatSprites() {
		IntIntMap count = new IntIntMap();
		for (CombatSprite sprite: sprites) {
			int pos = sprite.getParticipant().getTeamId();
			count.getAndIncrement(pos, 0, 1);
		}

		IntFloatMap yPos = new IntFloatMap();
		for (Iterator<IntIntMap.Entry> it = count.iterator(); it.hasNext(); ) {
			IntIntMap.Entry entry = it.next();
			yPos.put(entry.key, getY() + (getHeight() / (count.size) * (entry.key + 0.5f)));
		}

		IntIntMap xPos = new IntIntMap();
		for (CombatSprite sprite: sprites) {
			int teamid = sprite.getParticipant().getTeamId();
			int pos = xPos.getAndIncrement(teamid, 0, 1);
			sprite.setBaseCenter(
					getWidth() / count.get(teamid, 0) * (pos + 0.5f),
					yPos.get(teamid, 0)
			);
		}
	}

	public void setTargeting(CharacterSheet playerCharacterSheet)
	{
		if (target == null || !target.getParticipant().isAlive()) {
			// find a new target
			LinkedList<Participant> targets = combat.getEnemies(combat.getParticipant(playerCharacterSheet));
			if (targets.size() > 0) {
				target = getSpriteforParticipant(targets.get(0));
			}
		}
		targeting = true;
	}

	public void disableTargeting()
	{
		targeting = false;
	}

	public CombatSprite getTarget() {
		return target;
	}

	public void renderAttack(AttackEvent attackEvent) {
		Participant attacker = attackEvent.getActor();
		Participant target = attackEvent.getTarget();
		getSpriteforParticipant(attacker).drawAttack(getSpriteforParticipant(target), attackEvent.getDamage());
	}

	public CombatSprite getSpriteforParticipant(Participant participant) {
		CombatSprite sprite = null;
		for (CombatSprite c: sprites) {
			if (c.getParticipant() == participant) {
				sprite = c;
				break;
			}
		}
		return sprite;
	}

	@Override
	public void act (float delta) {
		super.act(delta);
		elapsedTime += delta;
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		for (CombatSprite e : sprites) {
			e.draw(batch, elapsedTime);
		}
		if (targeting && target != null) {
			batch.draw(upArrow, target.getX() + (target.getWidth() - upArrow.getWidth()) / 2, target.getY() - 2 - upArrow.getHeight());
		}
		elapsedTime = 0;
	}

	@Override
	protected void sizeChanged () {
		positionCombatSprites();
	}

	public void dispose () {
		for (CombatSprite sprite: sprites) {
			sprite.getTexture().dispose();
		}
	}

	public void onClicked(float x, float y) {
		if (targeting) {
			Gdx.app.log("Game", "Testing click " + x + " : " + y);
			for (CombatSprite sprite: sprites) {
				Gdx.app.log("Game", "    box " + sprite.getHitbox().x + " : " + sprite.getHitbox().y);
				if (sprite.getHitbox().contains(x, y)) {
					target = sprite;
				}
			}
		}
	}

	@Override
	public void receiveCombatEvent(Combat combat, CombatEvent event) {
		if (event instanceof DeathEvent) {
			getSpriteforParticipant(((DeathEvent) event).getActor()).drawDeath();
			// need to find a new target
			if (targeting == true && target != null && !target.getParticipant().isAlive()) {
				LinkedList<Participant> targets = new LinkedList<>();
				for (Participant candidate: combat.getParticipants()) {
					if (candidate.isAlive() && candidate.getTeamId() == target.getParticipant().getTeamId()) {
						target = getSpriteforParticipant(candidate);
						break;
					}
				}
			}
		}
	}
}
