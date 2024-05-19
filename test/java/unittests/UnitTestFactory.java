package unittests;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.Adventurer;
import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Imp;
import de.unisaarland.cs.se.selab.dungeon.Imp.ActionType;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.dungeon.Trap;
import de.unisaarland.cs.se.selab.dungeon.Tunnel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class UnitTestFactory {

    /*public Imp createImp(final ActionType actionType) {
        return new Imp(actionType);
    }*/

    public Adventurer createAdventurer(final int difficulty, final boolean charge,
            final int healValue, final int diffuseValue, final int id) {
        return new Adventurer("TestAdventurer", 4, difficulty, false,
                charge, healValue, diffuseValue, id);
    }

    public Monster createMonster(final String attackStrategy, final int id) {
        return new Monster(true, id, 2, 2, 1, attackStrategy);
    }

    public Trap createDungeonTrap(final int id, final int target, final String attackStrategy) {
        return new Trap(id, true, 1, target, attackStrategy);
    }

    public Room createRoom(final int column, final int row, final String placementRestriction,
            final int food, final int gold,
            final int imps, final int niceness, final int id) {
        return new Room(column, row, false, 2, placementRestriction, food, gold, imps, niceness,
                id);
    }

    public Tunnel createTunnel(final int column, final int row) {
        return new Tunnel(column, row, false);
    }

    public Tunnel createConqTunnel(final int column, final int row) {
        return new Tunnel(column, row, true);
    }

    public Dungeon createEmptyDungeon() {

        final List<List<Optional<Tile>>> dun = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            dun.add(createEmptyList());
        }
        dun.get(0).set(0, Optional.of(new Tunnel(0, 0, false)));
        return new Dungeon(dun);
    }

    public Dungeon createSampleDungeon() {

        final Dungeon dungeon = createEmptyDungeon();
        dungeon.getTiles().get(0).set(1, Optional.of(new Tunnel(1, 0, false)));
        dungeon.getTiles().get(1).set(0, Optional.of(new Tunnel(0, 1, false)));

        return dungeon;
    }

    public Dungeon createComplicatedDungeon() {

        final Dungeon dungeon = createEmptyDungeon();
        final List<Optional<Tile>> col0 = dungeon.getTiles().get(0);
        col0.set(0, Optional.of(new Tunnel(0, 0, true)));
        col0.set(1, Optional.of(new Tunnel(0, 1, true)));
        col0.set(2, Optional.of(new Tunnel(0, 2, true)));
        col0.set(3, Optional.of(new Tunnel(0, 3, false)));

        final List<Optional<Tile>> col1 = dungeon.getTiles().get(1);
        col1.set(0, Optional.of(new Tunnel(1, 0, true)));
        col1.set(2, Optional.of(new Tunnel(1, 2, false)));

        final List<Optional<Tile>> col2 = dungeon.getTiles().get(2);
        col2.set(0, Optional.of(new Tunnel(2, 0, true)));
        col2.set(1, Optional.of(new Tunnel(2, 1, false)));
        col2.set(2, Optional.of(new Tunnel(2, 2, false)));

        final List<Optional<Tile>> col3 = dungeon.getTiles().get(3);
        col3.set(0, Optional.of(new Tunnel(3, 0, false)));
        col3.set(2, Optional.of(new Tunnel(3, 2, false)));

        return dungeon;
    }

    /*
    public Dungeon createComplicatedDungeon2() {

        final Dungeon dungeon = createEmptyDungeon();
        List<Optional<Tile>> col0 = dungeon.getTiles().get(0);
        col0.set(0, Optional.of(new Tunnel(0, 0, true)));
        col0.set(1, Optional.of(new Tunnel(0, 1, true)));
        col0.set(2, Optional.of(new Tunnel(0, 2, true)));
        col0.set(3, Optional.of(new Tunnel(0, 3, false)));

        List<Optional<Tile>> col1 = dungeon.getTiles().get(1);
        col1.set(0, Optional.of(new Tunnel(1, 0, true)));
        col1.set(2, Optional.of(new Tunnel(1, 2, false)));

        List<Optional<Tile>> col2 = dungeon.getTiles().get(2);
        col2.set(0, Optional.of(new Tunnel(2, 0, true)));
        col2.set(1, Optional.of(new Tunnel(2, 1, false)));
        col2.set(2, Optional.of(new Tunnel(2, 2, false)));

        List<Optional<Tile>> col3 = dungeon.getTiles().get(3);
        col3.set(0, Optional.of(new Tunnel(3, 0, false)));
        col3.set(2, Optional.of(new Tunnel(3, 2, false)));

        return dungeon;
    }

     */

    private List<Optional<Tile>> createEmptyList() {
        final List<Optional<Tile>> tempRow = new ArrayList<>(4);
        for (int j = 0; j < 4; j++) {
            tempRow.add(Optional.empty());
        }
        return tempRow;
    }

    public DungeonLord createDungeonLord(final int playerID, final int commID) {
        final List<Imp> imps = new ArrayList<>();
        imps.add(new Imp(ActionType.RESTING));
        imps.add(new Imp(ActionType.RESTING));
        imps.add(new Imp(ActionType.RESTING));

        final List<Optional<SingleBid>> bids = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            bids.add(Optional.empty());
        }
        final List<Optional<BidType>> lockedBids = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            lockedBids.add(Optional.empty());
        }
        return new DungeonLord(playerID, commID, bids, "DL", 5, lockedBids,
                createEmptyDungeon(), imps, 3, 3, 3, 0, true);
    }

    public Queue<Adventurer> createAdventurerPool() {
        final Queue<Adventurer> advQueue = new LinkedList<>();

        advQueue.add(createAdventurer(3, false, 0, 2, 2));
        advQueue.add(createAdventurer(2, true, 2, 0, 1));
        advQueue.add(createAdventurer(1, false, 0, 0, 0));
        advQueue.add(createAdventurer(1, false, 2, 0, 3));
        advQueue.add(createAdventurer(3, false, 0, 0, 6));
        advQueue.add(createAdventurer(1, false, 3, 0, 9));
        advQueue.add(createAdventurer(2, false, 1, 2, 5));
        advQueue.add(createAdventurer(2, true, 2, 1, 7));
        advQueue.add(createAdventurer(5, false, 0, 0, 8));
        advQueue.add(createAdventurer(4, true, 0, 0, 4));

        return advQueue;
    }

    public Queue<Monster> createMonsterPool() {
        final Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(createMonster("BASIC", 0));
        monsterQueue.add(createMonster("MULTI", 3));
        monsterQueue.add(createMonster("BASIC", 7));
        monsterQueue.add(createMonster("TARGETED", 6));
        monsterQueue.add(createMonster("TARGETED", 4));
        monsterQueue.add(createMonster("BASIC", 5));
        monsterQueue.add(createMonster("MULTI", 2));
        monsterQueue.add(createMonster("BASIC", 1));
        monsterQueue.add(createMonster("MULTI", 8));

        return monsterQueue;
    }

    public Queue<Trap> createTrapPool() {

        final Queue<Trap> trapPool = new LinkedList<>();

        trapPool.add(createDungeonTrap(6, 3, "TARGETED"));
        trapPool.add(createDungeonTrap(0, 1, "BASIC"));
        trapPool.add(createDungeonTrap(4, 1, "BASIC"));
        trapPool.add(createDungeonTrap(2, 2, "TARGETED"));
        trapPool.add(createDungeonTrap(8, 2, "BASIC"));
        trapPool.add(createDungeonTrap(3, 3, "BASIC"));
        trapPool.add(createDungeonTrap(1, 1, "MULTI"));
        trapPool.add(createDungeonTrap(5, 2, "MULTI"));
        trapPool.add(createDungeonTrap(7, 1, "BASIC"));

        return trapPool;

    }

    public Queue<Room> createRoomPool() {
        final Queue<Room> roomPool = new LinkedList<>();

        roomPool.add(createRoom(-1, -1, "UPPER_HALF", 2, 0, 2, 1, 2));
        roomPool.add(createRoom(-1, -1, "UPPER_HALF", 0, 2, 1, 1, 3));
        roomPool.add(createRoom(-1, -1, "LOWER_HALF", 2, 1, 0, 1, 1));
        roomPool.add(createRoom(-1, -1, "UPPER_HALF", 1, 1, 1, 1, 6));
        roomPool.add(createRoom(-1, -1, "OUTER_RING", 2, 0, 2, 1, 5));
        roomPool.add(createRoom(-1, -1, "UPPER_HALF", 2, 0, 0, 1, 0));
        roomPool.add(createRoom(-1, -1, "INNER_RING", 2, 1, 2, 1, 7));
        roomPool.add(createRoom(-1, -1, "LOWER_HALF", 0, 4, 0, 1, 8));
        roomPool.add(createRoom(-1, -1, "OUTER_RING", 0, 3, 0, 1, 4));

        return roomPool;
    }

    /*
    public Game createDefaultGame() {
        final Logger logger = Logger.getLogger("print IOEException");
        final GameFactory gameFactory = new GameFactory(42,
                "src/main/resources/configuration.json");
        try {
            if (!gameFactory.parseConfig()) {
                return null;
            }
        } catch (IOException ioe) {
            logger.log(Level.FINE, ioe.toString());
        }
        final Game game = gameFactory.initializeGame();

        // create DLs
        final DungeonLord dl0 = createDungeonLord(0, 0);
        final DungeonLord dl1 = createDungeonLord(1, 1);
        final DungeonLord dl2 = createDungeonLord(2, 2);
        final DungeonLord dl3 = createDungeonLord(3, 3);

        // create the dungeon Lord map by commID
        final Map<Integer, DungeonLord> dlMap = new HashMap<>();

        game.setDungeonLordMap(dlMap);

        game.getDungeonLordMap().put(0, dl0);
        game.getDungeonLordMap().put(1, dl1);
        game.getDungeonLordMap().put(2, dl2);
        game.getDungeonLordMap().put(3, dl3);

        // create List of DLs
        final List<DungeonLord> dl = new ArrayList<>();
        dl.add(dl0);
        dl.add(dl1);
        dl.add(dl2);
        dl.add(dl3);

        //final BroadcastEvents sc = new BroadcastEvents(5030, -1, ac);

        // create Pools and available MonstersList
        final Queue<Monster> monstersPool = this.createMonsterPool();
        final List<Monster> monsterList = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            monsterList.add(monstersPool.poll());
        }

        // create Pool of traps
        final Queue<Trap> trapPool = this.createTrapPool();

        // create pool of rooms and the available rooms

        final Queue<Room> roomsPool = this.createRoomPool();
        final List<Room> roomList = new ArrayList<>(2);

        for (int i = 0; i < 2; i++) {
            roomList.add(roomsPool.poll());
        }

        // create Adventurers and available Adventurers in queue and in an array
        final Queue<Adventurer> adventurerPool = this.createAdventurerPool();
        Adventurer[] adventurerArray = new Adventurer[4];

        for (int i = 0; i < 4; i++) {
            adventurerArray[i] = adventurerPool.poll();
        }


        //game.createRegistrationPhase();
        //game.createOtherPhases(sc);

        final RegistrationPhase registrationPhase = new RegistrationPhase(null,
                dl, dlMap, game.getDungeonSideLength(), game.getConfig(), game.getMaxPlayers());
        game.setRegistrationPhase(registrationPhase);
        final BuildingPhase buildPhase = new BuildingPhase(null, dl,
                game.getDungeonLordMap(), monstersPool, roomsPool);
        final EvaluationPhase evalPhase = new EvaluationPhase(null,
                dl, game.getDungeonLordMap(), false, false, null,
                adventurerArray, trapPool, false);

        game.setEvaluationPhase(evalPhase);
        game.setBuildingPhase(buildPhase);

        return game;
    }

     */

}

