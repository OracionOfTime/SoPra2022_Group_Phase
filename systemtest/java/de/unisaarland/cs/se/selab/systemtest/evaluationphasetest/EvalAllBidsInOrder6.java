package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import java.util.Set;

public class EvalAllBidsInOrder6 extends EvalAllBidsInOrderOriginal {

    public EvalAllBidsInOrder6() {
        super(EvalAllBidsInOrder6.class, false);
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
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
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
}

