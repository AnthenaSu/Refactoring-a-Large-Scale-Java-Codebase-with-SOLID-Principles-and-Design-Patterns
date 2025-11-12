package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface OverLap {
    public void onOverlap(GameMap map, Entity entity);
}
