package dungeonmania.mvp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class MidnightArmourTest {

    @Test
    @Tag("Task 2 - Midnight Armour")
    @DisplayName("Can build midnight armour when no zombies exist")
    public void testBuildMidnightArmourNoZombies() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();

        // Start with dungeon containing no zombies
        DungeonResponse res = dmc.newGame("d_midnight_armour_no_zombie", "c_midnight_armour_test");

        // Pick up sword + sun stone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Ensure buildable list contains midnight_armour
        assertTrue(TestUtils.getBuildables(res).contains("midnight_armour"));

        // Build successfully
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertTrue(TestUtils.getInventory(res, "midnight_armour").size() == 1);
    }

    @Test
    @Tag("Task 2 - Midnight Armour")
    @DisplayName("Cannot build midnight armour when zombies exist")
    public void testBuildMidnightArmourWithZombies() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_midnight_armour_test", "c_midnight_armour_test");

        // Move to pick up sword and sun stone
        res = dmc.tick(Direction.RIGHT);  // pick up sword
        res = dmc.tick(Direction.RIGHT);  // pick up sun stone

        // Tick once more so zombie spawns
        res = dmc.tick(Direction.RIGHT);

        // Confirm a zombie
        assertTrue(TestUtils.getEntities(res, "zombie_toast").size() > 0, "Zombie should spawn adjacent to spawner");

        // Attempt to craft midnight armour should fail
        assertThrows(
            InvalidActionException.class,
            () -> dmc.build("midnight_armour"),
            "Expected failure when building midnight armour while zombies exist"
        );
    }

    @Test
    @Tag("Task 2 - Midnight Armour")
    @DisplayName("Cannot build without required materials")
    public void testBuildMidnightArmourMissingItems() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnight_armour_test", "c_midnight_armour_test");

        // Player moves only once (has sword but no sun_stone)
        res = dmc.tick(Direction.RIGHT);

        // midnight_armour should not appear in buildable list
        assertFalse(TestUtils.getBuildables(res).contains("midnight_armour"));

        // and attempt should throw
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    private List<EntityResponse> getZombies(DungeonResponse res) {
        return TestUtils.getEntities(res, "zombie_toast");
    }
}
