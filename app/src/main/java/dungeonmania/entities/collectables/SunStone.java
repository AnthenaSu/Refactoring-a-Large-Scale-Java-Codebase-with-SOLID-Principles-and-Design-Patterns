package dungeonmania.entities.collectables;
import dungeonmania.util.Position;

public class SunStone extends Treasure {

    public SunStone(Position position) {
        super(position);
    }

    /**
     * Identifies this treasure as a SunStone for crafting and door logic.
     */
    public boolean isSunStone() {
        return true;
    }

    /**
     * A SunStone can open any door, and is not consumed.
     * This method can be called by Door entities to check usable items.
     */
    public boolean canOpenDoor() {
        return true;
    }

    /**
     * Indicates whether the SunStone should be consumed in crafting.
     * Used for recipes where SunStone appears as an explicit listed ingredient.
     */
    public boolean isConsumedWhenCrafting() {
        return true;
    }

    /**
     * When SunStone is used as a substitute (e.g. for Treasure or Key),
     * it is not consumed.
     */
    public boolean isConsumedWhenSubstitute() {
        return false;
    }

    /**
     * Cannot be used to bribe mercenaries.
     */
    public boolean canBribe() {
        return false;
    }

}
