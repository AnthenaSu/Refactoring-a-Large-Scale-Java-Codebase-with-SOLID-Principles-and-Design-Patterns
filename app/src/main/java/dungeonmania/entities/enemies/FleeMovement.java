package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FleeMovement implements MoveStrategy {

    @Override
    public Position move(Entity enemy, Game game) {
        Position nextPos = null;
        GameMap map = game.getMap();
        Player player = game.getPlayer();

        // Check whether the mercenary should flee left or right & up or down
        Position plrDiff = Position.calculatePositionBetween(map.getPlayer().getPosition(), enemy.getPosition());
        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(enemy.getPosition(), Direction.RIGHT)
                : Position.translateBy(enemy.getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(enemy.getPosition(), Direction.DOWN)
                : Position.translateBy(enemy.getPosition(), Direction.UP);
        Position offset = enemy.getPosition();
        // If on the same Y axis and can flee left or right, do so.
        if (plrDiff.getY() == 0 && map.canMoveTo(enemy, moveX))
            offset = moveX;
        // Or if on the same X axis and can flee up or down, do so.
        else if (plrDiff.getX() == 0 && map.canMoveTo(enemy, moveY))
            offset = moveY;
        // Prioritise Y movement if further away on the X axis
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else
                offset = enemy.getPosition();
            // Prioritise X movement if further away on the Y axis
        } else {
            if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else
                offset = enemy.getPosition();
        }
        return offset;
    }

}
