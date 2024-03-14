package lu.bout.rpg.battler.campaign.storyAction;

import lu.bout.rpg.battler.RpgGame;

public class GoToChapterAction extends StoryAction {

    String chapterId;

    // Serializaton constructor
    public GoToChapterAction() {
    }

    public GoToChapterAction(String chapterId) {
        this.chapterId = chapterId;
    }

    public void run(RpgGame game) {
        game.goToChapter(chapterId);
    }
}
