package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
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

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.SubScreen;
import lu.bout.rpg.battler.assets.AssetConsumer;
import lu.bout.rpg.battler.battle.minigame.MiniGameFeedback;
import lu.bout.rpg.battler.battle.minigame.MiniGame;
import lu.bout.rpg.battler.battle.minigame.simonGame.SimonSays;
import lu.bout.rpg.battler.battle.minigame.lightsout.LightsoutGame;
import lu.bout.rpg.battler.battle.minigame.timingGame.TimingGame;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.character.Party;
import lu.bout.rpg.engine.combat.CombatListener;
import lu.bout.rpg.engine.combat.Encounter;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.command.AttackCommand;
import lu.bout.rpg.engine.combat.command.IdleCommand;
import lu.bout.rpg.engine.combat.event.AttackEvent;
import lu.bout.rpg.engine.combat.event.CombatEndedEvent;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.event.DeathEvent;
import lu.bout.rpg.engine.combat.event.ReadyEvent;
import lu.bout.rpg.engine.combat.participant.Participant;

public class BattleScreen implements Screen, MiniGameFeedback, CombatListener, AssetConsumer {

	private static final AssetDescriptor FILE_CAVE_BG = new AssetDescriptor("cave_bg.PNG", Texture.class);
	private static final AssetDescriptor FILE_CAVE_BRICK = new AssetDescriptor("cave_bricks_01.png", Texture.class);
	private static final AssetDescriptor FILE_UP_ARROW = new AssetDescriptor("battle/uparrow.png", Texture.class);

	enum CombatState {ongoing, won, lost};
	static final int INITIAL_DIFFICULTY = 3;

	RpgGame game;
	SpriteBatch batch;

	private OrthographicCamera camera;
	private Viewport viewport;

	private Texture bg;
	private Texture brick;
	private Texture upArrow;
	private LinkedList<CombatSprite> sprites;

	private Encounter encounter;
	private Combat combat;
	private Screen caller;
	private Participant player;
	CombatState state = CombatState.ongoing;

	private Vector3 touchPosRaw;
	private Vector2 touchPos;

	private SubScreen lowerScreen;
	private MiniGame minigame;
	private int minigameType = -1;
	private PartyScreen partyScreen;
	private Preferences stats;

	private CombatSprite target = null;
	private Participant lastTarget = null;

	private boolean isPaused = true;
	private float waitTime = 0;

	private int difficulty = 3;

	public BattleScreen(final RpgGame game) {
		this.game = game;
	}

	@Override
	public AssetDescriptor[] getRequiredFiles() {
		return new AssetDescriptor[]{
				FILE_CAVE_BG,
				FILE_CAVE_BRICK,
				FILE_UP_ARROW
		};
	}

	private void init() {
		partyScreen = new PartyScreen();
		lowerScreen = partyScreen;
		stats = Gdx.app.getPreferences("stats");
		batch = new SpriteBatch();
		bg = (Texture) game.getAssetService().get(FILE_CAVE_BG);
		brick = (Texture) game.getAssetService().get(FILE_CAVE_BRICK);
		upArrow = (Texture) game.getAssetService().get(FILE_UP_ARROW);

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(RpgGame.WIDTH, RpgGame.HEIGHT, RpgGame.WIDTH, (int)(RpgGame.HEIGHT * 1.5), camera);

		touchPosRaw = new Vector3();
		touchPos = new Vector2();
		sprites = new LinkedList<>();
	}

	public void startBattle(PlayerParty party, Party enemies, Screen caller) {
		if (batch == null) {
			init();
		}
		encounter = new Encounter(party, enemies, Encounter.TYPE_BALANCED);
		setupMinigame(); // might have changed
		this.caller = caller;
		Gdx.app.log("Game", "Starting encounter against " + encounter.getOpponentParty().getMembers().size() + " enemies");
		for (CombatSprite sprite: sprites) {
			sprite.getTexture().dispose();
		}
		sprites = new LinkedList<CombatSprite>();
		partyScreen.setParty(encounter.getPlayerParty());
		combat = new Combat(encounter);
		for (Participant participant: combat.getParticipants()) {
			boolean isPlayer = participant.getCharacter() == party.getPlayerCharacter();
			if (isPlayer) {
				player = participant;
			}
			CombatSprite e = CombatSprite.createSprite(participant, game.getSkin());
			sprites.add(e);
		}
		positionCombatSprites();
		combat.addListener(this);
		difficulty = INITIAL_DIFFICULTY;
		isPaused = false;
		state = CombatState.ongoing;
	}


	private void setupMinigame () {
		int minigameRequested = game.getPreferences().getInteger("minigame");
		if (minigameType != minigameRequested) {
			switch (minigameRequested) {
				case 1:
					minigame = new LightsoutGame(this, 0, 0, RpgGame.WIDTH, RpgGame.HEIGHT / 2);
					break;
				case 2:
					minigame = new TimingGame(this, 0, 0, RpgGame.WIDTH, RpgGame.HEIGHT / 2);
					break;
				case 0:
				default:
					minigame = new SimonSays(this, 0, 0, RpgGame.WIDTH, RpgGame.HEIGHT / 2);
			}
			minigameType = minigameRequested;
		}
	}

	private void endBattle() {
		isPaused = true;
		if (caller instanceof BattleFeedback) {
			((BattleFeedback)caller).combatEnded(combat, state == CombatState.won);
		}
		int xp = 0;
		for (Character monster :encounter.getOpponentParty().getMembers()) {
			xp += monster.getLevel() * 250;
		}
		// only show loot if the battle feedback did not trigger a navigation (ganeover)
		if (state == CombatState.won && game.getScreen() == this) {
			game.showLoot(encounter.getPlayerParty(), xp, caller);
		}
	}

	private void positionCombatSprites() {
		float miniGameHeight = RpgGame.HEIGHT / 2;
		float combatHeight = viewport.getWorldHeight() - miniGameHeight;
		IntIntMap count = new IntIntMap();
		for (CombatSprite sprite: sprites) {
			int pos = sprite.getParticipant().getTeamId();
			count.getAndIncrement(pos, 0, 1);
		}

		IntFloatMap yPos = new IntFloatMap();
		for (Iterator<IntIntMap.Entry> it = count.iterator(); it.hasNext(); ) {
			IntIntMap.Entry entry = it.next();
			yPos.put(entry.key, miniGameHeight + (combatHeight / (count.size) * (entry.key + 0.5f)));
		}

		IntIntMap xPos = new IntIntMap();
		for (CombatSprite sprite: sprites) {
			int teamid = sprite.getParticipant().getTeamId();
			int pos = xPos.getAndIncrement(teamid, 0, 1);
			sprite.setBaseCenter(
					RpgGame.WIDTH / count.get(teamid, 0) * (pos + 0.5f),
					yPos.get(teamid, 0)
			);
		}
	}

	public void setTarget(CombatSprite e) {
		target = e;
		lastTarget = e.getParticipant();
		minigame.changeCommand(new AttackCommand(target.getParticipant()));
	}

	@Override
	public void minigameEnded(boolean success, MiniGame game, long timeElapsed) {
		String statsKey = minigameType + "-" + difficulty + "-" + (success ? "s" : "l");
		stats.putInteger(statsKey, stats.getInteger(statsKey, 0) + 1);
		stats.flush();
		if (success) {
			difficulty++;
			player.setNextCommand(game.getCommandToRun());
		} else {
			difficulty = INITIAL_DIFFICULTY;
			player.setNextCommand(new IdleCommand());
		}
		lowerScreen = partyScreen;
		target = null;
		isPaused = false;
	}

	@Override
	public void receiveCombatEvent(CombatEvent event) {
		if (event instanceof AttackEvent) {
			this.renderAttack((AttackEvent) event);
		}
		if (event instanceof DeathEvent) {
			getSpriteforParticipant(((DeathEvent) event).getActor()).drawDeath();
		}
		if (event instanceof ReadyEvent && ((ReadyEvent)event).getActor() == player) {
			attemptAttack();
		}
		if (event instanceof CombatEndedEvent) {
			state = ((CombatEndedEvent) event).isPlayerWinner() ? CombatState.won : CombatState.lost;
		}
	}

	private void attemptAttack() {
		Participant potentialTarget = null;
		if (lastTarget != null && lastTarget.isAlive()) {
			potentialTarget = lastTarget;
		} else {
			LinkedList<Participant> targets = combat.getEnemies(player);
			if (targets.size() > 0) {
				potentialTarget = targets.get(0);
			}
		}
		if (potentialTarget != null) {
			lastTarget = potentialTarget;
			target = getSpriteforParticipant(potentialTarget);
			minigame.init(difficulty, new AttackCommand(potentialTarget));
			lowerScreen = minigame;
			isPaused = true;
		}
	}

	private void renderAttack(AttackEvent attackEvent) {
		Participant attacker = attackEvent.getActor();
		Participant target = attackEvent.getTarget();
		getSpriteforParticipant(attacker).drawAttack(getSpriteforParticipant(target), attackEvent.getDamage());
		waitTime = CombatSprite.ATTACK_DURATION;
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

	@Override
	public void show() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void render(float delta) {
		if (!isPaused) {
			if (waitTime == 0) {
				if (state != CombatState.ongoing) {
					this.endBattle();
				} else {
					combat.advanceTimer(Math.min(1, (int) (delta * 200)));
				}
			} else {
				waitTime = Math.max(0, waitTime - delta);
			}
		}
		boolean isTouched = Gdx.input.isTouched();
		if (isTouched) {
			touchPosRaw.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			viewport.unproject(touchPosRaw);
			touchPos.set(touchPosRaw.x, touchPosRaw.y);
		}

		ScreenUtils.clear(0, 0, 0, 1);
		viewport.apply();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bg, 0, (RpgGame.HEIGHT / 2), RpgGame.WIDTH, RpgGame.HEIGHT);
		batch.draw(brick, 0, 0, RpgGame.WIDTH, RpgGame.HEIGHT / 2);

		for (CombatSprite e : sprites) {
			e.draw(batch, delta);
			if (isTouched && e.getHitbox().contains(touchPos) && target != null) {
				setTarget(e);
			}
		}
		if (target != null) {
			batch.draw(upArrow, target.getX() + (target.getWidth() - upArrow.getWidth()) / 2, target.getY() - 2 - upArrow.getHeight());
		}
		lowerScreen.render(batch, delta, isTouched ? touchPos : null);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
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
		}
		minigame.dispose();
		batch.dispose();
		bg.dispose();
	}
}
