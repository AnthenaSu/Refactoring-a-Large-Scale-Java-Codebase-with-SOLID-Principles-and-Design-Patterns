package dungeonmania.entities.buildables;
import dungeonmania.entities.inventory.InventoryItem;

/**
 * A buildable sceptre that allows the player to mind control a mercenary.
 * The effect lasts for a specified duration (from config).
 */
public class Sceptre extends InventoryItem {
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
}
