package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class NiceTestsEvaluation extends SystemTest {


    public NiceTestsEvaluation(final Class<?> sub, final boolean fail) {
        super(sub, fail);
    }

    public NiceTestsEvaluation() {
        super(EvalAllBidsInOrderOriginal.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "configuration.json");
    }

    @Override
    public long createSeed() {
        return 42;
    }


    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2);
    }

    @Override
    public void run() throws TimeoutException {
        //Empty
    }

    public void registrationPhase() throws TimeoutException, AssertionError {
        final String config = createConfig();
        this.sendRegister(1, "Player1");
        this.assertConfig(1, config);
        this.sendRegister(2, "Player2");
        this.assertConfig(2, config);

        this.sendStartGame(1);
        this.assertGameStarted(1);
        this.assertGameStarted(2);

        this.assertPlayer(1, "Player1", 0);
        this.assertPlayer(2, "Player1", 0);

        this.assertPlayer(1, "Player2", 1);
        this.assertPlayer(2, "Player2", 1);

    }

    public void firstRoundDrawing() throws TimeoutException, AssertionError {
        this.assertNextYear(1, 1);
        this.assertNextYear(2, 1);

        this.assertNextRound(1, 1);
        this.assertNextRound(2, 1);

        this.assertAdventurerDrawn(1, 29);
        this.assertAdventurerDrawn(2, 29);
        this.assertAdventurerDrawn(1, 23);
        this.assertAdventurerDrawn(2, 23);

        this.assertMonsterDrawn(1, 23);
        this.assertMonsterDrawn(2, 23);
        this.assertMonsterDrawn(1, 13);
        this.assertMonsterDrawn(2, 13);
        this.assertMonsterDrawn(1, 9);
        this.assertMonsterDrawn(2, 9);

        this.assertRoomDrawn(1, 5);
        this.assertRoomDrawn(2, 5);
        this.assertRoomDrawn(1, 4);
        this.assertRoomDrawn(2, 4);

        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);

        this.assertActNow(1);
        this.assertActNow(2);
    }


    public void biddingPhaseOne() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.FOOD, 1);

        this.assertBidPlaced(1, BidType.FOOD, 0, 1);
        this.assertBidPlaced(2, BidType.FOOD, 0, 1);

        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.FOOD, 2);
        this.assertActionFailed(1);

        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.MONSTER, 1);
        this.assertActionFailed(1);

        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.MONSTER, -1);
        this.assertActionFailed(1);
        this.assertActNow(1);

        this.sendPlaceBid(2, BidType.MONSTER, 1);

        this.assertBidPlaced(1, BidType.MONSTER, 1, 1);
        this.assertBidPlaced(2, BidType.MONSTER, 1, 1);

        this.assertActNow(2);


    }

    public void biddingPhaseTwo() throws TimeoutException, AssertionError {
        sendPlaceBid(1, BidType.TUNNEL, 2);

        this.assertBidPlaced(1, BidType.TUNNEL, 0, 2);
        this.assertBidPlaced(2, BidType.TUNNEL, 0, 2);

        this.assertActNow(1);

        this.sendPlaceBid(2, BidType.ROOM, 2);

        this.assertBidPlaced(1, BidType.ROOM, 1, 2);
        this.assertBidPlaced(2, BidType.ROOM, 1, 2);

        this.assertActNow(2);

    }

    public void biddingPhaseThree() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.TRAP, 3);

        this.assertBidPlaced(1, BidType.TRAP, 0, 3);
        this.assertBidPlaced(2, BidType.TRAP, 0, 3);

        this.sendPlaceBid(2, BidType.IMPS, 3);

        this.assertBidPlaced(1, BidType.IMPS, 1, 3);
        this.assertBidPlaced(2, BidType.IMPS, 1, 3);

    }

    public void evalFoodTunnelBid() throws TimeoutException, AssertionError {
        //player 0 food
        this.assertGoldChanged(1, -1, 0);
        this.assertGoldChanged(2, -1, 0);
        this.assertFoodChanged(1, 2, 0);
        this.assertFoodChanged(2, 2, 0);
        //player 1 tunnel
        this.assertDigTunnel(1);
        this.assertActionFailed(1);
        this.sendDigTunnel(1, 5, 5);
        this.assertActionFailed(1);
        this.sendDigTunnel(1, 0, 1);
        this.assertImpsChanged(1, -1, 0);
        this.assertImpsChanged(2, -1, 0);
        this.assertTunnelDug(1, 0, 0, 1);
        this.assertTunnelDug(2, 0, 0, 1);
        this.sendEndTurn(1);
    }

    public void evalImpsTrap() throws TimeoutException, AssertionError {
        //player 0 imps
        this.assertFoodChanged(1, -1, 1);
        this.assertFoodChanged(2, -1, 1);
        this.assertImpsChanged(1, 1, 1);
        this.assertImpsChanged(2, 1, 1);
        //player 1 traps
        this.assertGoldChanged(1, -1, 0);
        this.assertGoldChanged(2, -1, 0);
        this.assertTrapAcquired(1, 0, 26);
        this.assertTrapAcquired(2, 0, 26);
    }

    public void evalMonsterRoom() throws TimeoutException, AssertionError {
        //player 1 monster
        this.assertSelectMonster(2);
        this.assertActNow(2);
        this.sendHireMonster(2, 111);
        this.assertActionFailed(2);
        this.sendHireMonster(2, 23);
        this.assertMonsterHired(1, 23, 1);
        this.assertMonsterHired(2, 23, 1);
        //player 1 trap
        this.assertGoldChanged(1, -1, 1);
        this.assertGoldChanged(1, -1, 1);
        this.assertPlaceRoom(2);
        this.assertActNow(2);
        this.sendBuildRoom(2, -1, 1, 4);
        this.assertActionFailed(2);
        this.sendBuildRoom(2, -1, 1, 3);
        this.assertActionFailed(2);
        this.sendBuildRoom(2, 0, 0, 4);
        this.assertActionFailed(2);
        this.sendEndTurn(2);


    }

    public void drawNextRound() throws TimeoutException {
        this.assertBidRetrieved(1, BidType.FOOD, 0);
        this.assertBidRetrieved(2, BidType.FOOD, 0);
        this.assertBidRetrieved(1, BidType.MONSTER, 1);
        this.assertBidRetrieved(2, BidType.MONSTER, 1);

        this.assertImpsChanged(1, 1, 0);
        this.assertImpsChanged(2, 1, 0);

        this.assertAdventurerArrived(1, 29, 0);
        this.assertAdventurerArrived(2, 29, 0);
        this.assertAdventurerArrived(1, 23, 1);
        this.assertAdventurerArrived(2, 23, 1);

        this.assertNextRound(1, 2);
        this.assertNextRound(2, 2);

        this.assertAdventurerDrawn(1, 2);
        this.assertAdventurerDrawn(2, 2);

        this.assertAdventurerDrawn(1, 0);
        this.assertAdventurerDrawn(2, 0);

        this.assertMonsterDrawn(1, 7);
        this.assertMonsterDrawn(2, 7);

        this.assertMonsterDrawn(1, 22);
        this.assertMonsterDrawn(2, 22);

        this.assertMonsterDrawn(1, 1);
        this.assertMonsterDrawn(2, 1);

        this.assertRoomDrawn(1, 8);
        this.assertRoomDrawn(2, 8);

        this.assertRoomDrawn(1, 15);
        this.assertRoomDrawn(2, 15);

        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);

        this.assertActNow(1);
        this.assertActNow(2);

    }


}