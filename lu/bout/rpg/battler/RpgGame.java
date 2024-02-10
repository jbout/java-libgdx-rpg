package lu.bout.rpg.battler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import lu.bout.rpg.battler.assets.AssetService;
import lu.bout.rpg.battler.battle.BattleScreen;
import lu.bout.rpg.battler.campaign.chapter.HubChapter;
import lu.bout.rpg.battler.campaign.chapter.NarrativeChapter;
import lu.bout.rpg.battler.campaign.chapter.VictoryChapter;
import lu.bout.rpg.battler.campaign.screen.GameWonScreen;
import lu.bout.rpg.battler.campaign.screen.NarrativeScreen;
import lu.bout.rpg.battler.campaign.screen.GameOverScreen;
import lu.bout.rpg.battler.campaign.storyAction.StoryAction;
import lu.bout.rpg.battler.dungeon.DungeonMap;
import lu.bout.rpg.battler.shared.DialogScreen;
import lu.bout.rpg.battler.menu.HomeScreen;
import lu.bout.rpg.battler.dungeon.DungeonScreen;
import lu.bout.rpg.battler.party.CharcterScreen;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.saves.GameState;
import lu.bout.rpg.battler.saves.SaveService;
import lu.bout.rpg.battler.campaign.chapter.Chapter;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.battler.shared.StageScreen;
import lu.bout.rpg.battler.world.city.Location;
import lu.bout.rpg.battler.world.city.LocationMap;
import lu.bout.rpg.battler.campaign.screen.LocationScreen;
import lu.bout.rpg.engine.character.Party;

public class RpgGame extends Game {

	private static final AssetDescriptor FILE_SKIN = new AssetDescriptor("skin/rpgskin.json", Skin.class);

	public final static int WIDTH = 800; // 720
	public final static int HEIGHT = 1333; // 1200

	private AssetService assetService;
	private Preferences preferences;
	private Skin sharedSkin;

	public HomeScreen homeScreen;
	public BattleScreen battleScreen;
	public DungeonScreen dungeonScreen;
	public CharcterScreen charScreen;
	private DialogScreen dialogScreen;
	private LocationScreen locationscreen;

	public SpriteBatch batch;
	public BitmapFont font;

	private SaveService saveService;

	public GameState state;

	public void create() {

		// shared resources

		assetService = new AssetService();
		assetService.preload(new AssetDescriptor[]{FILE_SKIN});
		preferences = Gdx.app.getPreferences("minigame");
		batch = new SpriteBatch();
		font = new BitmapFont();
		sharedSkin = (Skin) assetService.get(FILE_SKIN);
		saveService = new SaveService();

		// init screens

		battleScreen = new BattleScreen(this);
		assetService.preload(battleScreen);
		homeScreen = new HomeScreen(this);
		assetService.preload(homeScreen);
		assetService.preload(DungeonScreen.getRequiredFiles());
		dungeonScreen = new DungeonScreen(this);
		charScreen = new CharcterScreen(this);
		locationscreen = new LocationScreen(this);
		dialogScreen = new DialogScreen(this);

		showMenu();
	}

	public void showMenu() {
		this.setScreen(homeScreen);
	}

	public void showCharacter(PlayerCharacter character) {
		charScreen.showCharacter(character);
	}

	public void startBattle(PlayerParty party, Party monsters, DungeonScreen screen) {
		battleScreen.startBattle(party, monsters, screen);
		this.setScreen(battleScreen);
	}

	/**
	 * Dialogs work with Stages, so this is a workaround to work wit screen
	 * @param dialog
	 */
	public void showDialog(final Dialog dialog) {
		if (this.screen instanceof StageScreen) {
			dialog.show(((StageScreen) this.screen).getStage());
		} else {
			// legacy implementation, only works if dialog triggers navigation
			dialogScreen.showDialog(dialog);
			this.setScreen(dialogScreen);
		}
	}

	public void showLocation(LocationMap map, Location location) {
		locationscreen.showLocation(map, location);
		this.setScreen(locationscreen);
	}

	public void showDungeon(DungeonChapter chapter) {
		gotoDungeon(chapter.getMap(), chapter.getOnSuccessAction(), null);
	}

	public void gotoDungeon(DungeonMap map, StoryAction onSuccess, StoryAction onFlee) {
		dungeonScreen.enterDungeon(state.getPlayerParty(), map, onSuccess);
		if (onFlee != null) {
			dungeonScreen.allowFlee(onFlee);
		}
		this.setScreen(dungeonScreen);
	}

	public void launchGame(GameState state) {
		this.state = state;
		renderChapter(state.getCurrentChapter());
	}

	public void goToChapter(String chapterId) {
		Chapter chapter = state.campaignState.transition(this, chapterId);
		getSaveService().update(this.state);
		renderChapter(chapter);
	}

	private void renderChapter(Chapter chapter) {
		if (chapter instanceof DungeonChapter) {
			showDungeon((DungeonChapter)chapter);
		} else {
			if (chapter instanceof NarrativeChapter) {
				this.setScreen(new NarrativeScreen(this, (NarrativeChapter) chapter));
			} else {
				if (chapter instanceof VictoryChapter) {
					this.setScreen(new GameWonScreen(this));
				} else {
					if (chapter instanceof HubChapter) {
						HubChapter free = (HubChapter) chapter;
						showLocation(free.getMap(), free.getCurrentLocation());
					} else {
						Gdx.app.log("Game", "Unmanaged Chapter " + chapter.getClass().getSimpleName());
						gameOver();
					}
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
