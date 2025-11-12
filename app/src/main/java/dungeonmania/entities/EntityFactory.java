package dungeonmania.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import dungeonmania.Game;
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
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.map.GameMap;
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

    public void spawnSpider(Game game) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        int rate = config.optInt("spider_spawn_interval", 0);
        if (rate == 0 || (tick + 1) % rate != 0)
            return;
        int radius = 20;
        Position player = map.getPlayer().getPosition();

        // Spider dummySpider = buildSpider(new Position(0, 0)); // for checking possible positions
        Spider dummySpider = (Spider) new SpiderFactory().create(null, config, new Position(0, 0));
        List<Position> availablePos = new ArrayList<>();
        for (int i = player.getX() - radius; i < player.getX() + radius; i++) {
            for (int j = player.getY() - radius; j < player.getY() + radius; j++) {
                if (Position.calculatePositionBetween(player, new Position(i, j)).magnitude() > radius)
                    continue;
                Position np = new Position(i, j);
                if (!map.canMoveTo(dummySpider, np) || np.equals(player))
                    continue;
                if (map.getEntities(np).stream().anyMatch(Enemy.class::isInstance))
                    continue;
                availablePos.add(np);
            }
        }
        Position initPosition = availablePos.get(ranGen.nextInt(availablePos.size()));
        Spider spider = (Spider) new SpiderFactory().create(null, config, initPosition);
        map.addEntity(spider);
        game.register(() -> spider.move(game), Game.AI_MOVEMENT, spider.getId());
    }

    public void spawnZombie(Game game, ZombieToastSpawner spawner) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        Random randGen = new Random();
        int spawnInterval = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        if (spawnInterval == 0 || (tick + 1) % spawnInterval != 0)
            return;
        List<Position> pos = spawner.getPosition().getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.getEntities(p).stream().noneMatch(Wall.class::isInstance)).toList();
        if (pos.isEmpty())
            return;
        ZombieToast zt = (ZombieToast) new ZombieFactory().create(null, config, pos.get(randGen.nextInt(pos.size())));
        map.addEntity(zt);
        map.registerPotionListener(zt);
        game.register(() -> zt.move(game), Game.AI_MOVEMENT, zt.getId());
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

    private Entity constructEntity(JSONObject jsonEntity, JSONObject config) {
        Position pos = new Position(jsonEntity.getInt("x"), jsonEntity.getInt("y"));

        EntityBuilder newCreation = BUILDERS.get(jsonEntity.getString("type"));

        if (newCreation == null) {
            throw new IllegalArgumentException(
                    String.format("Failed to recognise '%s' entity in EntityFactory", jsonEntity.getString("type")));
        }
        return newCreation.create(jsonEntity, config, pos);
    }
}
