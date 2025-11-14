package dungeonmania.entities.enemies.MercenaryBehaviors;

import dungeonmania.entities.Player;

public class MindControlBehaviour {

    private int ticksRemaining = 0;
    private boolean active = false;

    public void trigger(Player player, int duration) {
        this.active = true;
        this.ticksRemaining = duration;
    }

    public void tick() {
        if (!active) return;
        if (ticksRemaining < 0) active = false;
        ticksRemaining--;
    }

    public boolean isMindControlled() {
        return active;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }
}
