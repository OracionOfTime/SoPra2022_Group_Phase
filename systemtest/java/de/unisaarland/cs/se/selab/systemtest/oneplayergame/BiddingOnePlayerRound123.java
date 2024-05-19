package de.unisaarland.cs.se.selab.systemtest.oneplayergame;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class BiddingOnePlayerRound123 extends CompleteGameOnePlayer {

    public BiddingOnePlayerRound123() {
        super(BiddingOnePlayerRound123.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(BiddingOnePlayerRound123.class, "configuration.json");
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
    public void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        this.sendRegister(1, "Player1");
        this.assertConfig(1, config);

        round1();
        round2();
        round3();

        this.sendLeave(1);
    }
}
