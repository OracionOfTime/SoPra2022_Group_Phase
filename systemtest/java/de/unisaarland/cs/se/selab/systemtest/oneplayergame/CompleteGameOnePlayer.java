package de.unisaarland.cs.se.selab.systemtest.oneplayergame;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public abstract class CompleteGameOnePlayer extends SystemTest {

    public CompleteGameOnePlayer(final Class<?> subclass, final boolean fail) {
        super(subclass, fail);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(CompleteGameOnePlayer.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1);
    }

    @Override
    public abstract void run() throws TimeoutException, AssertionError;

    public void round1() throws TimeoutException, AssertionError {

        this.sendStartGame(1);

        this.assertGameStarted(1);

        this.assertPlayer(1, "Player1", 0);

        this.assertNextYear(1, 1);

        this.assertNextRound(1, 1);

        skipDrawing();

        this.assertBiddingStarted(1);

        this.assertActNow(1);

        sendPlaceBid(1, BidType.FOOD, 1);
        assertBidPlaced(1, BidType.FOOD, 0, 1);
        assertActNow(1);

        sendPlaceBid(1, BidType.GOLD, 2);
        assertBidPlaced(1, BidType.GOLD, 0, 2);
        assertActNow(1);

        sendPlaceBid(1, BidType.NICENESS, 3);
        assertBidPlaced(1, BidType.NICENESS, 0, 3);

        //Evaluation of Bids

        //Food slot1
        assertGoldChanged(1, -1, 0);
        assertFoodChanged(1, 2, 0);

        //Niceness slot1
        assertEvilnessChanged(1, -1, 0);

        //Gold slot1
        assertImpsChanged(1, -1, 0);

        assertBidRetrieved(1, BidType.FOOD, 0);

        //Retrieve Imps: Gold/Tunnel
        assertImpsChanged(1, 1, 0);
        assertGoldChanged(1, 1, 0);

        //assertAdventurerArrived
        assertEvent(1);

        assertNextRound(1, 2);

        skipDrawing();

        assertBiddingStarted(1);
        assertActNow(1);

    }

    public void round2() throws TimeoutException {
        //First Round Completed

        sendPlaceBid(1, BidType.IMPS, 1);
        assertBidPlaced(1, BidType.IMPS, 0, 1);
        assertActNow(1);

        sendPlaceBid(1, BidType.TUNNEL, 2);
        assertBidPlaced(1, BidType.TUNNEL, 0, 2);
        assertActNow(1);

        sendPlaceBid(1, BidType.TRAP, 3);
        assertBidPlaced(1, BidType.TRAP, 0, 3);

        //Evaluation

        //Tunnel
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        assertImpsChanged(1, -1, 0);
        assertTunnelDug(1, 0, 0, 1);

        assertActNow(1);
        sendDigTunnel(1, 1, 0);
        assertImpsChanged(1, -1, 0);
        assertTunnelDug(1, 0, 1, 0);

        //Imps
        assertFoodChanged(1, -1, 0);
        assertImpsChanged(1, 1, 0);

        //Traps
        assertGoldChanged(1, -1, 0);
        assertTrapAcquired(1, 0, 26);

        assertBidRetrieved(1, BidType.GOLD, 0);
        assertBidRetrieved(1, BidType.NICENESS, 0);
        assertBidRetrieved(1, BidType.IMPS, 0);

        assertImpsChanged(1, 2, 0);

        //Adventure arrived
        assertEvent(1);

        assertNextRound(1, 3);

        skipDrawing();

        assertBiddingStarted(1);
        assertActNow(1);

    }

    public void round3() throws TimeoutException {

        //Blocked bid from last round
        sendPlaceBid(1, BidType.TUNNEL, 1);
        assertActionFailed(1);
        assertActNow(1);

        sendPlaceBid(1, BidType.MONSTER, 1);
        assertBidPlaced(1, BidType.MONSTER, 0, 1);
        assertActNow(1);

        sendPlaceBid(1, BidType.ROOM, 2);
        assertBidPlaced(1, BidType.ROOM, 0, 2);
        assertActNow(1);

        sendPlaceBid(1, BidType.IMPS, 3);
        assertBidPlaced(1, BidType.IMPS, 0, 3);

        // Imps
        assertFoodChanged(1, -1, 0);
        assertImpsChanged(1, 1, 0);

        //Monster
        assertSelectMonster(1);
        assertActNow(1);

        sendHireMonster(1, 3);

        assertFoodChanged(1, -2, 0);
        assertEvilnessChanged(1, 1, 0);

        assertMonsterHired(1, 3, 0);

        //ROOM
        assertGoldChanged(1, -1, 0);
        assertPlaceRoom(1);
        assertActNow(1);

        sendBuildRoom(1, 0, 0, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendEndTurn(1);

        //BID retrieving
        assertBidRetrieved(1, BidType.TUNNEL, 0);
        assertBidRetrieved(1, BidType.TRAP, 0);
        assertBidRetrieved(1, BidType.MONSTER, 0);

        //Adventurer Arrived
        assertEvent(1);

        assertNextRound(1, 4);

        for (int i = 0; i < 5; i++) {
            this.assertEvent(1);
        }

        assertBiddingStarted(1);
        assertActNow(1);

    }

    public void round4() throws TimeoutException {
        sendPlaceBid(1, BidType.FOOD, 1);
        assertBidPlaced(1, BidType.FOOD, 0, 1);
        assertActNow(1);

        sendPlaceBid(1, BidType.GOLD, 2);
        assertBidPlaced(1, BidType.GOLD, 0, 2);
        assertActNow(1);

        sendPlaceBid(1, BidType.TUNNEL, 3);
        assertBidPlaced(1, BidType.TUNNEL, 0, 3);

        //Evaluation
        //FOOD
        assertGoldChanged(1, -1, 0);
        assertFoodChanged(1, 2, 0);

        //Tunnel
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 1, 1);  //CAN not be dug
        assertActionFailed(1);
        assertActNow(1);

        sendDigTunnel(1, 2, 0);
        assertImpsChanged(1, -1, 0);
        assertTunnelDug(1, 0, 2, 0);

        assertActNow(1);
        sendEndTurn(1);

        //GOLD
        assertImpsChanged(1, -2, 0);

        assertBidRetrieved(1, BidType.ROOM, 0);
        assertBidRetrieved(1, BidType.IMPS, 0);
        assertBidRetrieved(1, BidType.FOOD, 0);

        //GOLD Tunnel production
        assertImpsChanged(1, 3, 0);
        assertGoldChanged(1, 2, 0);

        assertNextRound(1, 1);
        assertSetBattleGround(1);

        assertActNow(1);

    }

    /*
    public void combatRound1() throws TimeoutException {
        sendBattleGround(1, 1, 1);
        assertActionFailed(1);
        assertActNow(1);

        sendBattleGround(1, 3, 3);
        assertActionFailed(1);
        assertActNow(1);

        sendBattleGround(1, 0, 0);
        assertBattleGroundSet(1, 0, 0, 0);

        assertDefendYourself(1);
        assertActNow(1);

        //Placing Trap
        sendTrap(1, 26);
        assertTrapPlaced(1, 0, 26);


        assertActNow(1);

        //Monster
        sendMonster(1, 3);
        assertMonsterPlaced(1, 3, 0);

        assertActNow(1);
        sendEndTurn(1);

        assertAdventurerDamaged(1, 6, 1);
        assertTunnelConquered(1, 6, 0 ,0);

        assertEvilnessChanged(1, -1, 0);




    }

     */

    public void skipDrawing() throws TimeoutException {
        for (int i = 0; i < 6; i++) {
            this.assertEvent(1);
        }
    }
}
