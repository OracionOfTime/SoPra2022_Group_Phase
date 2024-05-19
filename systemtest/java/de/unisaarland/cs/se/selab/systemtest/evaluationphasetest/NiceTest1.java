package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import java.util.Set;

public class NiceTest1 extends NiceTestsEvaluation {


    public NiceTest1() {
        super(NiceTest1.class, false);
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2);
    }

    @Override
    public void run() throws TimeoutException {
        registrationPhase();
        biddingPhaseOne();
        biddingPhaseTwo();
        this.sendLeave(1);
        this.sendLeave(2);
    }
}
