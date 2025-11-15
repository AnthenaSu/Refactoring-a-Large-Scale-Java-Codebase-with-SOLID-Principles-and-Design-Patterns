package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class BuildInventoryChecker {
    /** Get the list of possible buildables */
    public static List<String> getBuildables(Inventory inv) {

        int wood = inv.count(Wood.class);
        int arrows = inv.count(Arrow.class);
        int treasure = inv.count(Treasure.class);
        int keys = inv.count(Key.class);
        int sunstone = inv.count(SunStone.class);
        int sword = inv.count(Sword.class);
        List<String> result = new ArrayList<>();

        if (wood >= 1 && arrows >= 3) {
            result.add("bow");
        }
        if (wood >= 2 && (treasure >= 1 || keys >= 1 || sunstone >= 1)) {
            result.add("shield");
        }
        if ((wood >= 1 || arrows >= 2) && (keys >= 1 || treasure >= 1) && sunstone >= 1) {
            result.add("sceptre");
        }
        if (sword >= 1 && sunstone >= 1) {
            result.add("midnight_armour");
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
    public static InventoryItem checkBuildCriteria(Player p, boolean remove, boolean forceShield,
        EntityFactory factory, Inventory items) {

        List<Wood> wood = items.getEntities(Wood.class);
        List<Arrow> arrows = items.getEntities(Arrow.class);
        List<Treasure> treasure = items.getEntities(Treasure.class);
        List<SunStone> sunstone = items.getEntities(SunStone.class);
        List<Key> keys = items.getEntities(Key.class);

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

                // find a non-SunStone treasure/key first
                Treasure usedTreasure = treasure.stream()
                        .filter(t -> !(t instanceof dungeonmania.entities.collectables.SunStone))
                        .findFirst()
                        .orElse(null);

                if (usedTreasure != null) {
                    items.remove(usedTreasure); // consume normal treasure
                } else if (!keys.isEmpty()) {
                    items.remove(keys.get(0)); // consume key
                }
            }
            return factory.buildShield();
        } else if ((wood.size() >= 1 || arrows.size() >= 2)
        && (treasure.size() >= 1 || keys.size() >= 1) && sunstone.size() >= 1) {
            if (remove) {
                // consume 1 wood or 2 arrows
                if (wood.size() >= 1) {
                    items.remove(wood.get(0));
                } else {
                    items.remove(arrows.get(0));
                    items.remove(arrows.get(1));
                }

                // consume (key OR treasure OR sunstone)
                if (!keys.isEmpty()) {
                    items.remove(keys.get(0));
                } else if (!treasure.isEmpty()) {
                    items.remove(treasure.get(0));
                } else {
                    // use sunstone as replacement
                    items.remove(sunstone.get(0));
                }

                if (!sunstone.isEmpty()) {
                    items.remove(sunstone.get(0));
                }
            }
            return factory.buildSceptre();
        } else if (items.count(Sword.class) >= 1 && items.count(SunStone.class) >= 1) {
            // build midnight armour
            if (remove) {
                // consume 1 sword and 1 sun stone
                items.remove(items.getFirst(Sword.class));
                items.remove(items.getFirst(SunStone.class));
            }
            return factory.buildMidnightArmour();
        }
        return null;
    }
}
