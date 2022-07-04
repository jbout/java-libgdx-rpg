package lu.bout.rpg.battler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import lu.bout.rpg.battler.battle.BattleFeedback;
import lu.bout.rpg.battler.battle.BattleScreen;
import lu.bout.rpg.battler.map.MapFactory;
import lu.bout.rpg.battler.menu.GameOverScreen;
import lu.bout.rpg.battler.menu.HomeScreen;
import lu.bout.rpg.battler.map.MapScreen;
import lu.bout.rpg.battler.menu.VictoryScreen;
import lu.bout.rpg.engine.character.Party;
import lu.bout.rpg.engine.character.Player;
import lu.bout.rpg.engine.combat.Combat;
import lu.bout.rpg.engine.combat.Encounter;

public class RpgBattler extends Game implements BattleFeedback {

	public final static int WIDTH = 800; // 720
	public final static int HEIGHT = 1333; // 1200
	// TODO: scale sprites ly
	// TODO: use asset manager

	public HomeScreen homeScreen;
	public BattleScreen battleScreen;
	public MapScreen mapScreen;

	public Screen battleLauncher;
	private Preferences preferences;

	// shared resources
	public SpriteBatch batch;
	public BitmapFont font;

	private Player player;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		preferences = Gdx.app.getPreferences("minigame");

		battleScreen = new BattleScreen(this);
		mapScreen = new MapScreen(this);
		homeScreen = new HomeScreen(this);

		// debug
/*
		Beastiarum beastiarum = Beastiarum.getInstance();
		Party monsterParty = new Party(beastiarum.getRandomMonster());
		if (MathUtils.random(2) == 1) {
			monsterParty.getMembers().add(beastiarum.getRandomMonster());
		}
		if (MathUtils.random(2) == 1) {
			monsterParty.getMembers().add(beastiarum.getRandomMonster());
		}
		startBattle(new Encounter(new Party(new Player()), monsterParty, Encounter.TYPE_BALANCED), mapScreen);
*/
		// real
		backToMenu();
	}

	public void backToMenu() {
		this.setScreen(homeScreen);
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public void startBattle(Encounter encounter, MapScreen screen) {
		battleLauncher = screen;
		battleScreen.startBattle(encounter, this);
		this.setScreen(battleScreen);
	}

	public void launchDungeon() {
		player = new Player();
		MapFactory mapper = new MapFactory(15, 5);
		mapScreen.enterDungeon(new Party(player), mapper.generate());
		this.setScreen(mapScreen);
	}

	public void dungeonFinished() {
		this.setScreen(new VictoryScreen(this));
	}

	public void combatEnded(Combat combat) {
		if (player.getHp() > 0) {
			this.setScreen(battleLauncher);
			if (battleLauncher instanceof BattleFeedback) {
				((BattleFeedback) battleLauncher).combatEnded(combat);
			}
		} else {
			this.setScreen(new GameOverScreen(this));
		}
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		battleScreen.dispose();
	}
}
