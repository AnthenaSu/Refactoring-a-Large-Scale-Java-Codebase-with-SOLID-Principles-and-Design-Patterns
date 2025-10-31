package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class RandomMovement implements MoveStrategy {
    @Override
    public Position move(Entity enemy, Game game) {
        Position nextPos = null;
        GameMap map = game.getMap();
        Player player = game.getPlayer();

        Random randGen = new Random();
        List<Position> pos = enemy.getPosition().getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(enemy, p)).toList();
        if (pos.size() == 0) {
            nextPos = enemy.getPosition();
            map.moveTo(enemy, nextPos);
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
            map.moveTo(enemy, nextPos);
        }
        return nextPos;
    }
}
