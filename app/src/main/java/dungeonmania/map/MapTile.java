package dungeonmania.map;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

/**
 * A class representing a single tile on the map.
 */
public class MapTile {
    private Position position;
    private List<Entity> entities = new ArrayList<>();

    private int weight = 1;

    public MapTile(Entity entity, int weight) {
        this(entity, entity.getPosition(), weight);
    }

    /** Create a map tile from the given entity. The entity's position is used as the tile's position. */
    public MapTile(Entity entity) {
        this(entity, entity.getPosition(), 1);
    }

    public MapTile(Entity entity, Position p, int weight) {
        this.position = p;
        this.entities.add(entity);
        this.weight = weight;
    }

    /**
     * Return whether the given entity can move onto this tile.
     *
     * This is `true` if and only iff all entities present on this tile are ok with the entity moving here.
     */
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entities.size() == 0 || entities.stream().allMatch(e -> e.canMoveOnto(map, entity));
    }

    /**
     * Return the "weight" of this tile.
     *
     * This is used when pathfinding to encourage/discourage entities from choosing certain paths, especially if we
     * intend to implement things such as tiles which make entities move more slowly.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Add an entity to this tile.
     *
     * If the entity is already here, do nothing.
     */
    public void addEntity(Entity entity) {
        if (!this.entities.contains(entity))
            this.entities.add(entity);
    }

    /** Remove the given entity from this tile. */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /** Return the number of entities present on this tile */
    public int size() {
        return entities.size();
    }

    /** Merge entities from the other MapTile */
    public void mergeEntities(MapTile other) {
        other.entities.forEach(this::addEntity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Position getPosition() {
        return position;
    }
}
