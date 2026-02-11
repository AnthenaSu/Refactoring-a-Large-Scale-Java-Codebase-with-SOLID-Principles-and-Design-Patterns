package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class DoMovement {
    private MoveStrategy moveStrategy;

    public void setStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    public Position executeMovement(Entity enemy, Game game) {
        return moveStrategy.move(enemy, game);
    }
}
