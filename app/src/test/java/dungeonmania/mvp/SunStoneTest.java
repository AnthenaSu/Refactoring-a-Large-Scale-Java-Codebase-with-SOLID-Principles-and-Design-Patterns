package dungeonmania.mvp;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

@Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class SunStoneTest {

    @Test
    @Tag("sunstone-1")
    @DisplayName("Test SunStone can open any door and is not consumed")
    public void openDoorWithSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_door", "c_sunStoneTest_door");

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // door ahead should be locked
        assertTrue(TestUtils.getEntities(res, "door").size() > 0);

        // move into door (should open without consuming)
        res = dmc.tick(Direction.RIGHT);

        // door now open, sunstone still in inventory
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("sunstone-2")
    @DisplayName("Test SunStone counts towards treasure goal")
    public void countsAsTreasureGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_treasureGoal", "c_sunStoneTest_door");

        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect the sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // goal should now be achieved
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("sunstone-3")
    @DisplayName("Test SunStone cannot be used to bribe mercenaries")
    public void cannotBribeMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        final DungeonResponse initialRes = dmc.newGame("d_sunStoneTest_bribe", "c_sunStoneTest_door");
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertThrows(Exception.class, () -> dmc.interact(
            TestUtils.getEntities(initialRes, "mercenary").get(0).getId()));
    }

    @Test
    @Tag("sunstone-4")
    @DisplayName("Test SunStone can substitute for treasure/key when crafting a shield")
    public void buildShieldWithSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_shield", "c_sunStoneTest_door");

        // Collect 2 wood and sunstone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Shield should now be buildable (wood + sunstone)
        assertTrue(TestUtils.getBuildables(res).contains("shield"));
    }
}
