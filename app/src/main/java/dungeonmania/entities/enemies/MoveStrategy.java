package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public interface MoveStrategy {
    public Position move(Entity enemy, Game game);
}
