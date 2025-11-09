package dungeonmania.entities.EntityFactories;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.util.Position;

public class SunStoneFactory implements EntityBuilder {

    @Override
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos) {
        return new SunStone(pos);
    }
}
