package dungeonmania.mvp;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

@Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class SceptreTest {

    @Test
    @Tag("13-1")
    @DisplayName("Test a Sceptre can be crafted from (wood OR 2 arrows) + (key OR treasure) + sun stone")
    public void craftSceptre() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_craft", "c_sceptreTest");

        // pick up materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);  // sun stone

        // should be buildable
        assertTrue(TestUtils.getBuildables(res).contains("sceptre"));

        // craft it
        res = dmc.build("sceptre");

        // check inventory
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("13-2")
    @DisplayName("Test sceptre allows mind control of a mercenary from any distance")
    public void mindControlDistance() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_control", "c_sceptreTest");

        // collect materials and build sceptre
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // attempt interact (should succeed even if far away)
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // mercenary now allied
        assertTrue(TestUtils.isEntityAllied(res, mercId));
    }

    @Test
    @Tag("13-5")
    @DisplayName("Test mind control lasts for configured duration then expires")
    public void mindControlDuration() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_duration", "c_sceptreTest");

        // collect items and build sceptre
        res = dmc.tick(Direction.RIGHT);  // wood
        res = dmc.tick(Direction.RIGHT);  // key
        res = dmc.tick(Direction.RIGHT);  // sun stone
        res = dmc.build("sceptre");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // mind-control starts
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // Tick 1 (enemy still controlled) -> NOT interactable
        res = dmc.tick(Direction.RIGHT);
        assertFalse(isInteractableById(res, mercId));

        // Tick 2 (enemy still controlled) -> NOT interactable
        res = dmc.tick(Direction.RIGHT);
        assertFalse(isInteractableById(res, mercId));

        // Tick 3 (expired) -> becomes interactable again
        res = dmc.tick(Direction.RIGHT);
        assertTrue(isInteractableById(res, mercId));
    }

    @Test
    @Tag("13-3")
    @DisplayName("Test mercenary interactability flips correctly after mind control duration")
    public void testMindControlInteractableLifecycle() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        // Use a test dungeon/config where mercenary + resources exist.
        // For example:
        DungeonResponse res = dmc.newGame("d_sceptreTest_duration", "c_sceptreTest");

        // Player picks up wood, key, and sun stone → can build sceptre
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");

        String mercId = res.getEntities().stream()
            .filter(e -> e.getType().equals("mercenary"))
            .findFirst().get().getId();

        // Mind control begins
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // Tick 1: enemy still controlled → NOT interactable
        res = dmc.tick(Direction.RIGHT);
        assertFalse(isInteractableById(res, mercId),
            "Mercenary should NOT be interactable during mind control (tick 1)");

        // Tick 2: still controlled → NOT interactable
        res = dmc.tick(Direction.RIGHT);
        assertFalse(isInteractableById(res, mercId),
            "Mercenary should still NOT be interactable (tick 2)");

        // Tick 3: control expired → becomes interactable again
        res = dmc.tick(Direction.RIGHT);
        assertTrue(isInteractableById(res, mercId),
            "Mercenary should become interactable again once mind control expires (tick 3)");
    }

    /**
     * Helper function to check interactability by entity id.
     */

    private static boolean isInteractableById(DungeonResponse res, String id) {
        return res.getEntities().stream().anyMatch(e -> e.getId().equals(id) && e.isInteractable());
    }

    @Test
    @Tag("13-4")
    @DisplayName("Test sceptre mind control does not consume treasure or depend on bribe radius")
    public void mindControlIgnoresBribeRules() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_ignoreBribe", "c_sceptreTest");

        // collect and build sceptre
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");

        // no treasure in inventory
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // can still interact without InvalidActionException
        assertDoesNotThrow(() -> dmc.interact(mercId));
    }
}
