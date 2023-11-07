package lu.bout.rpg.battler.dungeon;

import com.badlogic.gdx.math.MathUtils;

import lu.bout.rpg.battler.world.Beastiarum;
import lu.bout.rpg.engine.character.Party;

public class CombatEncounterFactory {

    private static final float p = 0.33f;

    private Beastiarum beastiarum;

    public CombatEncounterFactory() {
        beastiarum = Beastiarum.getInstance();
    }

    public Party generateEnemyParty(int minlvl, int maxlvl) {
        return generateEnemyParty(MathUtils.random(minlvl, maxlvl));
    }

    public Party generateEnemyParty(int level) {
        // distribution: 25% 1, 50% 2, 25% 3
        int nrOfMonsters = 1 + MathUtils.random(1) + MathUtils.random(1);
        Party monsterParty = new Party();
        // only works with party 3 or smaller
        while (nrOfMonsters > 0) {
            if (nrOfMonsters == 1) {
                monsterParty.addMember(beastiarum.getMonsterByLevel(level));
            } else {
                int[] lvls = splitLevel(level);
                monsterParty.addMember(beastiarum.getMonsterByLevel(lvls[1]));
                level = lvls[0];
            }
            nrOfMonsters--;
        }
        return monsterParty;
    }

    /**
     * assumption powerlvl x + powerlvl y = powerlvl z
     * with aproximation of powerlevel 2^(x * p)
     * @param level
     * @return 2 levels that combined should be as strong as the desired lvl, with the higher lvl first
     */
    private int[] splitLevel(int level) {
        int lvl1 = MathUtils.random(1, Math.max(level-1,1));
        int lvl2 = (int) Math.round(Math.log(Math.pow(2, level*p) - Math.pow(2, lvl1*p)) / Math.log(2) / p);
        lvl2 = Math.max(lvl2, 1);
        return lvl1 >= lvl2 ? new int[]{lvl1, lvl2} : new int[]{lvl2, lvl1};
    }

    public Party generateEnemyParty() {
        return generateEnemyParty(2, 15);
    }
}
