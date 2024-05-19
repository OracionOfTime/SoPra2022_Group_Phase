package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class EvaluateMonsterBidsround3 extends EvaluateMonsterBids {

    public EvaluateMonsterBidsround3() {
        super(EvaluateMonsterBidsround3.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();
        final Set<Integer> socket = createSockets();
        this.registration(socket, config);
        this.firstBid(socket);
        this.firstEvaluation(socket);
        this.endOfFirstRound(socket);
        this.startOfRound2(socket);
        this.secondBidding(socket);
        this.secondEval(socket);
        this.secondEval2(socket);
        this.endOfRound2(socket);
        this.startOfRound3(socket);

        this.sendLeave(1);
        this.assertLeft(2, 0);
        this.assertLeft(3, 0);
        this.assertLeft(4, 0);

        this.sendLeave(2);
        this.assertLeft(3, 1);
        this.assertLeft(4, 1);

        this.round3(socket);
        this.endOfRound3(socket);
        this.startOfRound4(socket);

        this.sendLeave(3);
        this.sendLeave(4);

    }
}
