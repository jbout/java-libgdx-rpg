package lu.bout.rpg.battler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import lu.bout.rpg.battler.battle.BattleScreen;
import lu.bout.rpg.battler.campaign.CampaignBuilder;
import lu.bout.rpg.battler.campaign.chapter.NarrativeChapter;
import lu.bout.rpg.battler.campaign.chapter.VictoryChapter;
import lu.bout.rpg.battler.campaign.screen.NarrativeScreen;
import lu.bout.rpg.battler.campaign.screen.GameOverScreen;
import lu.bout.rpg.battler.menu.HomeScreen;
import lu.bout.rpg.battler.map.MapScreen;
import lu.bout.rpg.battler.campaign.screen.VictoryScreen;
import lu.bout.rpg.battler.party.CharcterScreen;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.saves.GameState;
import lu.bout.rpg.battler.saves.SaveService;
import lu.bout.rpg.battler.campaign.chapter.Chapter;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;
import lu.bout.rpg.engine.character.Party;
import lu.bout.rpg.engine.combat.Encounter;

public class RpgGame extends Game {

	public final static int WIDTH = 800; // 720
	public final static int HEIGHT = 1333; // 1200
	// TODO: use asset manager

	public HomeScreen homeScreen;
	public BattleScreen battleScreen;
	public MapScreen mapScreen;
	public CharcterScreen charScreen;

	private Preferences preferences;
	private Skin sharedSkin;

	// shared resources
	public SpriteBatch batch;
	public BitmapFont font;

	private SaveService saveService;

	public GameState state;

	public void create() {

		// shared resources

		preferences = Gdx.app.getPreferences("minigame");
		batch = new SpriteBatch();
		font = new BitmapFont();
		sharedSkin = generateSkin();
		saveService = new SaveService();

		// init screens

		battleScreen = new BattleScreen(this);
		mapScreen = new MapScreen(this);
		homeScreen = new HomeScreen(this);
		charScreen = new CharcterScreen(this);

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
					this.setScreen(new VictoryScreen(this));
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
		this.setScreen(new VictoryScreen(this));
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

	// Generators

	private Skin generateSkin() {
		Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Amble-Light.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = Color.BLACK;
		parameter.borderWidth = 0;
		parameter.shadowOffsetX = 0;
		parameter.shadowOffsetY = 0;
		parameter.size = 36;
		BitmapFont font36 = generator.generateFont(parameter);
		parameter.color = Color.WHITE;
		BitmapFont font36white = generator.generateFont(parameter);
		generator.dispose();

		generator = new FreeTypeFontGenerator(Gdx.files.internal("font/bloody.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = Color.WHITE;
		parameter.borderWidth = 0;
		parameter.shadowOffsetX = 0;
		parameter.shadowOffsetY = 0;
		parameter.size = 36;
		parameter.characters = "0123456789 .KMGT+";
		BitmapFont bloodyFont = generator.generateFont(parameter);

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = font36;

		Label.LabelStyle whiteLabelStyle = new Label.LabelStyle();
		whiteLabelStyle.font = font36white;

		Label.LabelStyle bloodyLabelStyle = new Label.LabelStyle();
		bloodyLabelStyle.font = bloodyFont;


		skin.add("default", labelStyle);
		skin.add("white", whiteLabelStyle);
		skin.add("blood", bloodyLabelStyle);
		skin.get(TextButton.TextButtonStyle.class).font = font36;
		skin.get(SelectBox.SelectBoxStyle.class).font = font36;
		skin.get(SelectBox.SelectBoxStyle.class).listStyle.font = font36;
		skin.get(TextField.TextFieldStyle.class).font = font36;

		skin.add("healthbar", generateProgressBarStyle());

		return skin;
	}

	private ProgressBar.ProgressBarStyle generateProgressBarStyle() {
		ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();

		style.background = getColoredDrawable(10, 10, Color.RED);
		style.knob = getColoredDrawable(0, 10, Color.GREEN);
		style.knobBefore = getColoredDrawable(10, 10, Color.GREEN);

		return style;
	}

	private Drawable getColoredDrawable(int width, int height, Color color) {
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();

		TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

		pixmap.dispose();

		return drawable;
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		battleScreen.dispose();
	}
}
