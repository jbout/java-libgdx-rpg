package lu.bout.rpg.battler.campaign.storyAction;

import lu.bout.rpg.battler.RpgGame;
import lu.bout.rpg.battler.campaign.CampaignState;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.party.PlayerParty;

public class GoToChapterAction extends StoryAction {

    String chapterId;

    // Serializaton constructor
    public GoToChapterAction() {
    }

    public GoToChapterAction(String chapterId) {
        this.chapterId = chapterId;
    }

    public void run(RpgGame game, PlayerParty party) {
        game.goToChapter(chapterId);
    }
}
