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

    public SaveService() {
        preferences = Gdx.app.getPreferences(PREFERENCES_KEY);
        json = new Json();
    }

    public void add(GameState state) {
        Save save = new Save();
        save.id = this.getNextId();
        save.name = "Any Save";
        state.saveId = save.getId();
        preferences.putString("save-"+save.getId(), json.toJson(state));
        storeSave(save);
    }

    public GameState restore(int id) {
        if (id != 1) {
            throw new RuntimeException("Invalid save game id");
        }
        String jsonString = preferences.getString("save-"+id);
        GameState state = json.fromJson(GameState.class, jsonString);
        return state;
    }

    public void update(GameState state) {
        Save save = getSave(state.saveId);
        storeSave(save);
        preferences.putString("save-"+save.getId(), json.toJson(state));
    }

    public void remove(GameState state) {
        preferences.remove("save-" + state.saveId);
        // assuming the same
        preferences.remove("latest");
    }

    public void getSaves() {

    }

    /**
     * Returns the latest save if a valid latest exists
     *
     * @return latest Save OR NULL
     */
    public Save getLatest() {
        Save save = null;
        String jsonString = preferences.getString("latest", "");
        if (jsonString != "") {
            save = json.fromJson(Save.class, jsonString);
        }
        return save;
    }

    private void storeSave(Save save) {
        // store in list
        save.timestamp = System.currentTimeMillis();
        preferences.putString("latest", json.toJson(save));
        preferences.flush();
    }

    private Save getSave(int id) {
        // read from list
        return getLatest();
    }

    private int getNextId() {
        return 1;
    }
}
