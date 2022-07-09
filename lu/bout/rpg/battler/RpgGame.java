package lu.bout.rpg.battler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import lu.bout.rpg.battler.battle.BattleFeedback;
import lu.bout.rpg.battler.battle.BattleScreen;
import lu.bout.rpg.battler.map.MapFactory;
import lu.bout.rpg.battler.menu.GameOverScreen;
import lu.bout.rpg.battler.menu.HomeScreen;
import lu.bout.rpg.battler.map.MapScreen;
import lu.bout.rpg.battler.menu.VictoryScreen;
import lu.bout.rpg.battler.party.CharcterScreen;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.SamplePlayer;
import lu.bout.rpg.battler.saves.GameState;
import lu.bout.rpg.battler.saves.SaveService;
import lu.bout.rpg.engine.character.Party;
import lu.bout.rpg.engine.character.Player;
import lu.bout.rpg.engine.combat.Combat;
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

	public void showCharacter(SamplePlayer character) {
		charScreen.showCharacter(character);
	}

	public void startBattle(Encounter encounter, MapScreen screen) {
		battleScreen.startBattle(encounter, screen);
		this.setScreen(battleScreen);
	}

	public void launchDungeon(PlayerCharacter player) {
		MapFactory mapper = new MapFactory(15, 5);
		mapScreen.enterDungeon(new Party(player), mapper.generate());
		this.setScreen(mapScreen);
	}

	public void launchGame(GameState state) {
		this.state = state;
		launchDungeon(state.playerCharacter);
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
		BitmapFont font36 = generator.generateFont(parameter); // font size 24 pixels
		generator.dispose();

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = font36;

		skin.get(TextButton.TextButtonStyle.class).font = font36;
		skin.get(SelectBox.SelectBoxStyle.class).font = font36;
		skin.get(SelectBox.SelectBoxStyle.class).listStyle.font = font36;
		skin.get(TextField.TextFieldStyle.class).font = font36;

		return skin;
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		battleScreen.dispose();
	}
}
