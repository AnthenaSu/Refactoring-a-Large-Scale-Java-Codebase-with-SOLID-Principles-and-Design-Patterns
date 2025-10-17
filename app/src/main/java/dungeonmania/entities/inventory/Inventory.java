package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Useable;
import dungeonmania.entities.collectables.Wood;

/**
 * Represents the contents of the player's inventory, containing all their collected and crafted items.
 */
public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    /** Add the given item to the inventory */
    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    /** Remove the given item from the inventory */
    public void remove(InventoryItem item) {
        items.remove(item);
    }

    /** Get the list of possible buildables */
    public List<String> getBuildables() {

        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        int treasure = count(Treasure.class);
        int keys = count(Key.class);
        List<String> result = new ArrayList<>();

        if (wood >= 1 && arrows >= 3) {
            result.add("bow");
        }
        if (wood >= 2 && (treasure >= 1 || keys >= 1)) {
            result.add("shield");
        }
        return result;
    }

    /**
     * Check whether a player has the supplies to build a particular buildable. If so, build the item.
     *
     * Currently since there are only two buildables we have a boolean to keep track of which buildable it is.
     *
     * @param p player object
     * @param remove whether to remove the build materials from the inventory while crafting the item.
     * @param forceShield if `true` always craft a shield, otherwise craft a bow if possible, otherwise a shield.
     * @param factory entity factory
     */
    public InventoryItem checkBuildCriteria(Player p, boolean remove, boolean forceShield, EntityFactory factory) {

        List<Wood> wood = getEntities(Wood.class);
        List<Arrow> arrows = getEntities(Arrow.class);
        List<Treasure> treasure = getEntities(Treasure.class);
        List<Key> keys = getEntities(Key.class);

        if (wood.size() >= 1 && arrows.size() >= 3 && !forceShield) {
            if (remove) {
                items.remove(wood.get(0));
                items.remove(arrows.get(0));
                items.remove(arrows.get(1));
                items.remove(arrows.get(2));
            }
            return factory.buildBow();

        } else if (wood.size() >= 2 && (treasure.size() >= 1 || keys.size() >= 1)) {
            if (remove) {
                items.remove(wood.get(0));
                items.remove(wood.get(1));
                if (treasure.size() >= 1) {
                    items.remove(treasure.get(0));
                } else {
                    items.remove(keys.get(0));
                }
            }
            return factory.buildShield();
        }
        return null;
    }

    /**
     * Return the first instance of an item matching the given type, or `null` if none are found.
     *
     * For example, to find the first piece of treasure, you could pass `Treasure.class`.
     *
     * This uses an `isInstance` check, so you can use interfaces and base classes too.
     */
    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    /**
     * Return the number of items which match the given type.
     *
     * For example, to determine how rich the player is, you could pass `Treasure.class` to count their treasure.
     *
     * This uses an `isInstance` check, so you can use interfaces and base classes too.
     */
    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    /** Return a reference to the entity with the given ID, else `null` if not found */
    public Entity getEntity(String entityId) {
        for (InventoryItem item : items)
            if (item.getId().equals(entityId))
                return item;
        return null;
    }

    /** Return all entities in the inventory */
    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    /**
     * Return a list of inventory items which match the given type.
     *
     * For example, to plunder the player's riches, you could pass `Treasure.class` to find all their treasure.
     *
     * This uses an `isInstance` check, so you can use interfaces and base classes too.
     */
    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    /** Return whether the player has a sword or a bow. */
    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    /**
     * Returns a reference to the player's active weapon. If the player has a sword, it uses that, otherwise, it uses a
     * bow.
     *
     * If the player has no weapons, it returns `null`.
     */
    public Useable getWeapon() {
        Useable weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

}
