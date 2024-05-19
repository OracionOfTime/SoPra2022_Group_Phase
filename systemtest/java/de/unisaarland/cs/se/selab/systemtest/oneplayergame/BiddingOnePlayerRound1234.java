package de.unisaarland.cs.se.selab.systemtest.oneplayergame;

import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class BiddingOnePlayerRound1234 extends CompleteGameOnePlayer {

    public BiddingOnePlayerRound1234() {
        super(BiddingOnePlayerRound1234.class, false);
    }

    @Override
    public void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        this.sendRegister(1, "Player1");
        this.assertConfig(1, config);

        round1();
        round2();
        round3();
        round4();

        this.sendLeave(1);
    }
}
