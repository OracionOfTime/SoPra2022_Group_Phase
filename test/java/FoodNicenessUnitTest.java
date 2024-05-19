/*import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unisaarland.cs.se.selab.biddingsquare.BiddingSquare;
import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.FoodBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.GoldBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.ImpsBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.NicenessBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.game.Game;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodNicenessUnitTest {

    @BeforeEach
    void setUp() {
        // empty
    }

    void setBidsInDL(DungeonLord dl, BidType[] types) {
        List<Optional<SingleBid>> singleBids = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final SingleBid sb = new SingleBid(types[i], (i + 1) % 3, dl.getCommID());
            singleBids.add(Optional.of(sb));
        }
        dl.setBids(singleBids);
    }

    /*
    @Test
    void testFoodBid() {

        unittests.UnitTestFactory utf = new unittests.UnitTestFactory();
        Game game = utf.createDefaultGame();
        EvaluationPhase ep = game.getEvaluationPhase();
        final SingleBid[] foodSingleBidsArray = new SingleBid[4];
        final SingleBid[] nicenessSingleBidsArray = new SingleBid[4];
        final SingleBid[] goldSingleBidsArray = new SingleBid[3];

        foodSingleBidsArray[0] = new SingleBid(BidType.FOOD, 1, 0);
        nicenessSingleBidsArray[0] = new SingleBid(BidType.NICENESS, 2, 0);
        SingleBid sb03 = new SingleBid(BidType.IMPS, 3, 0);

        // put the single bids in the dls
        List<DungeonLord> dl = ep.getDungeonLordSortedList();
        BidType[] typ = {BidType.FOOD, BidType.NICENESS, BidType.IMPS};
        setBidsInDL(dl.get(0), typ);

        foodSingleBidsArray[1] = new SingleBid(BidType.FOOD, 1, 1);
        goldSingleBidsArray[0] = new SingleBid(BidType.GOLD, 2, 1);
        nicenessSingleBidsArray[2] = new SingleBid(BidType.NICENESS, 3, 1);

        BidType[] typ1 = {BidType.FOOD, BidType.GOLD, BidType.NICENESS};
        setBidsInDL(dl.get(1), typ1);

        foodSingleBidsArray[2] = new SingleBid(BidType.FOOD, 1, 2);
        goldSingleBidsArray[1] = new SingleBid(BidType.GOLD, 2, 2);
        nicenessSingleBidsArray[3] = new SingleBid(BidType.NICENESS, 3, 2);

        BidType[] typ2 = {BidType.FOOD, BidType.GOLD, BidType.NICENESS};
        setBidsInDL(dl.get(1), typ2);

        foodSingleBidsArray[3] = new SingleBid(BidType.FOOD, 1, 3);
        nicenessSingleBidsArray[1] = new SingleBid(BidType.NICENESS, 2, 3);
        goldSingleBidsArray[2] = new SingleBid(BidType.GOLD, 3, 3);

        BidType[] typ3 = {BidType.FOOD, BidType.NICENESS, BidType.GOLD};
        setBidsInDL(dl.get(2), typ3);

        Queue<SingleBid> foodBids = new LinkedList<>();
        Queue<SingleBid> nicenessBids = new LinkedList<>();
        Queue<SingleBid> goldBids = new LinkedList<>();

        // fill up the queues
        foodBids.addAll(Arrays.asList(foodSingleBidsArray));
        nicenessBids.addAll(Arrays.asList(nicenessSingleBidsArray));
        goldBids.addAll(Arrays.asList(goldSingleBidsArray));
        Queue<SingleBid> impsBids = new LinkedList<>();
        impsBids.add(sb03);

        Map<BidType, Queue<SingleBid>> mappedBids = new HashMap<>();
        mappedBids.put(BidType.FOOD, foodBids);
        mappedBids.put(BidType.NICENESS, nicenessBids);
        mappedBids.put(BidType.GOLD, goldBids);
        mappedBids.put(BidType.IMPS, impsBids);

        BiddingSquare bs = ep.getBiddingSquare();
        bs.setMappedBids(mappedBids);
        FoodBid fb = new FoodBid(ep.getSc());
        NicenessBid nb = new NicenessBid(ep.getSc());
        GoldBid gb = new GoldBid(ep.getSc());
        ImpsBid ib = new ImpsBid(ep.getSc());

        //execute our bids
        fb.evaluateAllBids(ep,foodBids);
        nb.evaluateAllBids(ep, nicenessBids);
        gb.evaluateAllBids(ep, goldBids);
        ib.evaluateAllBids(ep, impsBids);

        //food:
        assertEquals(4, dl.get(0).getFood());
        assertEquals(6, dl.get(1).getFood());
        assertEquals(6, dl.get(2).getFood());
        assertEquals(3, dl.get(3).getFood());
        //evilness
        assertEquals(4, dl.get(0).getEvilness());
        assertEquals(3, dl.get(1).getEvilness());
        assertEquals(7, dl.get(2).getEvilness());
        assertEquals(3, dl.get(3).getEvilness());
        //Gold
        assertEquals(2, dl.get(0).getGold());
        assertEquals(2, dl.get(1).getGold());
        assertEquals(4, dl.get(2).getGold());
        assertEquals(3, dl.get(3).getGold());
        //Imps(p1 has 2 miningGold imps and p3,p4 have 3 )
        assertEquals(4, dl.get(0).getNumberOfImps());
        assertEquals(3, dl.get(1).getNumberOfImps());
        assertEquals(3, dl.get(2).getNumberOfImps());
        assertEquals(3, dl.get(3).getNumberOfImps());


    }



}

     */