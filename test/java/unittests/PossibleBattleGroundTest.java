package unittests;

import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.phases.CombatPhase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class PossibleBattleGroundTest {

    private final UnitTestFactory fac = new UnitTestFactory();

    @Test
    void possibleBGTest1() {
        final List<DungeonLord> dungeonLordList = new ArrayList<>();
        final Map<Integer, DungeonLord> dungeonLordMap = new HashMap<>();

        final DungeonLord dl = fac.createDungeonLord(0, 0);

        dungeonLordList.add(dl);
        dungeonLordMap.put(0, dl);

        dl.setDungeon(fac.createComplicatedDungeon());

        final CombatPhase cp = new CombatPhase(null,
                dungeonLordList, dungeonLordMap, false, new ArrayList<>());

        cp.setCurrentDungeonLord(dl);

        cp.findShortestPath();

        /*
        final List<Optional<Tile>> expectedPosB = new ArrayList<>();
        expectedPosB.add(Optional.of(new Tunnel(0, 3, false)));
        expectedPosB.add(Optional.of(new Tunnel(3, 0, false)));
        expectedPosB.add(Optional.of(new Tunnel(2, 3, false)));
        expectedPosB.add(Optional.of(new Tunnel(3, 2, false)));

         */

        final List<Optional<Tile>> posB = cp.getPossibleBattleGround();
        assert posB.size() == 4;
    }

    /*
    @Test
    void possibleBGTest2() {
        final List<DungeonLord> dungeonLordList = new ArrayList<>();
        final Map<Integer, DungeonLord> dungeonLordMap = new HashMap<>();

        final DungeonLord dl = fac.createDungeonLord(0, 0);

        dungeonLordList.add(dl);
        dungeonLordMap.put(0, dl);

        dl.setDungeon(fac.createComplicatedDungeon2());

        final CombatPhase cp = new CombatPhase(null,
                dungeonLordList, dungeonLordMap, false, new ArrayList<>());

        cp.setCurrentDungeonLord(dl);

        cp.findShortestPath();

        List<Optional<Tile>> posB = cp.getPossibleBattleGround();


        List<Optional<Tile>> expectedPosB = new ArrayList<>();
        expectedPosB.add(Optional.of(new Tunnel(2, 0,false)));


        assert posB.size() == 1;
    }

     */
}
