package lu.bout.rpg.battler.campaign;

import lu.bout.rpg.battler.campaign.chapter.Chapter;
import lu.bout.rpg.battler.campaign.storyAction.StoryAction;
import lu.bout.rpg.battler.party.PlayerParty;

public class CampaignState {

    Campaign campaign;
    String currentChapter;

    public CampaignState() {
    }

    public String getCampaignName() {
        return campaign.name;
    }

    public CampaignState(Campaign campaign) {
        this.campaign = campaign;
        this.currentChapter = campaign.getStartChapter().getId();
    }

    public Chapter getCurrentChapter() {
        return campaign.getChapter(currentChapter);
    }

    public Chapter transition(PlayerParty party, String nextChapterId) {
        for (StoryAction action: getCurrentChapter().getAfter()) {
            action.run(party);
        }
        currentChapter = nextChapterId;
        return getCurrentChapter();
    }
}
