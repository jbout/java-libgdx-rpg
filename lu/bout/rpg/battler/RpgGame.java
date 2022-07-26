package lu.bout.rpg.battler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import lu.bout.rpg.battler.assets.AssetService;
import lu.bout.rpg.battler.assets.SkinFactory;
import lu.bout.rpg.battler.battle.BattleScreen;
import lu.bout.rpg.battler.battle.loot.VictoryScreen;
import lu.bout.rpg.battler.campaign.chapter.NarrativeChapter;
import lu.bout.rpg.battler.campaign.chapter.VictoryChapter;
import lu.bout.rpg.battler.campaign.screen.GameWonScreen;
import lu.bout.rpg.battler.campaign.screen.NarrativeScreen;
import lu.bout.rpg.battler.campaign.screen.GameOverScreen;
import lu.bout.rpg.battler.menu.HomeScreen;
import lu.bout.rpg.battler.map.MapScreen;
import lu.bout.rpg.battler.party.CharcterScreen;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.saves.GameState;
import lu.bout.rpg.battler.saves.SaveService;
import lu.bout.rpg.battler.campaign.chapter.Chapter;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.engine.character.Party;

public class RpgGame extends Game {

	public final static int WIDTH = 800; // 720
	public final static int HEIGHT = 1333; // 1200

	private AssetService assetService;
	private Preferences preferences;
	private Skin sharedSkin;

	public HomeScreen homeScreen;
	public BattleScreen battleScreen;
	public MapScreen mapScreen;
	public CharcterScreen charScreen;
	private VictoryScreen lootScreen;

	public SpriteBatch batch;
	public BitmapFont font;

	private SaveService saveService;

	public GameState state;

	public void create() {

		// shared resources

		assetService = new AssetService();
		preferences = Gdx.app.getPreferences("minigame");
		batch = new SpriteBatch();
		font = new BitmapFont();
		SkinFactory skinFactory = new SkinFactory(this);
		assetService.preload(skinFactory);
		sharedSkin = skinFactory.generateSkin();
		saveService = new SaveService();

		// init screens

		battleScreen = new BattleScreen(this);
		assetService.preload(battleScreen);
		homeScreen = new HomeScreen(this);
		assetService.preload(homeScreen);
		assetService.preload(MapScreen.getRequiredFiles());
		mapScreen = new MapScreen(this);
		charScreen = new CharcterScreen(this);
		lootScreen = new VictoryScreen(this);

		showMenu();
	}

	public void showMenu() {
		this.setScreen(homeScreen);
	}

	public void showCharacter(PlayerCharacter character) {
		charScreen.showCharacter(character);
	}

	public void startBattle(PlayerParty party, Party monsters, MapScreen screen) {
		battleScreen.startBattle(party, monsters, screen);
		this.setScreen(battleScreen);
	}

	public void showLoot(Party p, int xp, Screen screen) {
		lootScreen.showLoot(p, xp, screen);
		this.setScreen(lootScreen);
	}


	protected void launchDungeon(PlayerParty party, DungeonChapter chapter) {
		mapScreen.enterDungeon(party, chapter);
		this.setScreen(mapScreen);
	}

	public void launchGame(GameState state) {
		this.state = state;
		renderChapter(state.getCurrentChapter());
	}

	public void goToChapter(String chapterId) {
		Chapter chapter = state.campaignState.transition(state.playerParty, chapterId);
		getSaveService().update(this.state);
		renderChapter(chapter);
	}

	private void renderChapter(Chapter chapter) {
		if (chapter instanceof DungeonChapter) {
			launchDungeon(state.getPlayerParty(), (DungeonChapter)chapter);
		} else {
			if (chapter instanceof NarrativeChapter) {
				this.setScreen(new NarrativeScreen(this, (NarrativeChapter) chapter));
			} else {
				if (chapter instanceof VictoryChapter) {
					this.setScreen(new GameWonScreen(this));
				} else {
					Gdx.app.log("Game", "Unmanaged Chapter " + chapter.getClass().getSimpleName());
					gameOver();
				}
			}
		}
	}

	public void gameOver() {
		saveService.remove(state);
		state = null;
		this.setScreen(new GameOverScreen(this));
	}

	public void dungeonFinished() {
		this.setScreen(new GameWonScreen(this));
	}

	// Shared resources

	public Preferences getPreferences() {
		return preferences;
	}
	
	public Skin getSkin() {
		return sharedSkin;
	}

	public SaveService getSaveService() {
		return saveService;
	}

	public AssetService getAssetService() {
		return assetService;
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		battleScreen.dispose();
	}
}
