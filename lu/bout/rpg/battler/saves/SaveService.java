package lu.bout.rpg.battler.saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

/**
 * Service to store save-games, currently a single savegame is supported until interfaces catches up
 */
public class SaveService {

    private final static String PREFERENCES_KEY = "saves";

    private Preferences preferences;
    private Json json;

    private SaveMetadata[] saves;

    public SaveService() {
        preferences = Gdx.app.getPreferences(PREFERENCES_KEY);
        json = new Json();
    }

    public void add(GameState state) {
        SaveMetadata save = new SaveMetadata();
        save.id = this.getNextId();
        save.name = state.getCampaignName();
        state.saveId = save.getId();
        save(save, state);
    }

    public GameState restore(int id) {
        if (id != 1) {
            throw new RuntimeException("Invalid save game id");
        }
        return loadState(id);
    }

    public void update(GameState state) {
        SaveMetadata save = loadSave(state.saveId);
        save(save, state);
        preferences.flush();
    }

    public void remove(GameState state) {
        preferences.remove("save-" + state.saveId);
        // assuming the same
        preferences.remove("latest");
        preferences.flush();
    }

    public SaveMetadata[] getSaves() {
        SaveMetadata[] data;
        SaveMetadata latest = getLatest();
        if (latest != null) {
            data = new SaveMetadata[]{getLatest()};
        } else {
            data = new SaveMetadata[0];
        }
        return data;
    }

    /**
     * Returns the latest save if a valid latest exists
     *
     * @return latest Save OR NULL
     */
    public SaveMetadata getLatest() {
        SaveMetadata save = null;
        String jsonString = preferences.getString("latest", "");
        if (jsonString != "") {
            save = json.fromJson(SaveMetadata.class, jsonString);
        }
        return save;
    }

    private void save(SaveMetadata metadata, GameState state) {
        metadata.timestamp = System.currentTimeMillis();
        preferences.putString("latest", json.toJson(metadata));
        String jsonString = json.toJson(state);
        Gdx.app.log("Game", "Stored " + jsonString.length() + " characters of game state");
        preferences.putString("save-"+state.saveId, jsonString);
        preferences.flush();

    }

    private GameState loadState(int id) {
        String jsonString = preferences.getString("save-"+id);
        return json.fromJson(GameState.class, jsonString);
    }

    private SaveMetadata loadSave(int id) {
        // read from list
        return getLatest();
    }

    private int getNextId() {
        return 1;
    }
}
