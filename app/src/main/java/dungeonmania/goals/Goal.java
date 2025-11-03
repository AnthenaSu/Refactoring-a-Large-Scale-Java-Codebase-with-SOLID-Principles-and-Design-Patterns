package dungeonmania.goals;

import dungeonmania.Game;

public abstract class Goal {
    public abstract boolean achieved(Game game);
    public abstract String toString(Game game);
}
