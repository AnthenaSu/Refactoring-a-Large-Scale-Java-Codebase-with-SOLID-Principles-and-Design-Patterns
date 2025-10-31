package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class DijkstraMovement implements MoveStrategy {

    @Override
    public Position move(Entity enemy, Game game) {
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        return map.dijkstraPathFind(enemy.getPosition(), player.getPosition(), enemy);
    }
}
