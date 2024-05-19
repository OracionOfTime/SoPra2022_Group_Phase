package unittests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.actions.DigTunnel;
import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.Tunnel;
import java.util.Optional;
import org.junit.jupiter.api.Test;


class DigTunnelTest {

    final UnitTestFactory unitTestFactory = new UnitTestFactory();


    Dungeon createDungeon() {
        final Dungeon dungeon = unitTestFactory.createEmptyDungeon();
        dungeon.getTiles().get(1).set(0, Optional.of(new Tunnel(0, 1, false)));
        dungeon.getTiles().get(1).set(1, Optional.of(new Tunnel(1, 0, false)));
        dungeon.getTiles().get(2).set(1, Optional.of(new Tunnel(1, 2, false)));
        dungeon.getTiles().get(2).set(2, Optional.of(new Tunnel(2, 2, false)));
        dungeon.getTiles().get(2).set(3, Optional.of(new Tunnel(3, 2, false)));
        dungeon.getTiles().get(3).set(3, Optional.of(new Tunnel(3, 3, false)));
        return dungeon;
    }

    @Test
    void testSquare() {
        final Dungeon dungeon = createDungeon();
        final DigTunnel dig = new DigTunnel(1, 1, 2);
        final boolean res = dig.canBeDug(dungeon, 1, 2);
        assertFalse(res);
    }

    @Test
    void testSquareOne() {
        final Dungeon dungeon = createDungeon();
        final DigTunnel dig = new DigTunnel(1, 0, 1);
        final boolean res = dig.canBeDug(dungeon, 0, 1);
        assertFalse(res);
    }

    @Test
    void testValidCoordinates() {
        final Dungeon dungeon = createDungeon();
        final DigTunnel dig = new DigTunnel(1, 1, 3);
        final boolean res = dig.canBeDug(dungeon, 1, 3);
        assertTrue(res);
    }

    @Test
    void testIndexOutOfBounds() {
        final Dungeon dungeon = createDungeon();
        DigTunnel dig = new DigTunnel(1, 0, 4);
        boolean res = dig.canBeDug(dungeon, 0, 4);
        assertFalse(res);
        dig = new DigTunnel(1, -2, 3);
        res = dig.canBeDug(dungeon, -2, 3);
        assertFalse(res);
    }


    @Test
    void testInvertedCoordinates() {
        final Dungeon dungeon = createDungeon();
        dungeon.getTiles().get(1).set(3, Optional.of(new Tunnel(3, 1, false)));
        final DigTunnel dig = new DigTunnel(1, 3, 1);
        final boolean res = dig.canBeDug(dungeon, 3, 1);
        assertTrue(res);
    }
}
