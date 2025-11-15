package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import dungeonmania.Game;
import dungeonmania.entities.EntityFactories.SpiderFactory;
import dungeonmania.entities.EntityFactories.ZombieFactory;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Spawn {
    public static void spawnSpider(Game game, JSONObject config, Random ranGen) {
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

    public static void spawnZombie(Game game, ZombieToastSpawner spawner, JSONObject config, Random ranGen) {
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
}
