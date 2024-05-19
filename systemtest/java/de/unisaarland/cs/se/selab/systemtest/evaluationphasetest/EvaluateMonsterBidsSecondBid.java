package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class EvaluateMonsterBidsSecondBid extends EvaluateMonsterBids {

    public EvaluateMonsterBidsSecondBid() {
        super(EvaluateMonsterBidsSecondBid.class, false);
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

        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
        this.sendLeave(4);

    }
}
