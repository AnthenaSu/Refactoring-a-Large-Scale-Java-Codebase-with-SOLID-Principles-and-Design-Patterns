package dungeonmania.entities.EntityFactories;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.util.Position;

public class SpiderFactory implements EntityBuilder {

    @Override
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos) {
        double spiderHealth = config.optDouble("spider_health", Spider.DEFAULT_HEALTH);
        double spiderAttack = config.optDouble("spider_attack", Spider.DEFAULT_ATTACK);
        return new Spider(pos, spiderHealth, spiderAttack);
    }

}
