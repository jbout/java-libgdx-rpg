package lu.bout.rpg.battler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import lu.bout.rpg.battler.battle.BattleFeedback;
import lu.bout.rpg.battler.battle.BattleScreen;
import lu.bout.rpg.battler.map.DungeonMap;
import lu.bout.rpg.battler.menu.GameOverScreen;
import lu.bout.rpg.battler.menu.HomeScreen;
import lu.bout.rpg.battler.map.MapScreen;
import lu.bout.rpg.character.Party;
import lu.bout.rpg.character.Player;
import lu.bout.rpg.combat.Combat;
import lu.bout.rpg.combat.Encounter;

public class RpgBattler extends Game implements BattleFeedback {

	public final static int WIDTH = 800; // 720
	public final static int HEIGHT = 1333; // 1200
	// TODO: scale sprites ly
	// TODO: use asset manager

	public HomeScreen homeScreen;
	public BattleScreen battleScreen;
	public MapScreen mapScreen;

	public Screen battleLauncher;

	// shared resources
	public SpriteBatch batch;
	public BitmapFont font;

	private Player player;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		battleScreen = new BattleScreen(this);
		mapScreen = new MapScreen(this);
		homeScreen = new HomeScreen(this);

		backToMenu();
	}

	public void backToMenu() {
		this.setScreen(homeScreen);
	}

	public void startBattle(Encounter encounter, MapScreen screen) {
		battleLauncher = screen;
		battleScreen.startBattle(encounter, this);
		this.setScreen(battleScreen);
	}

	public void launchDungeon() {
		player = new Player();
		mapScreen.enterDungeon(new Party(player), DungeonMap.generateMap(20, 0));
		this.setScreen(mapScreen);
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
