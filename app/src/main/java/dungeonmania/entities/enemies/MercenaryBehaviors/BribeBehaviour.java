package dungeonmania.entities.enemies.MercenaryBehaviors;

import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;

public class BribeBehaviour {

    private int bribeAmount;
    private int bribeRadius;

    public BribeBehaviour(int bribeAmount, int bribeRadius) {
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
    }

    public boolean canBeBribed(Player player, int mercX, int mercY) {
        var playerPos = player.getPosition();
        int distance = Math.abs(playerPos.getX() - mercX)
                     + Math.abs(playerPos.getY() - mercY);

        if (distance > bribeRadius) return false;

        long bribeableTreasure = player.getInventory()
                .getEntities(Treasure.class).stream()
                .filter(t -> !(t instanceof SunStone))
                .count();

        return bribeableTreasure >= bribeAmount;
    }

    public void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }
    }

    public int getBribeAmount() {
        return bribeAmount;
    }

    public int getBribeRadius() {
        return bribeRadius;
    }
}
