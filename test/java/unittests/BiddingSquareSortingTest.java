package unittests;

import de.unisaarland.cs.se.selab.biddingsquare.BiddingSquare;
import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import org.junit.jupiter.api.Test;

class BiddingSquareSortingTest {

    @Test
    void testingSortBidType() {
        final UnitTestFactory fac = new UnitTestFactory();

        final List<DungeonLord> idList = new ArrayList<>();
        final DungeonLord dl0 = fac.createDungeonLord(0, 0);
        final DungeonLord dl1 = fac.createDungeonLord(1, 1);

        idList.add(dl0);
        idList.add(dl1);

        final List<Optional<SingleBid>> bidsdl0 = new LinkedList<>();
        final List<Optional<SingleBid>> bidsdl1 = new LinkedList<>();

        bidsdl0.add(Optional.of(new SingleBid(BidType.FOOD, 1, 0)));
        bidsdl0.add(Optional.of(new SingleBid(BidType.IMPS, 2, 0)));
        bidsdl0.add(Optional.of(new SingleBid(BidType.GOLD, 3, 0)));

        bidsdl1.add(Optional.of(new SingleBid(BidType.FOOD, 1, 0)));
        bidsdl1.add(Optional.of(new SingleBid(BidType.IMPS, 2, 0)));
        bidsdl1.add(Optional.of(new SingleBid(BidType.GOLD, 3, 0)));

        idList.get(0).setBids(bidsdl0);
        idList.get(1).setBids(bidsdl1);

        final Map<Integer, DungeonLord> dlmap = new HashMap<>();
        dlmap.put(0, dl0);
        dlmap.put(1, dl1);

        final BiddingSquare bs = new BiddingSquare(dlmap, null, null, null,
                0, null);

        bs.initTotalBids();

        bs.setMappedBids(bs.initMap());
        final EvaluationPhase ep = new EvaluationPhase(null, idList, null,
                false, false, bs, null, null, false);

        bs.sortBidType(ep);

        final Map<BidType, Queue<SingleBid>> mappedBids = bs.getMappedBids();

        assert (mappedBids.get(BidType.FOOD).size() == 2);
        assert (mappedBids.get(BidType.IMPS).size() == 2);
        assert (mappedBids.get(BidType.GOLD).size() == 2);
    }
}
