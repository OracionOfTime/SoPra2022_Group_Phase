package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import java.util.Set;

public class EvalAllBidsInOrder5 extends EvalAllBidsInOrderOriginal {

    public EvalAllBidsInOrder5() {
        super(EvalAllBidsInOrder5.class, false);
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
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

}
