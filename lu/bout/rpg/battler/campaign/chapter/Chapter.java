package lu.bout.rpg.battler.campaign.chapter;

import java.util.LinkedList;
import java.util.List;

import lu.bout.rpg.battler.campaign.storyAction.StoryAction;

public class Chapter {

    List<StoryAction> before;
    protected String id;
    List<StoryAction> after;

    public Chapter() {
        before = new LinkedList<>();
        after = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public void addAfterAction(StoryAction a) {
        after.add(a);
    }

    public List<StoryAction> getAfter() {
        return after;
    }
}
