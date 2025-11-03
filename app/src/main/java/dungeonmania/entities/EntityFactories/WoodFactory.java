package dungeonmania.entities.EntityFactories;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.util.Position;

public class WoodFactory implements EntityBuilder {

    @Override
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos) {
        return new Wood(pos);
    }

}
