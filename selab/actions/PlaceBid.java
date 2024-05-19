package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.phases.BuildingPhase;
import java.util.List;
import java.util.Optional;

public class PlaceBid extends Action {

    BidType bidType;
    int number;

    public PlaceBid(final int commID, final BidType bidType, final int number) {
        super(commID);
        this.bidType = bidType;
        this.number = number;

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    public BidType getBid() {
        return bidType;
    }

    public void setBid(final BidType bidType) {
        this.bidType = bidType;
    }

    @Override
    public boolean exec(final BuildingPhase bp) {

        if (this.number < 1 || this.number > 3) {
            bp.getSc().sendActionFailed(getCommID(), "wrong bid number");
            bp.getSc().sendActNow(getCommID());
            return false;
        }

        final DungeonLord dl = bp.getDungeonLordMap().get(this.getCommID());

        if (dl.getLockedBids().get(0).isPresent() && bidType == dl.getLockedBids().get(0).get()
                || dl.getLockedBids().get(1).isPresent() && bidType == dl.getLockedBids()
                .get(1).get()) {
            bp.getSc().sendActionFailed(getCommID(), "the bid is locked");
            bp.getSc().sendActNow(getCommID());
            return false;
        }

        final List<Optional<SingleBid>> bids = dl.getBids();

        for (int i = 0; i < 3; i++) {
            if (bids.get(i).isPresent() && bids.get(i).get().getBid() == this.bidType) {
                bp.getSc().sendActionFailed(getCommID(),
                        "bid on this bid type has already been placed");
                bp.getSc().sendActNow(getCommID());
                return false;
            }
            if (bids.get(i).isPresent() && bids.get(i).get().getBidNum() == this.number) {
                bp.getSc().sendActionFailed(getCommID(),
                        "bid with this number has already been placed");
                bp.getSc().sendActNow(getCommID());
                return false;
            }
        }

        final Optional<SingleBid> bidToAdd = Optional.of(
                new SingleBid(this.bidType, this.number, this.getCommID()));
        bids.set(number - 1, bidToAdd);
        dl.setBids(bids);
        bp.getSc().broadcastBidPlaced(bp.getDungeonLordSortedList(), this.bidType, dl.getPlayerID(),
                this.number);
        return true;
    }
}
