package lu.bout.rpg.battler.saves;

import lu.bout.rpg.battler.campaign.Campaign;
import lu.bout.rpg.battler.campaign.CampaignState;
import lu.bout.rpg.battler.party.PlayerCharacter;
import lu.bout.rpg.battler.campaign.chapter.Chapter;
import lu.bout.rpg.battler.party.PlayerParty;
import lu.bout.rpg.engine.character.Character;
import lu.bout.rpg.engine.character.Party;

public class GameState {

    public static GameState newGame(PlayerParty party, Campaign campaign) {
        GameState state = new GameState();
        state.playerParty = party;
        state.playerCharacter = party.getPlayerCharacter();
        state.campaignState = new CampaignState(campaign);
        return state;
    }

    public int saveId;
    public PlayerCharacter playerCharacter;
    public PlayerParty playerParty;
    public CampaignState campaignState;

    public String getCampaignName() {
        return campaignState.getCampaignName();
    }

    public Chapter getCurrentChapter() {
        return campaignState.getCurrentChapter();
    }

    public PlayerParty getPlayerParty() {
        return playerParty;
    }
}
