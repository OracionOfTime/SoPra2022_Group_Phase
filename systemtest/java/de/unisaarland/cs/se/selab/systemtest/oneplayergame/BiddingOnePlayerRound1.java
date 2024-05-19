package de.unisaarland.cs.se.selab.systemtest.oneplayergame;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class BiddingOnePlayerRound1 extends CompleteGameOnePlayer {

    public BiddingOnePlayerRound1() {
        super(BiddingOnePlayerRound1.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(BiddingOnePlayerRound1.class, "configuration.json");
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

        this.round1();

        this.sendLeave(1);

    }


}
