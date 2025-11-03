package dungeonmania.entities.EntityFactories;
import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public interface EntityBuilder {
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos);
}
