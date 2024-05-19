package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import java.util.Set;

public class EvalAllBidsInOrder3 extends EvalAllBidsInOrderOriginal {

    public EvalAllBidsInOrder3() {
        super(EvalAllBidsInOrder3.class, false);
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
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }
}
