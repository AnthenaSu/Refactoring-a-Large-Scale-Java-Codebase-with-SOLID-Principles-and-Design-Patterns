package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.OverLap;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryBattle;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Potion extends InventoryBattle implements OverLap {
    private int duration;

    public Potion(Position position, int duration) {
        super(position);
        this.duration = duration;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player player) {
            if (!player.pickUp(this))
                return;
            map.destroyEntity(this);
        }
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return origin;
    }

    @Override
    public int getDurability() {
        return 1;
    }
}
