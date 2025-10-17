package dungeonmania.entities.collectables;

import dungeonmania.Game;

/**
 * Represents an item which can be used (consumed) by the player.
 *
 * For example, potions and weapons.
 */
public interface Useable {
    public void use(Game game);

}
