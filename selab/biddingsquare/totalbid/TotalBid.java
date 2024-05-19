package de.unisaarland.cs.se.selab.biddingsquare.totalbid;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.io.IOException;
import java.util.Queue;

public abstract class TotalBid {

    protected BidType bidType;
    protected BroadcastEvents serverConnection;

    public TotalBid(final BidType bidType, final BroadcastEvents serverConnection) {
        this.bidType = bidType;
        this.serverConnection = serverConnection;
    }

    public BidType getBidType() {
        return bidType;
    }

    public void setBidType(final BidType bidType) {
        this.bidType = bidType;
    }

    public BroadcastEvents getServerConnection() {
        return serverConnection;
    }

    public void setServerConnection(final BroadcastEvents serverConnection) {
        this.serverConnection = serverConnection;
    }

    public abstract void evaluateAllBids(EvaluationPhase ep, Queue<SingleBid> singleBids)
            throws IOException;
}
