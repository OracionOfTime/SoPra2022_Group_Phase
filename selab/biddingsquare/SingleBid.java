package de.unisaarland.cs.se.selab.biddingsquare;

import de.unisaarland.cs.se.selab.comm.BidType;

public class SingleBid {

    private BidType bid;
    private int bidNum;
    private int commID;

    public SingleBid(final BidType bid, final int bidNum, final int commID) {
        this.bid = bid;
        this.bidNum = bidNum;
        this.commID = commID;
    }

    public BidType getBid() {
        return bid;
    }

    public void setBid(final BidType bid) {
        this.bid = bid;
    }

    public int getBidNum() {
        return bidNum;
    }

    public void setBidNum(final int bidNum) {
        this.bidNum = bidNum;
    }

    public int getCommID() {
        return commID;
    }

    public void setCommID(final int commID) {
        this.commID = commID;
    }
}
