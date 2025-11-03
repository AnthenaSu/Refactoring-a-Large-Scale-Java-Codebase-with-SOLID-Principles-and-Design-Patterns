package dungeonmania.entities.EntityFactories;

import org.json.JSONObject;

import dungeonmania.entities.ColorCodedType;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Portal;
import dungeonmania.util.Position;

public class PortalFactory implements EntityBuilder {

    @Override
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos) {
       return new Portal(pos, ColorCodedType.valueOf(jsonEntity.getString("colour")));
    }

}
