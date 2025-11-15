package dungeonmania.entities.enemies;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy implements PotionListener {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    private Random randGen = new Random();

    private String movementType = "random";
    // private static DoMovement movementStrategy = new DoMovement();

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    private static final Map<String, MoveStrategy> MOVE = new HashMap<>();
    static {
        MOVE.put("random", new RandomMovement());
        MOVE.put("runAway", new FleeMovement());
    }

    @Override
    public void move(Game game) {
        Position nextPos = null;
        GameMap map = game.getMap();
        // switch (movementType) {
        // case "random":
        //     movementStrategy.setStrategy(new RandomMovement());
        //     break;
        // case "runAway":
        //     movementStrategy.setStrategy(new FleeMovement());
        //     break;
        // default:
        //     break;
        // }
        MoveStrategy movement = MOVE.get(movementType);
        nextPos = movement.move(this, game);
        // Position nextPos = movementStrategy.executeMovement(this, game);
        game.getMap().moveTo(this, nextPos);

    }

    @Override
    public void notifyPotion(Potion potion) {
        if (potion instanceof InvincibilityPotion)
            movementType = "runAway";
    }

    @Override
    public void notifyNoPotion() {
        movementType = "random";
    }

}
