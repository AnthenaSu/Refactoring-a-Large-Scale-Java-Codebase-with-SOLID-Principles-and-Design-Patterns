package dungeonmania.entities.enemies;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.MercenaryBehaviors.BribeBehaviour;
import dungeonmania.entities.enemies.MercenaryBehaviors.MindControlBehaviour;
import dungeonmania.entities.enemies.MercenaryBehaviors.MovementBehaviour;
import dungeonmania.map.DijkstraPathFinder;
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

    private BribeBehaviour bribeBehaviour;
    private MindControlBehaviour mindControlBehaviour;
    private MovementBehaviour movementBehaviour;

    // private DoMovement movementStrategy = new DoMovement();
    private static final Map<String, MoveStrategy> MOVE = new HashMap<>();
    static {
        MOVE.put("invisible", new RandomMovement());
        MOVE.put("invincible", new FleeMovement());
        MOVE.put("hostile", new DijkstraMovement());
    }

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
        this.bribeBehaviour = new BribeBehaviour(bribeAmount, bribeRadius);
        this.mindControlBehaviour = new MindControlBehaviour();
        this.movementBehaviour = new MovementBehaviour();
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

    @Override
    public void interact(Player player, Game game) {
        var sceptre = player.getInventory().getFirst(Sceptre.class);
        if (sceptre != null) {
            mindControlBehaviour.trigger(player, sceptre.getMindControlDuration());
            allied = true;
            movementBehaviour.setType("allied");
            return;
        }

        allied = true;
        movementBehaviour.setType("allied");
        bribeBehaviour.bribe(player);
    }

    @Override
    public void move(Game game) {
        Position nextPos = null;
        GameMap map = game.getMap();
        Player player = game.getPlayer();

        if (mindControlBehaviour.isMindControlled()) {
            mindControlBehaviour.tick();
            if (!mindControlBehaviour.isMindControlled()) {
                allied = false;
                movementBehaviour.setType("hostile");
            }
        }
        if (movementBehaviour.getType().equals("allied")) {
            boolean isAdjacentToPlayer = Position.isAdjacent(player.getPosition(), getPosition());
            if (wasAdjacentToPlayer && !isAdjacentToPlayer) {
                nextPos = player.getPreviousDistinctPosition();
            } else {
                // If currently still adjacent, wait in place. Else pursue the player.
                nextPos = isAdjacentToPlayer ? getPosition()
                        : DijkstraPathFinder.findNext(map, getPosition(), player.getPosition(), this);
                wasAdjacentToPlayer = Position.isAdjacent(player.getPosition(), nextPos);
            }
        } else {
            MoveStrategy movement = MOVE.get(movementBehaviour.getType());
            nextPos = movement.move(this, game);
        }

        map.moveTo(this, nextPos);
    }

    @Override
    public boolean isInteractable(Player player) {
        // normal bribing condition
        boolean canBribeNormally = !allied
            && bribeBehaviour.canBeBribed(player, getPosition().getX(), getPosition().getY());

        // mind control with sceptre
        boolean hasSceptre = player.getInventory().getFirst(dungeonmania.entities.buildables.Sceptre.class) != null;

        return !allied && (canBribeNormally || hasSceptre);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied) return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }

    @Override
    public void notifyPotion(Potion potion) {
        if (allied) return;

        if (potion instanceof InvisibilityPotion)
            movementBehaviour.setType("invisible");
        if (potion instanceof InvincibilityPotion)
            movementBehaviour.setType("invincible");
    }

    @Override
    public void notifyNoPotion() {
        if (allied) return;
        movementBehaviour.setType("hostile");
    }


    public String getType() {
        return "mercenary";
    }

    public int getBribeAmount() {
        return bribeAmount;
    }

    public int getBribeRadius() {
        return bribeRadius;
    }
}
