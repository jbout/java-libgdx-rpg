package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.battle.map.CombatMap;
import lu.bout.rpg.battler.battle.map.CombatSprite;
import lu.bout.rpg.battler.battle.minigame.MiniGameActor;
import lu.bout.rpg.battler.battle.minigame.japanese.WordSearchGame;
import lu.bout.rpg.battler.shared.IStageScreen;
import lu.bout.rpg.battler.shared.SubScreen;
import lu.bout.rpg.battler.assets.AssetConsumer;
import lu.bout.rpg.battler.battle.loot.VictoryDialog;
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
import lu.bout.rpg.engine.combat.command.CombatCommand;
import lu.bout.rpg.engine.combat.command.IdleCommand;
import lu.bout.rpg.engine.combat.event.AttackEvent;
import lu.bout.rpg.engine.combat.event.CombatEndedEvent;
import lu.bout.rpg.engine.combat.event.CombatEvent;
import lu.bout.rpg.engine.combat.event.DeathEvent;
import lu.bout.rpg.engine.combat.event.ReadyEvent;
import lu.bout.rpg.engine.combat.participant.Participant;

public class BattleScreen implements IStageScreen, MiniGameFeedback, CombatListener, AssetConsumer {

	private static final AssetDescriptor FILE_CAVE_BG = new AssetDescriptor("cave_bg.PNG", Texture.class);
	private static final AssetDescriptor FILE_CAVE_BRICK = new AssetDescriptor("cave_bricks_01.png", Texture.class);
	private static final AssetDescriptor FILE_UP_ARROW = new AssetDescriptor("battle/uparrow.png", Texture.class);

	enum CombatState {ongoing, won, lost};
	static final int INITIAL_DIFFICULTY = 3;

	RpgGame game;
	SpriteBatch batch;

	private OrthographicCamera camera;
	private Viewport viewport;

	private Texture brick;

	private Encounter encounter;
	private Combat combat;
	private Screen caller;
	private Participant player;
	private PlayerParty party;
	CombatState state = CombatState.ongoing;

	private Actor lowerScreen;
	private MiniGame minigame;
	private int minigameType = -1;
	private PartyScreen partyScreen;
	private CombatMenu combatMenu;
	private VictoryDialog victoryDialog;
	private Preferences stats;

	private boolean isPaused = true;
	private float waitTime = 0;

	private int difficulty = 3;

	private Stage stage;

	private Table stageTable;

	private Drawable tableBackground;

	CombatCommand nextCommand;

	CombatMap map;

	private Cell container;


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

	public Stage getStage() {
		return stage;
	}

	private void init() {
		combatMenu = new CombatMenu(this);
		stats = Gdx.app.getPreferences("stats");
		batch = new SpriteBatch();
		brick = (Texture) game.getAssetService().get(FILE_CAVE_BRICK);

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(RpgGame.WIDTH, RpgGame.HEIGHT, RpgGame.WIDTH, (int)(RpgGame.HEIGHT * 1.5), camera);
		stage = new Stage(viewport, batch);
		stageTable = new Table();
		stageTable.setFillParent(true);
		tableBackground = new TextureRegionDrawable((Texture) game.getAssetService().get(FILE_CAVE_BG));
		stageTable.setBackground(tableBackground);
		stage.addActor(stageTable);

		map = new CombatMap(game);
		stageTable.add(map).fill().expand();
		stageTable.row();
		victoryDialog = new VictoryDialog(game);
		partyScreen = new PartyScreen();
		container = stageTable.add();
		container.height(RpgGame.WIDTH).expandX();
		setSubScreen(partyScreen);
	}

	public void startBattle(PlayerParty playerParty, Party enemies, Screen caller) {
		if (batch == null) {
			init();
		}
		party = playerParty;
		encounter = new Encounter(playerParty, enemies, Encounter.TYPE_BALANCED);
		setupMinigame(); // might have changed
		this.caller = caller;
		Gdx.app.log("Game", "Starting encounter against " + encounter.getOpponentParty().getMembers().size() + " enemies");
		partyScreen.setParty(playerParty);
		combat = new Combat(encounter);
		map.setCombatants(combat);
		for (Participant participant: combat.getParticipants()) {
			boolean isPlayer = participant.getCharacter() == playerParty.getPlayerCharacter();
			if (isPlayer) {
				player = participant;
			}
		}
		combat.addListener(this);
		combat.addListener(map);
		difficulty = INITIAL_DIFFICULTY;
		isPaused = false;
		state = CombatState.ongoing;
		showPlayerOptions();
	}


	private void setupMinigame () {
		int minigameRequested = game.getPreferences().getInteger("minigame");
		if (minigameType != minigameRequested) {
			if (minigame != null) {
				minigame.dispose();
			}
			switch (minigameRequested) {
				case 1:
					minigame = new LightsoutGame(this, 0, 0, RpgGame.WIDTH, RpgGame.HEIGHT / 2);
					break;
				case 2:
					minigame = new TimingGame(this, 0, 0, RpgGame.WIDTH, RpgGame.HEIGHT / 2);
					break;
				case 3:
					minigame = new WordSearchGame(this, 0, 0, RpgGame.WIDTH, RpgGame.HEIGHT / 2, game.getSkin().getFont("nobilty-48"));
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
		setSubScreen(partyScreen);
		int xp = 0;
		for (Character monster :encounter.getOpponentParty().getMembers()) {
			xp += monster.getLevel() * 250;
		}
		// only show loot if the battle feedback did not trigger a navigation (ganeover)
		if (state == CombatState.won && game.getScreen() == this) {
			victoryDialog.showLoot(party, xp, caller);
			victoryDialog.show(stage);
		} else {
			if (caller instanceof BattleFeedback) {
				((BattleFeedback)caller).combatEnded(false);
			}
		}
	}

	@Override
	public void minigameEnded(boolean success, MiniGame game, long timeElapsed) {
		String statsKey = minigameType + "-" + difficulty + "-" + (success ? "s" : "l");
		stats.putInteger(statsKey, stats.getInteger(statsKey, 0) + 1);
		stats.flush();
		if (success) {
			difficulty++;
			if (player.isReady()) {
				player.setNextCommand(game.getCommandToRun());
				showPlayerOptions();
			} else {
				// player no ready, queue
				nextCommand = game.getCommandToRun();
				setSubScreen(partyScreen);
			}
		} else {
			difficulty = INITIAL_DIFFICULTY;
			if (player.isReady()) {
				player.setNextCommand(new IdleCommand());
				showPlayerOptions();
			} else {
				nextCommand = new IdleCommand();
				setSubScreen(partyScreen);
			}
		}
		isPaused = false;
	}

	@Override
	public void receiveCombatEvent(Combat combat, CombatEvent event) {
		if (event instanceof AttackEvent) {
			this.renderAttack((AttackEvent) event);
		}
		if (event instanceof ReadyEvent && ((ReadyEvent)event).getActor() == player) {
			if (nextCommand != null) {
				player.setNextCommand(nextCommand);
				nextCommand = null;
				showPlayerOptions();
			} else {
				// player is still in minigame, waiting
				isPaused = true;
			}
		}
		if (event instanceof CombatEndedEvent) {
			state = ((CombatEndedEvent) event).isPlayerWinner() ? CombatState.won : CombatState.lost;
		}
	}

	private void showPlayerOptions() {
		setSubScreen(combatMenu);
		combatMenu.show(party);
	}

	public void launchAttackMinigame(Participant target) {
		minigame.init(difficulty, new AttackCommand(target));
		setSubScreen(minigame);
	}

	private void renderAttack(AttackEvent attackEvent) {
		map.renderAttack(attackEvent);
		waitTime = CombatSprite.ATTACK_DURATION;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		if (!isPaused) {
			if (waitTime == 0) {
				if (state != CombatState.ongoing) {
					this.endBattle();
				} else {
					combat.advanceTimer(Math.min(1, (int) (delta * 300)));
				}
			} else {
				waitTime = Math.max(0, waitTime - delta);
			}
		}

		ScreenUtils.clear(0, 0, 0, 1);
		viewport.apply();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		//container.height(container.getActorWidth());
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
		if (minigame != null) {
			minigame.dispose();
		}
		if (batch != null) {
			batch.dispose();
		}
		if (map != null) {
			map.dispose();
		}
		if (stage != null) {
			stage.dispose();
		}
	}

	private void setSubScreen(SubScreen sub) {
		if (lowerScreen != null) {
			lowerScreen.remove();
		}
		Actor actor = sub instanceof Actor ? (Actor) sub : new MiniGameActor(sub, brick);
		lowerScreen = actor;
		container.setActor(actor);
	}
}
