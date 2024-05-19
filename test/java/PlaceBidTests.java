/*import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.actions.PlaceBid;
import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.game.Game;
import java.util.Optional;
import org.junit.jupiter.api.Test;


public class PlaceBidTests {

    private unittests.UnitTestFactory unitTestFactory = new unittests.UnitTestFactory();


    @Test
    public void placeFirstBids() {
        Game game = unitTestFactory.createDefaultGame();
        PlaceBid placeBid = new PlaceBid(3, BidType.GOLD, 1);
        boolean res = placeBid.exec(game.getBuildingPhase());
        assertTrue(res);
        placeBid = new PlaceBid(1, BidType.FOOD, 1);
        res = placeBid.exec(game.getBuildingPhase());
        assertTrue(res);
        placeBid = new PlaceBid(2, BidType.IMPS, 1);
        res = placeBid.exec(game.getBuildingPhase());
        assertTrue(res);
    }

    @Test
    public void testSameBidTwice() {
        Game game = unitTestFactory.createDefaultGame();
        PlaceBid placeBid = new PlaceBid(0, BidType.TRAP, 2);
        boolean res = placeBid.exec(game.getBuildingPhase());
        assertTrue(res);
        game.getDungeonLordMap().get(0).getBids()
                .set(1, Optional.of(new SingleBid(BidType.TRAP, 2, 0)));
        placeBid = new PlaceBid(0, BidType.FOOD, 1);
        res = placeBid.exec(game.getBuildingPhase());
        assertTrue(res);
        game.getDungeonLordMap().get(0).getBids()
                .set(0, Optional.of(new SingleBid(BidType.FOOD, 1, 0)));
        placeBid = new PlaceBid(0, BidType.TRAP, 3);
        res = placeBid.exec(game.getBuildingPhase());
        assertFalse(res);
    }

    @Test
    public void testPlaceLockedBid() {
        Game game = unitTestFactory.createDefaultGame();
        game.getDungeonLordMap().get(2).getLockedBids().set(0, Optional.of(BidType.IMPS));
        PlaceBid placeBid = new PlaceBid(2, BidType.IMPS, 1);
        boolean res = placeBid.exec(game.getBuildingPhase());
        assertFalse(res);
    }


}
*/

