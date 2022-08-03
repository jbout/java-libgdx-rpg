package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import java.util.HashMap;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.assets.PortraitService;
import lu.bout.rpg.battler.campaign.Campaign;
import lu.bout.rpg.battler.campaign.CampaignBuilder;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.battler.saves.GameState;

public class NewGameScreen extends MenuScreen {

    private static final int PORTRAIT_OPTION_COUNT = 3;
    private static final int PORTRAIT_SIZE = 160;

    TextField playerNameField;

    ButtonGroup<Button> portraitGroup;
    HashMap<Button, Integer> buttonMap;
    Image[] portraits;
    int[] portraitIds;
    SelectBox<String> selectBox;

    public NewGameScreen(RpgGame rpgGame) {
        super(rpgGame);
    }

    @Override
    protected void init() {
        super.init();
        Table root = getRootTable();

        root.defaults().padTop(10);
        root.defaults().padBottom(50);

        Label label = new Label("New Game", game.getSkin(), "title");
        root.add(label);
        root.row();

        Table nameTable = new Table();
        nameTable.add(new Label("Player Name", game.getSkin())).fill();
        nameTable.row();
        playerNameField = new TextField("name", game.getSkin());
        playerNameField.setText(game.getPreferences().getString("lastname", "player"));
        playerNameField.setAlignment(Align.center);
        nameTable.add(playerNameField).fill().expandX();
        root.add(nameTable).fill().expandX();

        root.row();

        Table portraitTable = new Table();
        portraitTable.add(new Label("Portrait", game.getSkin())).fill();
        portraitTable.row();

        Table portraitSelect = new Table();
        portraits = new Image[PORTRAIT_OPTION_COUNT];
        portraitGroup = new ButtonGroup<>();
        Button.ButtonStyle style = new Button.ButtonStyle();
        Pixmap redSquare = new Pixmap(PORTRAIT_SIZE + 10, PORTRAIT_SIZE + 10, Pixmap.Format.RGB888);
        redSquare.setColor(Color.RED);
        redSquare.fill();
        style.checked = new TextureRegionDrawable(new Texture(redSquare));
        redSquare.dispose();
        buttonMap = new HashMap<>();
        for(int i = 0; i < PORTRAIT_OPTION_COUNT; i++) {
            portraits[i] = new Image();
            portraits[i].setScaling(Scaling.fit);
            Button portraitButton = new Button(style);
            buttonMap.put(portraitButton, i);
            portraitButton.add(portraits[i]).size(PORTRAIT_SIZE ,PORTRAIT_SIZE);
            portraitSelect.add(portraitButton).pad(10);
            portraitGroup.add(portraitButton);
        }
        generatePortraits();
        portraitSelect.row();

        TextButton shuffleButton = new TextButton("shuffle",game.getSkin());
        shuffleButton.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                generatePortraits();
            }
        });
        portraitSelect.add(shuffleButton).colspan(3);
        portraitTable.add(portraitSelect).expandX();
        root.add(portraitTable).fill();

        root.row();

        Table scenarioTable = new Table();
        scenarioTable.add(new Label("Scenario", game.getSkin())).fill();
        scenarioTable.row();
        String[] values = new String[]{"2 Step dungeon", "Free Roam"};
        selectBox = new SelectBox<>(game.getSkin());
        selectBox.setItems(values);
        selectBox.setAlignment(Align.center);
        scenarioTable.add(selectBox).fill().expandX();
        root.add(scenarioTable).fill().expandX();

        root.row();

        Table buttonTable = new Table();

        Button button0 = new TextButton("Cancel",game.getSkin());
        button0.setColor(Color.YELLOW);
        button0.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                game.showMenu();
            }
        });
        buttonTable.add(button0).padRight(20);

        Button button1 = new TextButton("Start",game.getSkin());
        button1.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                createGame();
            }
        });
        buttonTable.add(button1);

        root.add(buttonTable).align(Align.right);
    }

    private void generatePortraits() {
        PortraitService portraitService = new PortraitService();
        portraitIds = portraitService.getRandomIds(PORTRAIT_OPTION_COUNT);
        for (int i = 0; i < PORTRAIT_OPTION_COUNT; i++) {
            Texture t = portraitService.getPortrait(portraitIds[i]);
            portraits[i].setDrawable(new TextureRegionDrawable(new TextureRegion(t)));
        }
    }

    private void createGame() {
        stage.unfocusAll();
        Gdx.input.setOnscreenKeyboardVisible(false);
        game.getPreferences().putString("lastname", playerNameField.getText());
        game.getPreferences().flush();

        int selectedPortrait = buttonMap.get(portraitGroup.getChecked());
        int selectedScenario = selectBox.getSelectedIndex();
        Campaign campaign = selectedScenario == 0
            ? (new CampaignBuilder()).build2stepDungeon()
            : (new CampaignBuilder()).buildFreeRoamCampaign()
        ;

        PlayerCharacter player = new PlayerCharacter(playerNameField.getText(), portraitIds[selectedPortrait], 8);

        GameState state = GameState.newGame(new PlayerParty(player), campaign);
        game.getSaveService().add(state);
        game.launchGame(state);
    }
}
