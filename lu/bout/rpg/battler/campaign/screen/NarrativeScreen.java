package lu.bout.rpg.battler.campaign.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

        Rectangle surface = getUsableSurface();

        Label label = new Label(chapter.story, game.getSkin());
        label.setWrap(true);
        label.setSize(surface.getWidth(), surface.getHeight()-100);
        label.setPosition(surface.getX(), surface.getY()+100);
        label.setAlignment(Align.center);
        stage.addActor(label);

        TextButton continueButton = new TextButton("Continue",game.getSkin());
        continueButton.setSize(200, 80);
        continueButton.setPosition(surface.getX() + (surface.getWidth() / 2), surface.getY());
        continueButton.setColor(Color.GREEN);
        continueButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                advance();
            }
        });
        stage.addActor(continueButton);
    }

    public void advance() {
        game.goToChapter(chapter.next);
    }
}
