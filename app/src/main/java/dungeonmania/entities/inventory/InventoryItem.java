package dungeonmania.entities.inventory;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

/**
 * An item in the inventory
 */
public abstract class InventoryItem extends Entity {
    public InventoryItem(Position position) {
        super(position);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public abstract void onOverlap(GameMap map, Entity entity);

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        return;
    }

    /**
     * Use this inventory item to apply a buff to the player's battle statistics (eg having a sword increases the
     * player's attacking power).
     */
    public abstract BattleStatistics applyBuff(BattleStatistics origin);

    /** Returns the durability of the item. */
    public abstract int getDurability();
}
