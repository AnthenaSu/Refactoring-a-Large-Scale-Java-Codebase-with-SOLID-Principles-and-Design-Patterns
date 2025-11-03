package dungeonmania.entities.EntityFactories;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.util.Position;

public class SwitchFactory implements EntityBuilder {

    @Override
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos) {
        return new Switch(pos);
    }
}
