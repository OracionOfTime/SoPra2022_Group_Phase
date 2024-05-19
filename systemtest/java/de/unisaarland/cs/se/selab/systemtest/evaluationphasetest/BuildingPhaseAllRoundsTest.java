package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;


import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.CustomBuildingPhaseTests;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BuildingPhaseAllRoundsTest extends CustomBuildingPhaseTests {

    public BuildingPhaseAllRoundsTest(final Class<?> subclass, final boolean res) {
        super(subclass, res);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(BuildingPhaseAllRoundsTest.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3);
    }

    @Override
    public void run() throws TimeoutException, AssertionError {

        // register 3 players and start game
        this.registerAndStartGame();
        // shuffle pools of adventurers, monsters, rooms
        final Random rand = new Random(createSeed());
        final List<Integer> monsters = new ArrayList<>(24);
        for (int i = 0; i < 24; i++) {
            monsters.add(i);
        }
        Collections.shuffle(monsters, rand);

        final List<Integer> adventurers = new ArrayList<>(32);
        for (int i = 0; i < 32; i++) {
            adventurers.add(i);
        }
        Collections.shuffle(adventurers, rand);

        final List<Integer> traps = new ArrayList<>(51);
        for (int i = 0; i < 51; i++) {
            traps.add(i);
        }
        Collections.shuffle(traps, rand);

        final List<Integer> rooms = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            rooms.add(i);
        }
        Collections.shuffle(rooms, rand);

        // draw adventurers, monsters, rooms
        draw(adventurers, monsters, rooms, 0, 1);
        // start bidding
        startBiddingRoundOne();

        // since a bid was placed on the trap, we pass pool of traps to the evaluation function
        evalRoundOne(traps);

        // adventurers arrived
        // drawn adventurers are:
        // id 29, diff. 6; id 23, diff. 8; id 2, diff. 3
        // evilness of players:
        // player1 - 6, player2 - 6, player3 - 3

        spreadAdventurers(2, 2, 3);
        spreadAdventurers(29, 0, 3);
        spreadAdventurers(23, 1, 3);

        assertNextRound(1, 2);
        assertNextRound(2, 2);
        assertNextRound(3, 2);

        draw(adventurers, monsters, rooms, 3, 2);
        startBiddingRoundTwo();
        evalRoundTwo(traps);

        // adventurers arrived
        // IDs 0 (diff. 1), 18 (diff. 3), 11 (diff. 4)
        // evilness:
        // player1 - 6, player2 - 5, player3 - 4
        spreadAdventurers(0, 2, 3);
        spreadAdventurers(18, 1, 3);
        spreadAdventurers(11, 0, 3);

        assertNextRound(1, 3);
        assertNextRound(2, 3);
        assertNextRound(3, 3);

        draw(adventurers, monsters, rooms, 6, 3);
        startBiddingRoundThree();
        evalRoundThree();
        // adventurers arrived
        // IDs 20 (diff. 5), 6 (diff. 7), 9 (diff. 2)
        // evilness:
        // player1 - 5, player2 - 7, player3 - 4
        spreadAdventurers(9, 2, 3);
        spreadAdventurers(20, 0, 3);
        spreadAdventurers(6, 1, 3);

        assertNextRound(1, 4);
        assertNextRound(2, 4);
        assertNextRound(3, 4);

        draw(adventurers, monsters, rooms, 9, 4);
        startBiddingRoundFour();
        evalRoundFour();

        // round 1 of combat phase
        assertNextRound(1, 1);
        assertNextRound(2, 1);
        assertNextRound(3, 1);

        // combat phase starts but players leave
        playersLeave();
    }

    protected void evalRoundOne(final List<Integer> traps) throws TimeoutException, AssertionError {
        evalFoodSlotOne(2, 3);
        evalFoodSlotTwo(1, 3);
        evalFoodSlotThree(0, 3);
        evalNiceness(1, 0, 3);
        evalNiceness(2, 2, 3);
        evalTunnelSlotOne(3, 0, 1, 3);

        // only one tile available to mine gold
        assertImpsChanged(1, -1, 1);
        assertImpsChanged(2, -1, 1);
        assertImpsChanged(3, -1, 1);

        // evalGold(1, 1);
        evalImpsSlotOne(0, 3);
        evalTrap(traps, 1, 1, 0, 3);

        assertBidRetrieved(1, BidType.NICENESS, 0);
        assertBidRetrieved(2, BidType.NICENESS, 0);
        assertBidRetrieved(3, BidType.NICENESS, 0);
        assertBidRetrieved(1, BidType.GOLD, 1);
        assertBidRetrieved(2, BidType.GOLD, 1);
        assertBidRetrieved(3, BidType.GOLD, 1);
        assertBidRetrieved(1, BidType.FOOD, 2);
        assertBidRetrieved(2, BidType.FOOD, 2);
        assertBidRetrieved(3, BidType.FOOD, 2);

        // returnImpsForGold(1, 1);
        returnAllImps(1, 1, 3);
        retrieveGold(1, 1, 3);
        returnImpsForTunnel(1, 2, 3);
    }


    /**
     * collects the bids from players in the first round placed bids: player1 - Niceness, Imp, Food
     * player2 - Gold, Food, Trap player3 - Food, Tunnel, Niceness
     *
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void startBiddingRoundOne() throws TimeoutException, AssertionError {
        assertBiddingStarted(1);
        assertBiddingStarted(2);
        assertBiddingStarted(3);

        assertActNow(1);
        assertActNow(2);
        assertActNow(3);

        sendPlaceBid(1, BidType.NICENESS, 1);
        assertBidPlaced(1, BidType.NICENESS, 0, 1);
        assertBidPlaced(2, BidType.NICENESS, 0, 1);
        assertBidPlaced(3, BidType.NICENESS, 0, 1);
        assertActNow(1);

        sendPlaceBid(2, BidType.GOLD, 1);
        assertBidPlaced(1, BidType.GOLD, 1, 1);
        assertBidPlaced(2, BidType.GOLD, 1, 1);
        assertBidPlaced(3, BidType.GOLD, 1, 1);
        assertActNow(2);

        sendPlaceBid(3, BidType.FOOD, 1);
        assertBidPlaced(1, BidType.FOOD, 2, 1);
        assertBidPlaced(2, BidType.FOOD, 2, 1);
        assertBidPlaced(3, BidType.FOOD, 2, 1);
        assertActNow(3);

        sendPlaceBid(1, BidType.IMPS, 2);
        assertBidPlaced(1, BidType.IMPS, 0, 2);
        assertBidPlaced(2, BidType.IMPS, 0, 2);
        assertBidPlaced(3, BidType.IMPS, 0, 2);
        assertActNow(1);

        sendPlaceBid(2, BidType.FOOD, 2);
        assertBidPlaced(1, BidType.FOOD, 1, 2);
        assertBidPlaced(2, BidType.FOOD, 1, 2);
        assertBidPlaced(3, BidType.FOOD, 1, 2);
        assertActNow(2);

        sendPlaceBid(3, BidType.TUNNEL, 2);
        assertBidPlaced(1, BidType.TUNNEL, 2, 2);
        assertBidPlaced(2, BidType.TUNNEL, 2, 2);
        assertBidPlaced(3, BidType.TUNNEL, 2, 2);
        assertActNow(3);

        sendPlaceBid(1, BidType.FOOD, 3);
        assertBidPlaced(1, BidType.FOOD, 0, 3);
        assertBidPlaced(2, BidType.FOOD, 0, 3);
        assertBidPlaced(3, BidType.FOOD, 0, 3);

        sendPlaceBid(2, BidType.TRAP, 3);
        assertBidPlaced(1, BidType.TRAP, 1, 3);
        assertBidPlaced(2, BidType.TRAP, 1, 3);
        assertBidPlaced(3, BidType.TRAP, 1, 3);

        sendPlaceBid(3, BidType.NICENESS, 3);
        assertBidPlaced(1, BidType.NICENESS, 2, 3);
        assertBidPlaced(2, BidType.NICENESS, 2, 3);
        assertBidPlaced(3, BidType.NICENESS, 2, 3);
    }


    /**
     * collects bids from round two player1: Tunnel, Monster,Trap player2: Imp, Tunnel, Niceness
     * player3: Trap, Gold, Monster
     *
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void startBiddingRoundTwo() throws TimeoutException, AssertionError {
        assertBiddingStarted(1);
        assertBiddingStarted(2);
        assertBiddingStarted(3);

        assertActNow(1);
        assertActNow(2);
        assertActNow(3);

        sendPlaceBid(1, BidType.TUNNEL, 1);
        assertBidPlaced(1, BidType.TUNNEL, 0, 1);
        assertBidPlaced(2, BidType.TUNNEL, 0, 1);
        assertBidPlaced(3, BidType.TUNNEL, 0, 1);
        assertActNow(1);

        sendPlaceBid(2, BidType.TUNNEL, 2);
        assertBidPlaced(1, BidType.TUNNEL, 1, 2);
        assertBidPlaced(2, BidType.TUNNEL, 1, 2);
        assertBidPlaced(3, BidType.TUNNEL, 1, 2);
        assertActNow(2);

        sendPlaceBid(3, BidType.TRAP, 1);
        assertBidPlaced(1, BidType.TRAP, 2, 1);
        assertBidPlaced(2, BidType.TRAP, 2, 1);
        assertBidPlaced(3, BidType.TRAP, 2, 1);
        assertActNow(3);

        sendPlaceBid(1, BidType.MONSTER, 2);
        assertBidPlaced(1, BidType.MONSTER, 0, 2);
        assertBidPlaced(2, BidType.MONSTER, 0, 2);
        assertBidPlaced(3, BidType.MONSTER, 0, 2);
        assertActNow(1);

        sendPlaceBid(2, BidType.IMPS, 1);
        assertBidPlaced(1, BidType.IMPS, 1, 1);
        assertBidPlaced(2, BidType.IMPS, 1, 1);
        assertBidPlaced(3, BidType.IMPS, 1, 1);
        assertActNow(2);

        sendPlaceBid(3, BidType.GOLD, 2);
        assertBidPlaced(1, BidType.GOLD, 2, 2);
        assertBidPlaced(2, BidType.GOLD, 2, 2);
        assertBidPlaced(3, BidType.GOLD, 2, 2);
        assertActNow(3);

        sendPlaceBid(3, BidType.MONSTER, 3);
        assertBidPlaced(1, BidType.MONSTER, 2, 3);
        assertBidPlaced(2, BidType.MONSTER, 2, 3);
        assertBidPlaced(3, BidType.MONSTER, 2, 3);

        sendPlaceBid(2, BidType.NICENESS, 3);
        assertBidPlaced(1, BidType.NICENESS, 1, 3);
        assertBidPlaced(2, BidType.NICENESS, 1, 3);
        assertBidPlaced(3, BidType.NICENESS, 1, 3);

        sendPlaceBid(1, BidType.TRAP, 3);
        assertBidPlaced(1, BidType.TRAP, 0, 3);
        assertBidPlaced(2, BidType.TRAP, 0, 3);
        assertBidPlaced(3, BidType.TRAP, 0, 3);
    }

    /**
     * evaluate (send events for) bids in round two
     *
     * @param traps - pool of traps
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void evalRoundTwo(final List<Integer> traps) throws TimeoutException, AssertionError {
        evalNiceness(1, 1, 3);
        evalTunnelSlotOne(1, 0, 1, 3);
        evalTunnelSlotTwo(2, 0, 1, 3);
        evalGold(1, 2, 3);
        evalImpsSlotOne(1, 3);
        evalTrap(traps, 2, 1, 1, 3);
        evalTrap(traps, 0, 2, 2, 3);

        // monsters with IDs 7, 22, 1 were drawn
        // player1 chose monster 7 with evilness 0 and hunger 0
        // player3 chose monster 22 with evilness 1 and hunger 1
        evalMonster(1, 1, 7, 0, 0, 3);
        evalMonster(2, 3, 22, 1, 1, 3);
        unlockBids(BidType.IMPS, 0, 3);
        unlockBids(BidType.FOOD, 0, 3);
        unlockBids(BidType.TUNNEL, 0, 3);
        unlockBids(BidType.FOOD, 1, 3);
        unlockBids(BidType.TRAP, 1, 3);
        unlockBids(BidType.IMPS, 1, 3);
        unlockBids(BidType.TUNNEL, 2, 3);
        unlockBids(BidType.NICENESS, 2, 3);
        unlockBids(BidType.TRAP, 2, 3);
        returnImpsForTunnel(1, 0, 3);

        returnImpsForTunnel(2, 1, 3);
        returnImpsForGold(1, 2, 3);
    }


    /**
     * collects bids for round 3: player1 - Gold, Niceness, Tunnel player2 - Gold, Food, Monster
     * player3 - Imp, Food, Tunnel
     *
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void startBiddingRoundThree() throws TimeoutException, AssertionError {
        assertBiddingStarted(1);
        assertBiddingStarted(2);
        assertBiddingStarted(3);

        assertActNow(1);
        assertActNow(2);
        assertActNow(3);

        sendPlaceBid(2, BidType.MONSTER, 3);
        assertBidPlaced(1, BidType.MONSTER, 1, 3);
        assertBidPlaced(2, BidType.MONSTER, 1, 3);
        assertBidPlaced(3, BidType.MONSTER, 1, 3);
        assertActNow(2);

        sendPlaceBid(3, BidType.IMPS, 1);
        assertBidPlaced(1, BidType.IMPS, 2, 1);
        assertBidPlaced(2, BidType.IMPS, 2, 1);
        assertBidPlaced(3, BidType.IMPS, 2, 1);
        assertActNow(3);

        sendPlaceBid(1, BidType.GOLD, 1);
        assertBidPlaced(1, BidType.GOLD, 0, 1);
        assertBidPlaced(2, BidType.GOLD, 0, 1);
        assertBidPlaced(3, BidType.GOLD, 0, 1);
        assertActNow(1);

        sendPlaceBid(1, BidType.TUNNEL, 3);
        assertBidPlaced(1, BidType.TUNNEL, 0, 3);
        assertBidPlaced(2, BidType.TUNNEL, 0, 3);
        assertBidPlaced(3, BidType.TUNNEL, 0, 3);
        assertActNow(1);

        sendPlaceBid(2, BidType.FOOD, 2);
        assertBidPlaced(1, BidType.FOOD, 1, 2);
        assertBidPlaced(2, BidType.FOOD, 1, 2);
        assertBidPlaced(3, BidType.FOOD, 1, 2);
        assertActNow(2);

        sendPlaceBid(3, BidType.FOOD, 2);
        assertBidPlaced(1, BidType.FOOD, 2, 2);
        assertBidPlaced(2, BidType.FOOD, 2, 2);
        assertBidPlaced(3, BidType.FOOD, 2, 2);
        assertActNow(3);

        sendPlaceBid(1, BidType.NICENESS, 2);
        assertBidPlaced(1, BidType.NICENESS, 0, 2);
        assertBidPlaced(2, BidType.NICENESS, 0, 2);
        assertBidPlaced(3, BidType.NICENESS, 0, 2);

        sendPlaceBid(2, BidType.GOLD, 1);
        assertBidPlaced(1, BidType.GOLD, 1, 1);
        assertBidPlaced(2, BidType.GOLD, 1, 1);
        assertBidPlaced(3, BidType.GOLD, 1, 1);

        sendPlaceBid(3, BidType.TUNNEL, 3);
        assertBidPlaced(1, BidType.TUNNEL, 2, 3);
        assertBidPlaced(2, BidType.TUNNEL, 2, 3);
        assertBidPlaced(3, BidType.TUNNEL, 2, 3);
    }

    /**
     * evaluate bids from round three, unlock previous bids, return imps
     *
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void evalRoundThree() throws TimeoutException, AssertionError {
        evalFoodSlotOne(2, 3);
        evalFoodSlotTwo(1, 3);
        evalNiceness(1, 0, 3);
        evalTunnelSlotOne(3, 1, 2, 3);
        evalTunnelSlotTwo(1, 1, 2, 3);
        // player1 got 1st slot of gold bid but has only one imp available
        assertImpsChanged(1, -1, 0);
        assertImpsChanged(2, -1, 0);
        assertImpsChanged(3, -1, 0);

        evalGold(2, 1, 3);
        evalImpsSlotOne(2, 3);
        // monsters drawn with IDs 1, 14, 3
        // player2 chooses monster 14 with hunger 1 and evilness 1
        evalMonster(1, 2, 14, 1, 1, 3);

        unlockBids(BidType.MONSTER, 0, 3);
        unlockBids(BidType.TRAP, 0, 3);
        unlockBids(BidType.GOLD, 0, 3);
        unlockBids(BidType.TUNNEL, 1, 3);
        unlockBids(BidType.NICENESS, 1, 3);
        unlockBids(BidType.GOLD, 1, 3);
        unlockBids(BidType.GOLD, 2, 3);
        unlockBids(BidType.MONSTER, 2, 3);
        unlockBids(BidType.IMPS, 2, 3);

        returnAllImps(4, 0, 3);
        retrieveGold(1, 0, 3);

        returnImpsForGold(2, 1, 3);
        returnImpsForTunnel(1, 2, 3);
    }

    /**
     * bidding round 4 player1: Room, Food, Imp player2: Room, Niceness, Imp player3: Room, Gold,
     * Imp
     *
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void startBiddingRoundFour() throws TimeoutException, AssertionError {
        assertBiddingStarted(1);
        assertBiddingStarted(2);
        assertBiddingStarted(3);

        assertActNow(1);
        assertActNow(2);
        assertActNow(3);

        sendPlaceBid(1, BidType.ROOM, 1);
        for (int i = 1; i < 4; i++) {
            assertBidPlaced(i, BidType.ROOM, 0, 1);
        }
        assertActNow(1);

        sendPlaceBid(2, BidType.ROOM, 1);
        for (int i = 1; i < 4; i++) {
            assertBidPlaced(i, BidType.ROOM, 1, 1);
        }
        assertActNow(2);

        sendPlaceBid(3, BidType.ROOM, 1);
        for (int i = 1; i < 4; i++) {
            assertBidPlaced(i, BidType.ROOM, 2, 1);
        }
        assertActNow(3);

        sendPlaceBid(1, BidType.FOOD, 2);
        for (int i = 1; i < 4; i++) {
            assertBidPlaced(i, BidType.FOOD, 0, 2);
        }
        assertActNow(1);

        sendPlaceBid(2, BidType.NICENESS, 2);
        for (int i = 1; i < 4; i++) {
            assertBidPlaced(i, BidType.NICENESS, 1, 2);
        }
        assertActNow(2);

        sendPlaceBid(3, BidType.GOLD, 2);
        for (int i = 1; i < 4; i++) {
            assertBidPlaced(i, BidType.GOLD, 2, 2);
        }
        assertActNow(3);

        sendPlaceBid(1, BidType.IMPS, 3);
        for (int i = 1; i < 4; i++) {
            assertBidPlaced(i, BidType.IMPS, 0, 3);
        }

        sendPlaceBid(2, BidType.IMPS, 3);
        for (int i = 1; i < 4; i++) {
            assertBidPlaced(i, BidType.IMPS, 1, 3);
        }

        sendPlaceBid(3, BidType.IMPS, 3);
        for (int i = 1; i < 4; i++) {
            assertBidPlaced(i, BidType.IMPS, 2, 3);
        }
    }

    protected void evalRoundFour() throws TimeoutException, AssertionError {
        evalFoodSlotOne(0, 3);
        evalNiceness(1, 1, 3);
        evalGold(1, 2, 3);
        evalImpsSlotOne(0, 3);
        evalImpsSlotTwo(1, 3);
        evalImpsSLotThree(2, 3);
        evalRoomBidsRoundFour();

        unlockBids(BidType.NICENESS, 0, 3);
        unlockBids(BidType.TUNNEL, 0, 3);
        unlockBids(BidType.ROOM, 0, 3);
        unlockBids(BidType.FOOD, 1, 3);
        unlockBids(BidType.MONSTER, 1, 3);
        unlockBids(BidType.ROOM, 1, 3);
        unlockBids(BidType.FOOD, 2, 3);
        unlockBids(BidType.TUNNEL, 2, 3);
        unlockBids(BidType.ROOM, 2, 3);

        returnImpsForGold(1, 2, 3);
    }

    /**
     * evaluates room bids in the 4th round where all 3 players bid on room; rooms 2 and 9 are drawn
     * room 2 has restriction outer ring, cost of 4 room 9 has restriction inner ring, cost of 3 and
     * produces niceness 1 player 1 chooses room 2 player 2 chooses room 9 player 3 doesn't get the
     * slot
     *
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void evalRoomBidsRoundFour() throws TimeoutException, AssertionError {
        assertGoldChanged(1, -1, 0);
        assertGoldChanged(2, -1, 0);
        assertGoldChanged(3, -1, 0);
        assertPlaceRoom(1);
        assertActNow(1);
        sendBuildRoom(1, 0, 1, 2);
        assertRoomBuilt(1, 0, 2, 0, 1);
        assertRoomBuilt(2, 0, 2, 0, 1);
        assertRoomBuilt(3, 0, 2, 0, 1);

        assertGoldChanged(1, -1, 1);
        assertGoldChanged(2, -1, 1);
        assertGoldChanged(3, -1, 1);
        assertPlaceRoom(2);
        assertActNow(2);
        sendBuildRoom(2, 1, 1, 9);
        assertRoomBuilt(1, 1, 9, 1, 1);
        assertRoomBuilt(2, 1, 9, 1, 1);
        assertRoomBuilt(3, 1, 9, 1, 1);
    }

    /**
     * player1 is asked to choose battleground, but he leaves same for the remaining players
     *
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void playersLeave() throws TimeoutException, AssertionError {
        assertSetBattleGround(1);
        assertActNow(1);
        sendLeave(1);
        assertLeft(2, 0);
        assertLeft(3, 0);

        assertNextRound(2, 1);
        assertNextRound(3, 1);
        assertSetBattleGround(2);
        assertActNow(2);
        sendLeave(2);
        assertLeft(3, 1);

        assertNextRound(3, 1);
        assertSetBattleGround(3);
        assertActNow(3);
        sendLeave(3);
    }
}
