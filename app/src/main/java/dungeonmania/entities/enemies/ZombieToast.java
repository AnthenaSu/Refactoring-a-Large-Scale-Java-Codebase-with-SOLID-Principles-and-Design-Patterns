package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy implements PotionListener {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    private Random randGen = new Random();

    private String movementType = "random";
    private DoMovement movementStrategy = new DoMovement();

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        // Position nextPos = null;
        // GameMap map = game.getMap();
        switch (movementType) {
        case "random":
            movementStrategy.setStrategy(new RandomMovement());
            break;
        case "runAway":
            movementStrategy.setStrategy(new FleeMovement());
            break;
        default:
            break;
        }
        Position nextPos = movementStrategy.executeMovement(this, game);
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
