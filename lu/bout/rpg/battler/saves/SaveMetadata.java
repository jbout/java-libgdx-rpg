package lu.bout.rpg.battler.saves;

/**
 * Class with identifiable information concerning a safe game
 */
public class SaveMetadata {

    public int id;
    public long timestamp;
    public String name;

    public int getId() {
        return id;
    }
}
