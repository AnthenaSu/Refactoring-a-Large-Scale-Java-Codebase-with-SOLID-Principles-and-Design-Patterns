package dungeonmania.response.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.battles.BattleRound;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.util.NameConverter;

/**
 * Functions used to build responses sent by the web server.
 *
 * You may change this file at your own discretion.
 * However, you must always make sure your changes pass MVP tests and the dryrun.
 */
public class ResponseBuilder {
    /** Return the overall state of the dungeon */
    public static DungeonResponse getDungeonResponse(Game game) {
        List<EntityResponse> entityResponse = new ArrayList<>();
        game.getMap().getEntities().forEach(e -> {
            entityResponse.add(ResponseBuilder.getEntityResponse(game, e));
        });
        return new DungeonResponse(game.getId(), game.getName(), entityResponse,
                (game.getPlayer() != null) ? getInventoryResponse(game.getPlayer().getInventory()) : null,
                game.getBattleFacade().getBattleResponses(),
                (game.getPlayer() != null) ? game.getPlayer().getBuildables() : null,
                (game.getGoals().achieved(game)) ? "" : game.getGoals().toString(game));
    }

    /** Return the state of the inventory */
    private static List<ItemResponse> getInventoryResponse(Inventory inventory) {
        return inventory.getEntities().stream().map(ResponseBuilder::getItemResponse).collect(Collectors.toList());
    }

    /** Return item info for the given entity */
    public static ItemResponse getItemResponse(Entity entity) {
        return new ItemResponse(entity.getId(), NameConverter.toSnakeCase(entity));
    }

    /** Return entity info for the given entity */
    public static EntityResponse getEntityResponse(Game game, Entity entity) {
        return new EntityResponse(entity.getId(), NameConverter.toSnakeCase(entity), entity.getPosition(),
                (entity instanceof Interactable interactable) && interactable.isInteractable(game.getPlayer()));
    }

    /** Return info about a round of battle */
    public static RoundResponse getRoundResponse(BattleRound round) {
        return new RoundResponse(round.getDeltaSelfHealth(), round.getDeltaTargetHealth());
    }
}
