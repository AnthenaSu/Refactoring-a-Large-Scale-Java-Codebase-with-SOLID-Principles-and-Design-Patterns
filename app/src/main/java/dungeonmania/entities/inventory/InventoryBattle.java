package dungeonmania.entities.inventory;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

/** A battle-capable item that can apply buffs and has durability */

public abstract class InventoryBattle extends InventoryItem {

    public InventoryBattle(Position position) {
        super(position);
    }

    @Override
    public abstract void onOverlap(GameMap map, Entity entity);

    public abstract BattleStatistics applyBuff(BattleStatistics origin);

    public abstract int getDurability();
}
