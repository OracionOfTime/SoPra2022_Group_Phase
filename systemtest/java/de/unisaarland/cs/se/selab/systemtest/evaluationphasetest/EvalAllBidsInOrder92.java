package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import java.util.Set;

public class EvalAllBidsInOrder92 extends EvalAllBidsInOrderOriginal {

    public EvalAllBidsInOrder92() {
        super(EvalAllBidsInOrder92.class, false);
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3);
    }

    @Override
    public void run() throws TimeoutException {
        registrationPhase();
        firstRoundDrawing();
        biddingPhase1FirstBids();
        biddingPhase1SecondBids();
        biddingPhase1ThirdBids();
        evalPhaseFoodBid1();
        evalPhaseNicenessBid1();
        evalPhaseTunnelBid1();
        endOfRound1();
        secondRoundDrawing();
        biddingPhase2FirstBids();
        biddingPhase2SecondBids();
        biddingPhase2ThirdBids();
        evalPhaseFoodBid2();
        evalPhaseGoldBid2();
        evalPhaseImpsBid2();
        evalPhaseTrapBid2();
        evalPhaseMonsterBid2();
        evalPhaseRoomBid2();
        endOfRound2();
        thirdRoundDrawing();
        biddingPhase3FirstBids();
        biddingPhase3SecondBids();
        biddingPhase3ThirdBids();
        evalPhaseNicenessBid3();
        evalPhaseTunnelBid3();
        evalPhaseGoldBid3();
        evalPhaseImpBid3();
        evalPhaseMonsterBid3();
        evalPhaseRoomBid3();
        endOfRound3();
        fourthRoundDrawing();
        biddingPhase4FirstBids();
        biddingPhase4SecondBids();
        biddingPhase4ThirdBids();
        evalPhaseTunnelBid4();
        evalPhaseImpBid4();
        evalPhaseTrapBid4();
        evalPhaseMonsterBid4();
        evalPhaseRoomBid4();
        endOfRound4();
        this.sendLeave(1);
    }

    public void biddingPhase3FirstBids() throws TimeoutException, AssertionError {

        this.sendPlaceBid(1, BidType.TUNNEL, 1);

        this.assertBidPlaced(1, BidType.TUNNEL, 0, 1);
        this.assertBidPlaced(2, BidType.TUNNEL, 0, 1);
        this.assertBidPlaced(3, BidType.TUNNEL, 0, 1);

        this.assertActNow(1);

        this.sendPlaceBid(2, BidType.GOLD, 1);

        this.assertBidPlaced(1, BidType.GOLD, 1, 1);
        this.assertBidPlaced(2, BidType.GOLD, 1, 1);
        this.assertBidPlaced(3, BidType.GOLD, 1, 1);

        this.assertActNow(2);

        this.sendPlaceBid(3, BidType.MONSTER, 1);

        this.assertBidPlaced(1, BidType.MONSTER, 2, 1);
        this.assertBidPlaced(2, BidType.MONSTER, 2, 1);
        this.assertBidPlaced(3, BidType.MONSTER, 2, 1);

        this.assertActNow(3);
    }

    public void biddingPhase3SecondBids() throws TimeoutException, AssertionError {
        //first players second bid error
        this.sendPlaceBid(1, BidType.IMPS, 2);
        this.assertActionFailed(1);
        this.assertActNow(1);
        //normal bidding
        this.sendPlaceBid(1, BidType.ROOM, 2);

        this.assertBidPlaced(1, BidType.ROOM, 0, 2);
        this.assertBidPlaced(2, BidType.ROOM, 0, 2);
        this.assertBidPlaced(3, BidType.ROOM, 0, 2);

        this.assertActNow(1);
        //second players second bid error #1
        this.sendPlaceBid(2, BidType.TRAP, 2);
        this.assertActionFailed(2);
        this.assertActNow(2);
        //second players second bid error #2
        this.sendPlaceBid(2, BidType.GOLD, 2);
        this.assertActionFailed(2);
        this.assertActNow(2);

        this.sendPlaceBid(2, BidType.IMPS, 2);

        this.assertBidPlaced(1, BidType.IMPS, 1, 2);
        this.assertBidPlaced(2, BidType.IMPS, 1, 2);
        this.assertBidPlaced(3, BidType.IMPS, 1, 2);

        this.assertActNow(2);

        this.sendPlaceBid(3, BidType.ROOM, 2);

        this.assertBidPlaced(1, BidType.ROOM, 2, 2);
        this.assertBidPlaced(2, BidType.ROOM, 2, 2);
        this.assertBidPlaced(3, BidType.ROOM, 2, 2);

        this.assertActNow(3);
    }

    public void biddingPhase3ThirdBids() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.NICENESS, 3);

        this.assertBidPlaced(1, BidType.NICENESS, 0, 3);
        this.assertBidPlaced(2, BidType.NICENESS, 0, 3);
        this.assertBidPlaced(3, BidType.NICENESS, 0, 3);

        this.sendPlaceBid(2, BidType.MONSTER, 3);

        this.assertBidPlaced(1, BidType.MONSTER, 1, 3);
        this.assertBidPlaced(2, BidType.MONSTER, 1, 3);
        this.assertBidPlaced(3, BidType.MONSTER, 1, 3);

        this.sendPlaceBid(3, BidType.IMPS, 3);

        this.assertBidPlaced(1, BidType.IMPS, 2, 3);
        this.assertBidPlaced(2, BidType.IMPS, 2, 3);
        this.assertBidPlaced(3, BidType.IMPS, 2, 3);
    }

    public void evalPhaseNicenessBid3() throws TimeoutException, AssertionError {
        //player1
        this.assertEvilnessChanged(1, -1, 0);
        this.assertEvilnessChanged(2, -1, 0);
        this.assertEvilnessChanged(3, -1, 0);
    }

    public void evalPhaseTunnelBid3() throws TimeoutException, AssertionError {
        //player1
        this.assertDigTunnel(1);

        this.assertActNow(1);
        this.sendDigTunnel(1, 1, 1);
        this.assertActionFailed(1);
        this.assertActNow(1);

        this.sendDigTunnel(1, 0, 2);
        this.assertImpsChanged(1, -1, 0);
        this.assertImpsChanged(2, -1, 0);
        this.assertImpsChanged(3, -1, 0);

        this.assertTunnelDug(1, 0, 0, 2);
        this.assertTunnelDug(2, 0, 0, 2);
        this.assertTunnelDug(3, 0, 0, 2);

        this.assertActNow(1);

        this.sendDigTunnel(1, 1, 2);
        this.assertImpsChanged(1, -1, 0);
        this.assertImpsChanged(2, -1, 0);
        this.assertImpsChanged(3, -1, 0);

        this.assertTunnelDug(1, 0, 1, 2);
        this.assertTunnelDug(2, 0, 1, 2);
        this.assertTunnelDug(3, 0, 1, 2);
    }

    public void evalPhaseGoldBid3() throws TimeoutException, AssertionError {
        //player2
        this.assertImpsChanged(1, -2, 1);
        this.assertImpsChanged(2, -2, 1);
        this.assertImpsChanged(3, -2, 1);
    }

    public void evalPhaseImpBid3() throws TimeoutException, AssertionError {
        //player2
        this.assertFoodChanged(1, -1, 1);
        this.assertFoodChanged(2, -1, 1);
        this.assertFoodChanged(3, -1, 1);

        this.assertImpsChanged(1, 1, 1);
        this.assertImpsChanged(2, 1, 1);
        this.assertImpsChanged(3, 1, 1);

        //player3
        this.assertFoodChanged(1, -2, 2);
        this.assertFoodChanged(2, -2, 2);
        this.assertFoodChanged(3, -2, 2);

        this.assertImpsChanged(1, 2, 2);
        this.assertImpsChanged(2, 2, 2);
        this.assertImpsChanged(3, 2, 2);
    }

    public void evalPhaseMonsterBid3() throws TimeoutException, AssertionError {
        //player3
        this.assertSelectMonster(3);
        this.assertActNow(3);
        this.sendHireMonster(3, 14);

        this.assertFoodChanged(1, -1, 2);
        this.assertFoodChanged(2, -1, 2);
        this.assertFoodChanged(3, -1, 2);

        this.assertEvilnessChanged(1, 1, 2);
        this.assertEvilnessChanged(2, 1, 2);
        this.assertEvilnessChanged(3, 1, 2);

        this.assertMonsterHired(1, 14, 2);
        this.assertMonsterHired(2, 14, 2);
        this.assertMonsterHired(3, 14, 2);

        //player2
        this.assertSelectMonster(2);
        this.assertActNow(2);
        this.sendHireMonster(2, 14);
        this.assertActionFailed(2);
        this.assertActNow(2);
        this.sendHireMonster(2, 3);

        this.assertFoodChanged(1, -2, 1);
        this.assertFoodChanged(2, -2, 1);
        this.assertFoodChanged(3, -2, 1);

        this.assertEvilnessChanged(1, 1, 1);
        this.assertEvilnessChanged(2, 1, 1);
        this.assertEvilnessChanged(3, 1, 1);

        this.assertMonsterHired(1, 3, 1);
        this.assertMonsterHired(2, 3, 1);
        this.assertMonsterHired(3, 3, 1);
    }

    public void evalPhaseRoomBid3() throws TimeoutException, AssertionError {
        //player3
        this.assertGoldChanged(1, -1, 2);
        this.assertGoldChanged(2, -1, 2);
        this.assertGoldChanged(3, -1, 2);

        this.assertPlaceRoom(3);
        this.assertActNow(3);
        this.sendBuildRoom(3, 1, 0, 0);
        this.assertActionFailed(3);
        this.assertActNow(3);
        this.sendEndTurn(3);
        //player1
        this.assertGoldChanged(1, -1, 0);
        this.assertGoldChanged(2, -1, 0);
        this.assertGoldChanged(3, -1, 0);

        this.assertPlaceRoom(1);
        this.assertActNow(1);
        this.sendBuildRoom(1, 1, 2, 10);

        this.assertRoomBuilt(1, 0, 10, 1, 2);
        this.assertRoomBuilt(2, 0, 10, 1, 2);
        this.assertRoomBuilt(3, 0, 10, 1, 2);
    }

    public void endOfRound3() throws TimeoutException, AssertionError {
        //retrieve player1 bids
        this.assertBidRetrieved(1, BidType.IMPS, 0);
        this.assertBidRetrieved(2, BidType.IMPS, 0);
        this.assertBidRetrieved(3, BidType.IMPS, 0);

        this.assertBidRetrieved(1, BidType.TRAP, 0);
        this.assertBidRetrieved(2, BidType.TRAP, 0);
        this.assertBidRetrieved(3, BidType.TRAP, 0);

        this.assertBidRetrieved(1, BidType.TUNNEL, 0);
        this.assertBidRetrieved(2, BidType.TUNNEL, 0);
        this.assertBidRetrieved(3, BidType.TUNNEL, 0);
        //retrieve player2 bids
        this.assertBidRetrieved(1, BidType.ROOM, 1);
        this.assertBidRetrieved(2, BidType.ROOM, 1);
        this.assertBidRetrieved(3, BidType.ROOM, 1);

        this.assertBidRetrieved(1, BidType.TRAP, 1);
        this.assertBidRetrieved(2, BidType.TRAP, 1);
        this.assertBidRetrieved(3, BidType.TRAP, 1);

        this.assertBidRetrieved(1, BidType.GOLD, 1);
        this.assertBidRetrieved(2, BidType.GOLD, 1);
        this.assertBidRetrieved(3, BidType.GOLD, 1);
        //retrieve player3 bids
        this.assertBidRetrieved(1, BidType.FOOD, 2);
        this.assertBidRetrieved(2, BidType.FOOD, 2);
        this.assertBidRetrieved(3, BidType.FOOD, 2);

        this.assertBidRetrieved(1, BidType.GOLD, 2);
        this.assertBidRetrieved(2, BidType.GOLD, 2);
        this.assertBidRetrieved(3, BidType.GOLD, 2);

        this.assertBidRetrieved(1, BidType.MONSTER, 2);
        this.assertBidRetrieved(2, BidType.MONSTER, 2);
        this.assertBidRetrieved(3, BidType.MONSTER, 2);

        //retrieve player1 imps
        this.assertImpsChanged(1, 2, 0);
        this.assertImpsChanged(2, 2, 0);
        this.assertImpsChanged(3, 2, 0);
        //retrieve player2 imps
        this.assertImpsChanged(1, 2, 1);
        this.assertImpsChanged(2, 2, 1);
        this.assertImpsChanged(3, 2, 1);

        this.assertGoldChanged(1, 2, 1);
        this.assertGoldChanged(2, 2, 1);
        this.assertGoldChanged(3, 2, 1);

        //assign adventurers
        this.assertAdventurerArrived(1, 9, 0);
        this.assertAdventurerArrived(2, 9, 0);
        this.assertAdventurerArrived(3, 9, 0);

        this.assertAdventurerArrived(1, 20, 1);
        this.assertAdventurerArrived(2, 20, 1);
        this.assertAdventurerArrived(3, 20, 1);

        this.assertAdventurerArrived(1, 6, 2);
        this.assertAdventurerArrived(2, 6, 2);
        this.assertAdventurerArrived(3, 6, 2);
    }

    public void fourthRoundDrawing() throws TimeoutException, AssertionError {
        this.assertNextRound(1, 4);
        this.assertNextRound(2, 4);
        this.assertNextRound(3, 4);

        this.assertMonsterDrawn(1, 6);
        this.assertMonsterDrawn(2, 6);
        this.assertMonsterDrawn(3, 6);

        this.assertMonsterDrawn(1, 11);
        this.assertMonsterDrawn(2, 11);
        this.assertMonsterDrawn(3, 11);

        this.assertMonsterDrawn(1, 16);
        this.assertMonsterDrawn(2, 16);
        this.assertMonsterDrawn(3, 16);

        this.assertRoomDrawn(1, 2);
        this.assertRoomDrawn(2, 2);
        this.assertRoomDrawn(3, 2);

        this.assertRoomDrawn(1, 9);
        this.assertRoomDrawn(2, 9);
        this.assertRoomDrawn(3, 9);

        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);
        this.assertBiddingStarted(3);

        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);
    }

    public void biddingPhase4FirstBids() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.IMPS, 1);

        this.assertBidPlaced(1, BidType.IMPS, 0, 1);
        this.assertBidPlaced(2, BidType.IMPS, 0, 1);
        this.assertBidPlaced(3, BidType.IMPS, 0, 1);

        this.assertActNow(1);

        this.sendPlaceBid(2, BidType.TRAP, 1);

        this.assertBidPlaced(1, BidType.TRAP, 1, 1);
        this.assertBidPlaced(2, BidType.TRAP, 1, 1);
        this.assertBidPlaced(3, BidType.TRAP, 1, 1);

        this.assertActNow(2);

        this.sendPlaceBid(3, BidType.TRAP, 1);

        this.assertBidPlaced(1, BidType.TRAP, 2, 1);
        this.assertBidPlaced(2, BidType.TRAP, 2, 1);
        this.assertBidPlaced(3, BidType.TRAP, 2, 1);

        this.assertActNow(3);
    }

    public void biddingPhase4SecondBids() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.MONSTER, 2);

        this.assertBidPlaced(1, BidType.MONSTER, 0, 2);
        this.assertBidPlaced(2, BidType.MONSTER, 0, 2);
        this.assertBidPlaced(3, BidType.MONSTER, 0, 2);

        this.assertActNow(1);

        this.sendPlaceBid(2, BidType.TUNNEL, 2);

        this.assertBidPlaced(1, BidType.TUNNEL, 1, 2);
        this.assertBidPlaced(2, BidType.TUNNEL, 1, 2);
        this.assertBidPlaced(3, BidType.TUNNEL, 1, 2);

        this.assertActNow(2);

        this.sendPlaceBid(3, BidType.MONSTER, 2);

        this.assertBidPlaced(1, BidType.MONSTER, 2, 2);
        this.assertBidPlaced(2, BidType.MONSTER, 2, 2);
        this.assertBidPlaced(3, BidType.MONSTER, 2, 2);

        this.assertActNow(3);
    }

    public void biddingPhase4ThirdBids() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.TUNNEL, 3);

        this.assertBidPlaced(1, BidType.TUNNEL, 0, 3);
        this.assertBidPlaced(2, BidType.TUNNEL, 0, 3);
        this.assertBidPlaced(3, BidType.TUNNEL, 0, 3);

        this.sendPlaceBid(2, BidType.ROOM, 3);

        this.assertBidPlaced(1, BidType.ROOM, 1, 3);
        this.assertBidPlaced(2, BidType.ROOM, 1, 3);
        this.assertBidPlaced(3, BidType.ROOM, 1, 3);

        this.sendPlaceBid(3, BidType.TUNNEL, 3);

        this.assertBidPlaced(1, BidType.TUNNEL, 2, 3);
        this.assertBidPlaced(2, BidType.TUNNEL, 2, 3);
        this.assertBidPlaced(3, BidType.TUNNEL, 2, 3);
    }

    public void evalPhaseTunnelBid4() throws TimeoutException, AssertionError {
        //player2
        this.assertDigTunnel(2);
        this.assertActNow(2);
        this.sendDigTunnel(2, 0, 2);
        this.assertImpsChanged(1, -1, 1);
        this.assertImpsChanged(2, -1, 1);
        this.assertImpsChanged(3, -1, 1);
        this.assertTunnelDug(1, 1, 0, 2);
        this.assertTunnelDug(2, 1, 0, 2);
        this.assertTunnelDug(3, 1, 0, 2);
        this.assertActNow(2);
        this.sendDigTunnel(2, 1, 2);
        this.assertImpsChanged(1, -1, 1);
        this.assertImpsChanged(2, -1, 1);
        this.assertImpsChanged(3, -1, 1);
        this.assertTunnelDug(1, 1, 1, 2);
        this.assertTunnelDug(2, 1, 1, 2);
        this.assertTunnelDug(3, 1, 1, 2);
        //player1
        this.assertDigTunnel(1);
        this.assertActNow(1);
        this.sendDigTunnel(1, 2, 2);
        this.assertImpsChanged(1, -1, 0);
        this.assertImpsChanged(2, -1, 0);
        this.assertImpsChanged(3, -1, 0);
        this.assertTunnelDug(1, 0, 2, 2);
        this.assertTunnelDug(2, 0, 2, 2);
        this.assertTunnelDug(3, 0, 2, 2);
        this.assertActNow(1);
        this.sendDigTunnel(1, 2, 1);
        this.assertImpsChanged(1, -1, 0);
        this.assertImpsChanged(2, -1, 0);
        this.assertImpsChanged(3, -1, 0);
        this.assertTunnelDug(1, 0, 2, 1);
        this.assertTunnelDug(2, 0, 2, 1);
        this.assertTunnelDug(3, 0, 2, 1);
        this.assertActNow(1);
        this.sendDigTunnel(1, 2, 0);
        this.assertImpsChanged(1, -1, 0);
        this.assertImpsChanged(2, -1, 0);
        this.assertImpsChanged(3, -1, 0);
        this.assertTunnelDug(1, 0, 2, 0);
        this.assertTunnelDug(2, 0, 2, 0);
        this.assertTunnelDug(3, 0, 2, 0);
        //player3
        this.assertDigTunnel(3);
        this.assertActNow(3);
        this.sendDigTunnel(3, 2, 1);
        this.assertImpsChanged(1, -1, 2);
        this.assertImpsChanged(2, -1, 2);
        this.assertImpsChanged(3, -1, 2);
        this.assertTunnelDug(1, 2, 2, 1);
        this.assertTunnelDug(2, 2, 2, 1);
        this.assertTunnelDug(3, 2, 2, 1);
        this.assertActNow(3);
        this.sendDigTunnel(3, 2, 2);
        this.assertImpsChanged(1, -1, 2);
        this.assertImpsChanged(2, -1, 2);
        this.assertImpsChanged(3, -1, 2);
        this.assertTunnelDug(1, 2, 2, 2);
        this.assertTunnelDug(2, 2, 2, 2);
        this.assertTunnelDug(3, 2, 2, 2);
        this.assertActNow(3);
        this.sendDigTunnel(3, 0, 2);
        this.assertImpsChanged(1, -1, 2);
        this.assertImpsChanged(2, -1, 2);
        this.assertImpsChanged(3, -1, 2);
        this.assertTunnelDug(1, 2, 0, 2);
        this.assertTunnelDug(2, 2, 0, 2);
        this.assertTunnelDug(3, 2, 0, 2);
        this.assertActNow(3);
        this.sendDigTunnel(3, 1, 2);
        this.assertImpsChanged(1, -2, 2);
        this.assertImpsChanged(2, -2, 2);
        this.assertImpsChanged(3, -2, 2);
        this.assertTunnelDug(1, 2, 1, 2);
        this.assertTunnelDug(2, 2, 1, 2);
        this.assertTunnelDug(3, 2, 1, 2);
    }

    public void evalPhaseImpBid4() throws TimeoutException, AssertionError {
        //player1
        this.assertFoodChanged(1, -1, 0);
        this.assertFoodChanged(2, -1, 0);
        this.assertFoodChanged(3, -1, 0);

        this.assertImpsChanged(1, 1, 0);
        this.assertImpsChanged(2, 1, 0);
        this.assertImpsChanged(3, 1, 0);
    }

    public void evalPhaseTrapBid4() throws TimeoutException, AssertionError {
        //player2
        this.assertGoldChanged(1, -1, 1);
        this.assertGoldChanged(2, -1, 1);
        this.assertGoldChanged(3, -1, 1);

        this.assertTrapAcquired(1, 1, 19);
        this.assertTrapAcquired(2, 1, 19);
        this.assertTrapAcquired(3, 1, 19);
        //player3
        this.assertTrapAcquired(1, 2, 5);
        this.assertTrapAcquired(2, 2, 5);
        this.assertTrapAcquired(3, 2, 5);
    }

    public void evalPhaseMonsterBid4() throws TimeoutException, AssertionError {
        //player1
        this.assertSelectMonster(1);
        this.assertActNow(1);
        this.sendActivateRoom(1, 10);
        this.assertActionFailed(1);
        this.assertActNow(1);

        this.sendEndTurn(1);
        //this.assertActionFailed(1);
        //this.assertActNow(1);

        /*this.sendHireMonster(1, 6);
        this.assertFoodChanged(1, -1, 0);
        this.assertFoodChanged(2, -1, 0);
        this.assertFoodChanged(3, -1, 0);

        this.assertEvilnessChanged(1, 1, 0);
        this.assertEvilnessChanged(2, 1, 0);
        this.assertEvilnessChanged(3, 1, 0);

        this.assertMonsterHired(1, 6, 0);
        this.assertMonsterHired(2, 6, 0);
        this.assertMonsterHired(3, 6, 0);*/
        //player3
        this.assertSelectMonster(3);
        this.assertActNow(3);
        //this.sendEndTurn(3);
        //this.assertActionFailed(3);
        //this.assertActNow(3);
        this.sendLeave(3);

        this.assertLeft(1, 2);
        this.assertLeft(2, 2);
    }

    public void evalPhaseRoomBid4() throws TimeoutException, AssertionError {
        //player2
        this.assertGoldChanged(1, -1, 1);
        this.assertGoldChanged(2, -1, 1);
        //this.assertGoldChanged(3, -1, 1);

        this.assertPlaceRoom(2);
        this.assertActNow(2);
        this.sendLeave(2);
        this.assertLeft(1, 1);
    }

    public void endOfRound4() throws TimeoutException, AssertionError {
        //retrieve player1 bids
        this.assertBidRetrieved(1, BidType.ROOM, 0);

        this.assertBidRetrieved(1, BidType.NICENESS, 0);

        this.assertBidRetrieved(1, BidType.IMPS, 0);
        //retrieve player1 imps
        this.assertImpsChanged(1, 3, 0);

        this.assertNextRound(1, 1);
        this.assertSetBattleGround(1);
        this.assertActNow(1);
    }

}
