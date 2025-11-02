package dungeonmania.entities.buildables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryBattle;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

/**
 * Represents an item which can be built (crafted) by the player.
 */
public abstract class Buildable extends InventoryBattle {
    public Buildable(Position position) {
        super(position);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        return;
    }

}
