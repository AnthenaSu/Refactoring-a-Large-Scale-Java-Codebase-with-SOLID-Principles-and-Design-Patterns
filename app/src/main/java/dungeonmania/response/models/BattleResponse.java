package dungeonmania.response.models;

import java.util.List;

/**
 * Represents information about a battle, displayed to the player.
 *
 * DO NOT CHANGE THIS FILE
 */
public final class BattleResponse {
    private final String enemy;
    private final double initialPlayerHealth;
    private final double initialEnemyHealth;
    private final List<ItemResponse> battleItems;
    private final List<RoundResponse> rounds;

    /**
     * Create a battle response
     *
     * @param enemy enemy type (in snake_case)
     * @param rounds list of battle rounds
     * @param battleItems list of items involved in the battle
     * @param initialPlayerHealth health of the player before the battle commenced
     * @param initialEnemyHealth health of the enemy before the battle commenced
     */
    public BattleResponse(String enemy, List<RoundResponse> rounds, List<ItemResponse> battleItems,
            double initialPlayerHealth, double initialEnemyHealth) {
        this.initialPlayerHealth = initialPlayerHealth;
        this.initialEnemyHealth = initialEnemyHealth;
        this.enemy = enemy;
        this.rounds = rounds;
        this.battleItems = battleItems;
    }

    public final String getEnemy() {
        return enemy;
    }

    public final double getInitialPlayerHealth() {
        return initialPlayerHealth;
    }

    public final double getInitialEnemyHealth() {
        return initialEnemyHealth;
    }

    public final List<RoundResponse> getRounds() {
        return rounds;
    }

    public final List<ItemResponse> getBattleItems() {
        return battleItems;
    }
}
