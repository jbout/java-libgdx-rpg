package lu.bout.rpg.battler.campaign.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.chapter.NarrativeChapter;
import lu.bout.rpg.battler.menu.MenuScreen;

public class NarrativeScreen extends MenuScreen {

    NarrativeChapter chapter;

    public NarrativeScreen(RpgGame game, final NarrativeChapter chapter) {
        super(game);
        this.chapter = chapter;
        Table root = getRootTable();

        Label label = new Label(chapter.story, game.getSkin());
        label.setWrap(true);
        label.setAlignment(Align.center);
        ScrollPane pane = new ScrollPane(label, game.getSkin());
        root.add(pane).fill().expand();

        root.row();
        TextButton continueButton = new TextButton("Continue",game.getSkin());
        continueButton.setColor(Color.GREEN);
        continueButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                advance();
            }
        });
        root.add(continueButton).align(Align.bottomRight);
    }

    public void advance() {
        game.goToChapter(chapter.next);
    }
}
