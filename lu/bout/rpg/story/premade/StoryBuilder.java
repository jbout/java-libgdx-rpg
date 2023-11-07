package lu.bout.rpg.story.premade;

import lu.bout.rpg.story.world.character.CompanionCharacter;
import lu.bout.rpg.story.world.character.PlayerCharacter;
import lu.bout.rpg.story.world.story.Chapter;
import lu.bout.rpg.story.world.story.Story;

public class StoryBuilder {

    public Story buildArenaElderScrolls() {
        Story s = new Story();

        // characters
        PlayerCharacter hero = new PlayerCharacter();
        CompanionCharacter ally = new CompanionCharacter();
        s.addCharacter(hero);

        // chapters
        Chapter introNarrative = new Chapter("introNarrative", "Intro");
        Chapter initialDungeon = new Chapter("dungeon1", "Prison");
        Chapter village = new Chapter("free", "Granitehall");
        Chapter finalDungeon = new Chapter("dungeon2", "Lair");
        Chapter victory = new Chapter("victory", "Victory");
        //s.addChapter(initialDungeon);
        return s;
    }
}
