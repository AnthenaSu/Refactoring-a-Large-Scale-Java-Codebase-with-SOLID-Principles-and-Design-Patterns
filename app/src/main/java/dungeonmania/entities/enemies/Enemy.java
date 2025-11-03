package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.PotionListener;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable {
    private BattleStatistics battleStatistics;

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player player) {
            map.battle(player, this);
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
        if (this instanceof PotionListener potionListener)
            map.removePotionListener(potionListener);
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }

    // protected Position getFleePosition(GameMap map, Position playerPos) {
    //     Position plrDiff = Position.calculatePositionBetween(map.getPlayer().getPosition(), getPosition());
    //     Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(getPosition(), Direction.RIGHT)
    //             : Position.translateBy(getPosition(), Direction.LEFT);
    //     Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(getPosition(), Direction.DOWN)
    //             : Position.translateBy(getPosition(), Direction.UP);
    //     Position offset = getPosition();
    //     // If on the same Y axis and can flee left or right, do so.
    //     if (plrDiff.getY() == 0 && map.canMoveTo(this, moveX))
    //         offset = moveX;
    //     // Or if on the same X axis and can flee up or down, do so.
    //     else if (plrDiff.getX() == 0 && map.canMoveTo(this, moveY))
    //         offset = moveY;
    //     // Prioritise Y movement if further away on the X axis
    //     else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
    //         if (map.canMoveTo(this, moveY))
    //             offset = moveY;
    //         else if (map.canMoveTo(this, moveX))
    //             offset = moveX;
    //         else
    //             offset = getPosition();
    //         // Prioritise X movement if further away on the Y axis
    //     } else {
    //         if (map.canMoveTo(this, moveX))
    //             offset = moveX;
    //         else if (map.canMoveTo(this, moveY))
    //             offset = moveY;
    //         else
    //             offset = getPosition();
    //     }
    //     return offset;
    // }

    /**
     * Movement logic for the enemy.
     *
     * When called, this enemy should move to a new position.
     */
    public abstract void move(Game game);

    public double getHealth() {
        return battleStatistics.getHealth();
    }
}
