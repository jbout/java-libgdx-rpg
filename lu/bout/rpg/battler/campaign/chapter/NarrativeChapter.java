package lu.bout.rpg.battler.campaign.chapter;

public class NarrativeChapter extends Chapter {

    public String story;

    public String next;

    public NarrativeChapter() {
    }

    public NarrativeChapter(String id, String story) {
        this.id = id;
        this.story = story;
    }

    public void setNext(Chapter next) {
        this.next = next.getId();
    }

}
