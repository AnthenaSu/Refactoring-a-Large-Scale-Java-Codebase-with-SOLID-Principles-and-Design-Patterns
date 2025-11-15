package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.inventory.InventoryBattle;

public class MidnightArmour extends InventoryBattle {

    private double extraDefense;
    private double extraAttack;

    public MidnightArmour(double extraDefense, double extraAttack) {
        super(null);
        this.extraDefense = extraDefense;
        this.extraAttack = extraAttack;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin,
            new BattleStatistics(0, extraAttack, extraDefense, 1, 1));
    }

    @Override
    public int getDurability() {
        return 1;
    }

    public String getType() {
        return "midnight_armour";
    }

}
