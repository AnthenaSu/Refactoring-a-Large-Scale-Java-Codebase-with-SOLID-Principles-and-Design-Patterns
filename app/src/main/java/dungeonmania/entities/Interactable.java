package dungeonmania.entities;

import dungeonmania.Game;

/** Represents an entity that can be interacted with by the player */
public interface Interactable {
    /**
     * Called when the player interacts with this entity
     *
     * This function is responsible for applying any required state updates (eg removing items from the player's
     * inventory).
     */
    public void interact(Player player, Game game);
    /**
     * Return whether the player can currently interact with this entity.
     *
     * This can perform operations such as checking the contents of the player's inventory.
     */
    public boolean isInteractable(Player player);
}
