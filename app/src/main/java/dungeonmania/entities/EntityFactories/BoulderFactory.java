package dungeonmania.entities.EntityFactories;

import org.json.JSONObject;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class BoulderFactory implements EntityBuilder {

    @Override
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos) {
        return new Boulder(pos);
    }
}
