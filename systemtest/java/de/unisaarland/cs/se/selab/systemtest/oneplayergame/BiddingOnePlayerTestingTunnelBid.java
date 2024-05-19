package de.unisaarland.cs.se.selab.systemtest.oneplayergame;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class BiddingOnePlayerTestingTunnelBid extends CompleteGameOnePlayer {

    public BiddingOnePlayerTestingTunnelBid() {
        super(BiddingOnePlayerTestingTunnelBid.class, false);
    }

    @Override
    public void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        this.sendRegister(1, "Player1");
        this.assertConfig(1, config);

        round1();
        round2();

        this.sendLeave(1);
    }

    @Override
    public void round2() throws TimeoutException {
        //First Round Completed

        sendPlaceBid(1, BidType.IMPS, 1);
        assertBidPlaced(1, BidType.IMPS, 0, 1);
        assertActNow(1);

        sendPlaceBid(1, BidType.TUNNEL, 2);
        assertBidPlaced(1, BidType.TUNNEL, 0, 2);
        assertActNow(1);

        sendPlaceBid(1, BidType.TRAP, 3);
        assertBidPlaced(1, BidType.TRAP, 0, 3);

        //Evaluation

        //Tunnel
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 0);
        assertActionFailed(1);
        assertActNow(1);

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x == 0 && y == 0) {
                    sendDigTunnel(1, 0, 0);
                    assertActionFailed(1);
                    assertActNow(1);
                } else if (!((x == 0 && y == 1) || (x == 1 && y == 0))) {
                    sendDigTunnel(1, x, y);
                    assertActionFailed(1);
                    assertActNow(1);
                }
            }
        }

        sendDigTunnel(1, 0, 1);
        assertImpsChanged(1, -1, 0);
        assertTunnelDug(1, 0, 0, 1);

        assertActNow(1);
        sendDigTunnel(1, 1, 0);
        assertImpsChanged(1, -1, 0);
        assertTunnelDug(1, 0, 1, 0);

        //Imps
        assertFoodChanged(1, -1, 0);
        assertImpsChanged(1, 1, 0);

        //Traps
        assertGoldChanged(1, -1, 0);
        assertTrapAcquired(1, 0, 26);

        assertBidRetrieved(1, BidType.GOLD, 0);
        assertBidRetrieved(1, BidType.NICENESS, 0);
        assertBidRetrieved(1, BidType.IMPS, 0);

        assertImpsChanged(1, 2, 0);

        //Adventure arrived
        assertEvent(1);

        assertNextRound(1, 3);

        skipDrawing();

        assertBiddingStarted(1);
        assertActNow(1);
    }
}
