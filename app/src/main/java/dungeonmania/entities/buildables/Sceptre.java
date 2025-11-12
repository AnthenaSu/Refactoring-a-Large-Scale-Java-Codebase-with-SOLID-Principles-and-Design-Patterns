package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;

/**
 * A buildable sceptre that allows the player to mind control a mercenary.
 * The effect lasts for a specified duration (from config).
 */
public class Sceptre extends Buildable {
    private final int mindControlDuration;

    public Sceptre(int mindControlDuration) {
        super(null);
        this.mindControlDuration = mindControlDuration;
    }

    /**
     * Returns the number of ticks the mind control lasts.
     */
    public int getMindControlDuration() {
        return mindControlDuration;
    }

    /**
     * Sceptre provides no battle buff.
     */
    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        // The sceptre has no effect in battle
        return origin;
    }

    /**
     * The sceptre has durability 1 (not consumed when used for mind control).
     */
    @Override
    public int getDurability() {
        return 1;
    }
}
