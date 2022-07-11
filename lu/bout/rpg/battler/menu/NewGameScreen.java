package lu.bout.rpg.battler.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.CampaignBuilder;
import lu.bout.rpg.battler.map.MapFactory;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.saves.GameState;
import lu.bout.rpg.battler.campaign.chapter.DungeonChapter;

public class NewGameScreen extends MenuScreen {

    TextField playerNameField;

    public NewGameScreen(RpgGame rpgGame) {
        super(rpgGame);

        Label label = new Label("New Game",getTitleStyle());
        label.setSize(Gdx.graphics.getWidth(),200);
        label.setPosition(0,Gdx.graphics.getHeight()-400);
        label.setAlignment(Align.center);
        stage.addActor(label);

        playerNameField = new TextField("name", game.getSkin());
        playerNameField.setText(game.getPreferences().getString("lastname", "player"));
        playerNameField.setAlignment(Align.center);
        playerNameField.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        playerNameField.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() * 0.5f);

        stage.addActor(playerNameField);

        Button button0 = new TextButton("Create",game.getSkin());
        button0.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        button0.setPosition(Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() * 0.4f);
        button0.setColor(Color.GREEN);
        button0.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                createGame();
            }
        });
        stage.addActor(button0);
    }

    private void createGame() {
        stage.unfocusAll();
        Gdx.input.setOnscreenKeyboardVisible(false);
        game.getPreferences().putString("lastname", playerNameField.getText());
        game.getPreferences().flush();

        GameState state = GameState.newGame(new PlayerCharacter(playerNameField.getText()), (new CampaignBuilder()).justSingleDungeon());
        game.getSaveService().add(state);
        game.launchGame(state);
    }
}
