package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable, PotionListener {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean wasAdjacentToPlayer = false;

    /** Type of movement to use */
    private String movementType = "hostile";
    private DoMovement movementStrategy = new DoMovement();

    // private MoveStrategy movementStrategy = new FollowPlayerStrategy();

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current mercenary can be bribed
     */
    private boolean canBeBribed(Player player) {
        // Position playerPos = player.getPosition();
        // int distance = Math.abs(playerPos.getX() - getPosition().getX())
        //         + Math.abs(playerPos.getY() - getPosition().getY());
        // return distance <= bribeRadius && player.countEntityOfType(Treasure.class) >= bribeAmount;
        Position playerPos = player.getPosition();
        int distance = Math.abs(playerPos.getX() - getPosition().getX())
                + Math.abs(playerPos.getY() - getPosition().getY());
        if (distance > bribeRadius) return false;

        // Count only Treasure that is NOT a SunStone
        long bribeableTreasureCount = player.getInventory().getEntities(Treasure.class).stream()
                .filter(t -> !(t instanceof SunStone))
                .count();

        return bribeableTreasureCount >= bribeAmount;
    }

    /**
     * bribe the mercenary
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void interact(Player player, Game game) {
        allied = true;
        movementType = "allied";
        bribe(player);
    }

    @Override
    public void move(Game game) {
        Position nextPos = null;
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        switch (movementType) {
        case "allied":
            boolean isAdjacentToPlayer = Position.isAdjacent(player.getPosition(), getPosition());
            if (wasAdjacentToPlayer && !isAdjacentToPlayer) {
                nextPos = player.getPreviousDistinctPosition();
            } else {
                // If currently still adjacent, wait in place. Else pursue the player.
                nextPos = isAdjacentToPlayer ? getPosition()
                        : map.dijkstraPathFind(getPosition(), player.getPosition(), this);
                wasAdjacentToPlayer = Position.isAdjacent(player.getPosition(), nextPos);
            }
            break;
        case "invisible":
            movementStrategy.setStrategy(new RandomMovement());
            nextPos = movementStrategy.executeMovement(this, game);
            break;
        case "invincible":
            movementStrategy.setStrategy(new FleeMovement());
            nextPos = movementStrategy.executeMovement(this, game);
            break;
        case "hostile":
            // nextPos = map.dijkstraPathFind(getPosition(), player.getPosition(), this);
            movementStrategy.setStrategy(new DijkstraMovement());
            nextPos = movementStrategy.executeMovement(this, game);
            break;
        default:
            break;
        }
        map.moveTo(this, nextPos);
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && canBeBribed(player);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied)
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }

    @Override
    public void notifyPotion(Potion potion) {
        if (allied)
            return;

        if (potion instanceof InvisibilityPotion)
            movementType = "invisible";
        if (potion instanceof InvincibilityPotion)
            movementType = "invincible";
    }

    @Override
    public void notifyNoPotion() {
        if (allied)
            return;

        movementType = "hostile";
    }
}
