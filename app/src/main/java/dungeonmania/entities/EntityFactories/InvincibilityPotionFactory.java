package dungeonmania.entities.EntityFactories;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.util.Position;

public class InvincibilityPotionFactory implements EntityBuilder {

    @Override
    public Entity create(JSONObject jsonEntity, JSONObject config, Position pos) {
        int invincibilityPotionDuration = config.optInt("invincibility_potion_duration",
        InvincibilityPotion.DEFAULT_DURATION);
        return new InvincibilityPotion(pos, invincibilityPotionDuration);
    }

}
