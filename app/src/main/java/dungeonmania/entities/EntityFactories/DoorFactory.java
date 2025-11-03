package dungeonmania.entities.EntityFactories;

import org.json.JSONObject;

import dungeonmania.entities.Door;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class DoorFactory implements EntityBuilder {

    @Override
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos) {
        return new Door(pos, jsonEntity.getInt("key"));
    }

}
