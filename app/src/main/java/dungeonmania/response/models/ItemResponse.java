package dungeonmania.response.models;

/**
 * Information about an item within the player's inventory.
 *
 * DO NOT CHANGE THIS FILE
 */
public final class ItemResponse {
    private final String id;
    private final String type;

    /**
     * Create an item response object
     * @param id unique ID of the item
     * @param type type of the item (the object's class name, converted to snake_case)
     */
    public ItemResponse(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    public final String getId() {
        return id;
    }
}
