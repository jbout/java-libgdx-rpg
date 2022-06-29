package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntFloatMap;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;
import java.util.LinkedList;

import lu.bout.rpg.battler.RpgBattler;
import lu.bout.rpg.battler.SubScreen;
import lu.bout.rpg.battler.battle.minigame.GameFeedback;
import lu.bout.rpg.battler.battle.minigame.MiniGame;
import lu.bout.rpg.battler.battle.minigame.SimonSays;
import lu.bout.rpg.character.Player;
import lu.bout.rpg.combat.CombatListener;
import lu.bout.rpg.combat.Encounter;
import lu.bout.rpg.combat.Combat;
import lu.bout.rpg.combat.command.AttackCommand;
import lu.bout.rpg.combat.command.IdleCommand;
import lu.bout.rpg.combat.event.AttackEvent;
import lu.bout.rpg.combat.event.CombatEndedEvent;
import lu.bout.rpg.combat.event.CombatEvent;
import lu.bout.rpg.combat.event.DeathEvent;
import lu.bout.rpg.combat.event.ReadyEvent;
import lu.bout.rpg.combat.participant.Participant;

public class BattleScreen implements Screen, GameFeedback, CombatListener {

	static final int INITIAL_DIFFICULTY = 3;

	SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;

	private Texture bg;
	private Texture brick;

	private Combat combat;
	private BattleFeedback caller;
	private LinkedList<CombatSprite> sprites;
	private Participant player;
	boolean isCombatOver;

	private Vector3 touchPosRaw;
	private Vector2 touchPos;

	private SubScreen lowerScreen;
	private MiniGame minigame;
	private PartyScreen partyScreen;

	private boolean isPaused = true;
	private float waitTime = 0;

	private int difficulty = 3;

	public BattleScreen(final RpgBattler game) {
		minigame = new SimonSays(this,0,0,RpgBattler.WIDTH, RpgBattler.HEIGHT / 2);
		partyScreen = new PartyScreen();
		lowerScreen = partyScreen;
		create();
	}

	public void create () {
		batch = new SpriteBatch();
		bg = new Texture("cave_bg.PNG");
		brick = new Texture("cave_bricks_01.png");

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(RpgBattler.WIDTH, RpgBattler.HEIGHT, RpgBattler.WIDTH, (int)(RpgBattler.HEIGHT * 1.5), camera);
		viewport.apply();

		camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

		touchPosRaw = new Vector3();
		touchPos = new Vector2();
		sprites = new LinkedList<CombatSprite>();
	}

	public void startBattle(Encounter encounter, BattleFeedback caller) {
		this.caller = caller;
		Gdx.app.log("Game", "Starting encounter against " + encounter.getOpponentParty().getMembers().size() + " enemies");
		for (CombatSprite sprite: sprites) {
			sprite.getTexture().dispose();
		};
		sprites = new LinkedList<CombatSprite>();
		partyScreen.setParty(encounter.getPlayerParty());
		combat = new Combat(encounter);
		for (Participant participant: combat.getParticipants()) {
			if (participant.getCharacter() instanceof Player) {
				player = participant;
			}
			CombatSprite e = CombatSprite.createSprite(participant);
			sprites.add(e);
		}
		positionCombatSprites();
		combat.addListener(this);
		difficulty = INITIAL_DIFFICULTY;
		isPaused = false;
		isCombatOver = false;
	}

	public void endBattle() {
		isPaused = true;
		caller.combatEnded(combat);
	}

	private void positionCombatSprites() {
		float minigameHeight = RpgBattler.HEIGHT / 2;
		float combatHeight = viewport.getWorldHeight() - minigameHeight;
		IntIntMap count = new IntIntMap();
		for (CombatSprite sprite: sprites) {
			int pos = sprite.getParticipant().getTeamId();
			count.getAndIncrement(pos, 0, 1);
		}

		IntFloatMap yPos = new IntFloatMap();
		for (Iterator<IntIntMap.Entry> it = count.iterator(); it.hasNext(); ) {
			IntIntMap.Entry entry = it.next();
			yPos.put(entry.key, minigameHeight + (combatHeight / (count.size) * (entry.key + 0.5f)));
		}

		IntIntMap xPos = new IntIntMap();
		for (CombatSprite sprite: sprites) {
			int teamid = sprite.getParticipant().getTeamId();
			int pos = xPos.getAndIncrement(teamid, 0, 1);
			sprite.setCenterXY(
					RpgBattler.WIDTH / count.get(teamid, 0) * (pos + 0.5f),
					yPos.get(teamid, 0)
			);
		}
	}

	public void init () {

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		if (!isPaused) {
			if (waitTime == 0) {
				if (isCombatOver) {
					this.endBattle();
				} else {
					combat.advanceTimer(1);
				}
			} else {
				waitTime -= delta;
				waitTime = waitTime < 0 ? 0 : waitTime;
			}
		}
		boolean isTouched = Gdx.input.isTouched();
		if(isTouched) {
			touchPosRaw.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			viewport.unproject(touchPosRaw);
			touchPos.set(touchPosRaw.x, touchPosRaw.y);
		}

		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bg, 0, (RpgBattler.HEIGHT/2) , RpgBattler.WIDTH, RpgBattler.HEIGHT);
		batch.draw(brick, 0, 0, RpgBattler.WIDTH, RpgBattler.HEIGHT/2);

		for (CombatSprite e: sprites) {
			e.draw(batch, delta);
		}
		lowerScreen.render(batch, isTouched ? touchPos : null);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
		positionCombatSprites();
		Gdx.app.log("Game", "Resize : " + width + " - " + height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		for (CombatSprite sprite: sprites) {
			sprite.getTexture().dispose();
		};
		minigame.dispose();
		batch.dispose();
		bg.dispose();
	}

	@Override
	public void minigameEnded(boolean success, MiniGame game, long timeElapsed) {
		if (success) {
			difficulty++;
			player.setNextCommand(game.getCommandToRun());
		} else {
			difficulty = INITIAL_DIFFICULTY;
			player.setNextCommand(new IdleCommand());
		}
		lowerScreen = partyScreen;
		isPaused = false;
	}

	@Override
	public void receiveCombatEvent(CombatEvent event) {
		// TODO render attack animations
		if (event instanceof AttackEvent) {
			Participant target = ((AttackEvent) event).getTarget();
			getSpriteforParticipant(((AttackEvent) event).getActor()).drawAttack(getSpriteforParticipant(target));
			this.waitTime = CombatSprite.ATTACK_DURATION;
		}
		if (event instanceof DeathEvent) {
			getSpriteforParticipant(((DeathEvent) event).getActor()).drawDeath();
		}
		if (event instanceof ReadyEvent && ((ReadyEvent)event).getActor() == player) {
			Participant target = combat.getEnemies(player).get(0);
			if (target != null) {
				minigame.init(difficulty, new AttackCommand(target));
				lowerScreen = minigame;
				isPaused = true;
			}
		}
		if (event instanceof CombatEndedEvent) {
			isCombatOver = true;
		}
	}

	private CombatSprite getSpriteforParticipant(Participant participant) {
		CombatSprite sprite = null;
		for (CombatSprite c: sprites) {
			if (c.getParticipant() == participant) {
				sprite = c;
				break;
			}
		}
		return sprite;
	}
}
