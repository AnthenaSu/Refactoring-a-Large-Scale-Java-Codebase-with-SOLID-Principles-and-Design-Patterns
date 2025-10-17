package dungeonmania.entities;

import dungeonmania.entities.collectables.potions.Potion;

/**
 * Apply to objects which need to listen for updates on the player's potion effects.
 */
public interface PotionListener {
    /** Called when a potion effect is triggered on the player */
    public void notifyPotion(Potion potion);

    /** Called when all potion effects have expired, meaning there are now no potion effects affecting the player */
    public void notifyNoPotion();
}
