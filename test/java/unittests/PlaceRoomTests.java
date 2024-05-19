package unittests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.actions.BuildRoom;
import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.dungeon.Tunnel;
import java.util.Optional;
import org.junit.jupiter.api.Test;


class PlaceRoomTests {

    final UnitTestFactory unitTestFactory = new UnitTestFactory();

    @Test
    void testValidCoordinates() {
        final Dungeon dungeon = createDungeon();
        final BuildRoom build = new BuildRoom(1, 3, 0, 2);
        final boolean res = build.checkValidCoordinates(3, 0, dungeon.getTiles());
        assertTrue(res);
    }


    @Test
    void testRoomAlreadyPlaced() {
        final Dungeon dungeon = createDungeon();
        final BuildRoom build = new BuildRoom(1, 1, 2, 2);
        final boolean res = build.checkValidCoordinates(1, 2, dungeon.getTiles());
        assertFalse(res);
    }

    @Test
    void testTileConquered() {
        final Dungeon dungeon = createDungeon();
        final BuildRoom build = new BuildRoom(1, 1, 0, 2);
        final boolean res = build.checkValidCoordinates(1, 0, dungeon.getTiles());
        assertFalse(res);
    }

    @Test
    void testAdjacentRoom() {
        final Dungeon dungeon = createDungeon();
        final BuildRoom build = new BuildRoom(1, 2, 2, 2);
        final boolean res = build.checkValidCoordinates(2, 2, dungeon.getTiles());
        assertFalse(res);
    }

    @Test
    void testOuterRingValid() {
        final Dungeon dungeon = createDungeon();
        final BuildRoom build = new BuildRoom(1, 2, 3, 2);
        final boolean res = build.checkPlacementRestriction(3, 2, dungeon.getTiles(),
                "OUTER_RING");
        assertTrue(res);
    }

    @Test
    void testInnerRingInvalid() {
        final Dungeon dungeon = createDungeon();
        final BuildRoom build = new BuildRoom(1, 2, 3, 2);
        final boolean res = build.checkPlacementRestriction(3, 2, dungeon.getTiles(),
                "INNER_RING");
        assertFalse(res);
    }

    @Test
    void testUpperHalfValid() {
        final Dungeon dungeon = createDungeon();
        final BuildRoom build = new BuildRoom(1, 3, 0, 2);
        final boolean res = build.checkPlacementRestriction(3, 0, dungeon.getTiles(),
                "UPPER_HALF");
        assertTrue(res);
    }

    @Test
    void testLowerHalfInvalid() {
        final Dungeon dungeon = createDungeon();
        final BuildRoom build = new BuildRoom(1, 3, 1, 2);
        final boolean res = build.checkPlacementRestriction(3, 1, dungeon.getTiles(),
                "LOWER_HALF");
        assertFalse(res);
    }

    Dungeon createDungeon() {
        final Dungeon dungeon = unitTestFactory.createEmptyDungeon();
        dungeon.getTiles().get(0).set(1, Optional.of(new Tunnel(1, 0, true)));
        dungeon.getTiles().get(0).set(3, Optional.of(new Tunnel(3, 0, false)));
        dungeon.getTiles().get(1).set(1, Optional.of(new Tunnel(1, 1, false)));
        dungeon.getTiles().get(1).set(3, Optional.of(new Tunnel(3, 1, false)));
        dungeon.getTiles().get(2).set(1, Optional.of(new Room(1, 0, false,
                2, "INNER_RING", 0, 0, 0, 0, 0)));
        dungeon.getTiles().get(2).set(2, Optional.of(new Tunnel(2, 2, false)));
        dungeon.getTiles().get(2).set(3, Optional.of(new Tunnel(3, 2, false)));
        return dungeon;
    }
}