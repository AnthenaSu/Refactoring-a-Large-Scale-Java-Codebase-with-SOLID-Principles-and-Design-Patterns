package dungeonmania.entities.EntityFactories;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.util.Position;

public class SwordFactory implements EntityBuilder {

    @Override
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos) {
       double swordAttack = config.optDouble("sword_attack", Sword.DEFAULT_ATTACK);
            int swordDurability = config.optInt("sword_durability", Sword.DEFAULT_DURABILITY);
            return new Sword(pos, swordAttack, swordDurability);
    }

}
