package dungeonmania.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import dungeonmania.entities.EntityFactories.ArrowFactory;
import dungeonmania.entities.EntityFactories.BombFactory;
import dungeonmania.entities.EntityFactories.BoulderFactory;
import dungeonmania.entities.EntityFactories.DoorFactory;
import dungeonmania.entities.EntityFactories.EntityBuilder;
import dungeonmania.entities.EntityFactories.ExitFactory;
import dungeonmania.entities.EntityFactories.InvincibilityPotionFactory;
import dungeonmania.entities.EntityFactories.InvisibilityPotionFactory;
import dungeonmania.entities.EntityFactories.KeyFactory;
import dungeonmania.entities.EntityFactories.MercenaryFactory;
import dungeonmania.entities.EntityFactories.PlayerFactory;
import dungeonmania.entities.EntityFactories.PortalFactory;
import dungeonmania.entities.EntityFactories.SpiderFactory;
import dungeonmania.entities.EntityFactories.SunStoneFactory;
import dungeonmania.entities.EntityFactories.SwitchFactory;
import dungeonmania.entities.EntityFactories.SwordFactory;
import dungeonmania.entities.EntityFactories.TreasureFactory;
import dungeonmania.entities.EntityFactories.WallFactory;
import dungeonmania.entities.EntityFactories.WoodFactory;
import dungeonmania.entities.EntityFactories.ZombieFactory;
import dungeonmania.entities.EntityFactories.ZombieToastSpawnerFactory;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.util.Position;

public class EntityFactory {
    private JSONObject config;
    private Random ranGen = new Random();
    private static final Map<String, EntityBuilder> BUILDERS = new HashMap<>();

    public EntityFactory(JSONObject config) {
        this.config = config;
    }

    static {
        BUILDERS.put("player", new PlayerFactory());
        BUILDERS.put("door", new DoorFactory());
        BUILDERS.put("key", new KeyFactory());
        BUILDERS.put("portal", new PortalFactory());
        BUILDERS.put("bomb", new BombFactory());
        BUILDERS.put("sword", new SwordFactory());
        BUILDERS.put("spider", new SpiderFactory());
        BUILDERS.put("zombie_toast", new ZombieFactory());
        BUILDERS.put("zombie_toast_spawner", new ZombieToastSpawnerFactory());
        BUILDERS.put("mercenary", new MercenaryFactory());
        BUILDERS.put("invisibility_potion", new InvisibilityPotionFactory());
        BUILDERS.put("invincibility_potion", new InvincibilityPotionFactory());
        BUILDERS.put("wall", new WallFactory());
        BUILDERS.put("boulder", new BoulderFactory());
        BUILDERS.put("switch", new SwitchFactory());
        BUILDERS.put("exit", new ExitFactory());
        BUILDERS.put("treasure", new TreasureFactory());
        BUILDERS.put("wood", new WoodFactory());
        BUILDERS.put("arrow", new ArrowFactory());
        BUILDERS.put("sun_stone", new SunStoneFactory());
    }

    public Entity createEntity(JSONObject jsonEntity) {
        return constructEntity(jsonEntity, config);
    }

    public Bow buildBow() {
        int bowDurability = config.optInt("bow_durability");
        return new Bow(bowDurability);
    }

    public Shield buildShield() {
        int shieldDurability = config.optInt("shield_durability");
        double shieldDefence = config.optInt("shield_defence");
        return new Shield(shieldDurability, shieldDefence);
    }

    public Sceptre buildSceptre() {
        int duration = config.optInt("mind_control_duration");
        return new Sceptre(duration);
    }

    public MidnightArmour buildMidnightArmour() {
        double attack = config.optDouble("midnight_armour_attack");
        double defence = config.optDouble("midnight_armour_defence");
        return new MidnightArmour(defence, attack);
    }

    private Entity constructEntity(JSONObject jsonEntity, JSONObject config) {
        Position pos = new Position(jsonEntity.getInt("x"), jsonEntity.getInt("y"));

        EntityBuilder newCreation = BUILDERS.get(jsonEntity.getString("type"));

        if (newCreation == null) {
            throw new IllegalArgumentException(
                    String.format("Failed to recognise '%s' entity in EntityFactory", jsonEntity.getString("type")));
        }
        return newCreation.create(jsonEntity, config, pos);
    }

    public JSONObject getConfig() {
        return config;
    }

    public Random getRandomGenerator() {
        return ranGen;
    }
}
