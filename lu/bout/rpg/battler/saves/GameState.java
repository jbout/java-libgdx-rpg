package lu.bout.rpg.battler.saves;

import lu.bout.rpg.battler.campaign.Campaign;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.campaign.chapter.Chapter;

public class GameState {

    public static GameState newGame(PlayerCharacter player, Campaign campaign) {
        GameState state = new GameState();
        state.playerCharacter = player;
        state.campaign = campaign;
        state.currentChapter = campaign.getStartChapter().getId();
        return state;
    }

    public int saveId;
    public PlayerCharacter playerCharacter;
    public Campaign campaign;
    public String currentChapter;

    public Chapter getCurrentChapter() {
        return campaign.getChapter(currentChapter);
    }
}
