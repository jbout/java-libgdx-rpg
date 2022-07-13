package lu.bout.rpg.battler.campaign;

import com.badlogic.gdx.Gdx;
import java.util.HashMap;

import lu.bout.rpg.battler.campaign.chapter.Chapter;

public class Campaign {

    public String name;
    private String startId;
    private HashMap<String, Chapter> chapters;

    public Campaign() {
        chapters = new HashMap<>();
    }

    public Chapter getStartChapter() {
        return getChapter(startId);
    }

    public Chapter getChapter(String id) {
        return chapters.get(id);
    }

    public void setStartChapter(Chapter chapter) {
        chapters.put(chapter.getId(), chapter);
        startId = chapter.getId();
    }

    public Chapter addChapter(Chapter chapter) {
        if (this.chapters.containsKey(chapter.getId())) {
            Gdx.app.log("Error", "Duplicate chapter id " + chapter.getId());
        }
        chapters.put(chapter.getId(), chapter);
        return chapter;
    }
}
