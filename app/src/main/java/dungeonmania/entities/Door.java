package dungeonmania.entities;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Door extends Entity implements OverLap {
    private boolean open = false;
    private int number;

    public Door(Position position, int number) {
        super(position.asLayer(Entity.DOOR_LAYER));
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (open || entity instanceof Spider) {
            return true;
        }
        // return (entity instanceof Player player && hasKey(player));
        if (entity instanceof Player player) {
            // Allow entry if player has matching key or any SunStone
            return hasKey(player) || hasSunStone(player);
        }
        return false;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player player))
            return;

        Inventory inventory = player.getInventory();
        Key key = inventory.getFirst(Key.class);

        // If SunStone is present, door opens without consuming it
        if (hasSunStone(player)) {
            open();
            return;
        }

        // Otherwise, use key normally (and consume it)
        if (hasKey(player)) {
            inventory.remove(key);
            open();
        }
    }

    /** Check whether the player has a key capable of unlocking this door */
    private boolean hasKey(Player player) {
        Inventory inventory = player.getInventory();
        Key key = inventory.getFirst(Key.class);

        return (key != null && key.getnumber() == number);
    }

    /** Check whether the player possesses at least one SunStone */
    private boolean hasSunStone(Player player) {
        Inventory inventory = player.getInventory();
        return inventory.getFirst(SunStone.class) != null;
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }
}
