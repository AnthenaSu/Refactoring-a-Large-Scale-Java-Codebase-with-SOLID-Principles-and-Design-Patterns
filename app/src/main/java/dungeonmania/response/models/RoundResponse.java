package dungeonmania.response.models;

/**
 * Information about a round of battle.
 *
 * DO NOT CHANGE THIS FILE
 */
public class RoundResponse {
    private double deltaPlayerHealth;
    private double deltaEnemyHealth;

    /**
     * Create a round response object
     * @param deltaPlayerHealth amount that the player's health decreased by
     * @param deltaEnemyHealth amount that the enemy's health decreased by
     */
    public RoundResponse(double deltaPlayerHealth, double deltaEnemyHealth) {
        this.deltaPlayerHealth = deltaPlayerHealth;
        this.deltaEnemyHealth = deltaEnemyHealth;
    }

    public double getDeltaCharacterHealth() {
        return deltaPlayerHealth;
    }

    public double getDeltaEnemyHealth() {
        return deltaEnemyHealth;
    }

}
