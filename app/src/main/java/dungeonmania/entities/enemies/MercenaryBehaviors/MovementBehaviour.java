package dungeonmania.entities.enemies.MercenaryBehaviors;

public class MovementBehaviour {

    private String movementType = "hostile";

    public void setType(String type) {
        this.movementType = type;
    }

    public String getType() {
        return movementType;
    }
}
