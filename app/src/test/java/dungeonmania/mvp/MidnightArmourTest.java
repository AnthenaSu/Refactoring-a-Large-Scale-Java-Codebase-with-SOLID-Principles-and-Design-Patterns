package dungeonmania.mvp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class MidnightArmourTest {

    @Test
    @Tag("20")
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
    @Tag("21")
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
    @Tag("19")
    @DisplayName("Midnight Armour get functions")
    public void testDurabilityAlwaysOne() {
        MidnightArmour m = new MidnightArmour(3, 4);
        assertEquals(1, m.getDurability());
        assertEquals("midnight_armour", m.getType());
    }

    @Test
    @Tag("22")
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


    @Test
    @Tag("23")
    @DisplayName("Midnight Armour provides attack & defence boosts in battle")
    public void testMidnightArmourBattleEffect() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnight_armour_effect_original", "c_midnight_armour_test");

        // Pick up the sword (RIGHT), then sun stone (DOWN)
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // Craft the armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Move into the enemy
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertTrue(res.getBattles().size() == 1);

        BattleResponse br = res.getBattles().get(0);
        RoundResponse round = br.getRounds().get(0);

        assertEquals(2.8, -round.getDeltaEnemyHealth());
        assertEquals(0.2, -round.getDeltaCharacterHealth());
    }




}
