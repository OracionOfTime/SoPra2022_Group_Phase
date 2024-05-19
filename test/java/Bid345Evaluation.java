/*
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import de.unisaarland.cs.se.selab.actions.BuildRoom;
import de.unisaarland.cs.se.selab.actions.EndTurn;
import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.FoodBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.GoldBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.ImpsBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.MonsterBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.RoomBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.TrapBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.dungeon.Trap;
import de.unisaarland.cs.se.selab.dungeon.Tunnel;
import de.unisaarland.cs.se.selab.game.Game;
import de.unisaarland.cs.se.selab.game.GameFactory;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class Bid345Evaluation {

    @Test
    public void testBid345() throws TimeoutException {
        unittests.UnitTestFactory utf=new unittests.UnitTestFactory();
        Game g =utf.createDefaultGame();
        DungeonLord dl0=g.getEvaluationPhase().getDungeonLordSortedList().get(0);
        DungeonLord dl1=g.getEvaluationPhase().getDungeonLordSortedList().get(1);
        DungeonLord dl2=g.getEvaluationPhase().getDungeonLordSortedList().get(2);
        DungeonLord dl3=g.getEvaluationPhase().getDungeonLordSortedList().get(3);
        roomBidTest(g,dl0,dl1,dl2,dl3);




        //call goldbid test, impbid test or any other test

    }



    private Map<BidType, Queue<SingleBid>> mapBids(DungeonLord dl0, DungeonLord dl1,
            DungeonLord dl2, DungeonLord dl3, Game g){

        Map<BidType, Queue<SingleBid>> mappedBids=new HashMap<>();
        for(int i=0;i<3; i++) {
            for (final DungeonLord dungeonLord :
            g.getEvaluationPhase().getDungeonLordSortedList()) {
                if (dungeonLord.getBids().get(i).isPresent()) {
                    final SingleBid tempBid = dungeonLord.getBids().get(i).get();
                    Queue<SingleBid> tempQueue = mappedBids.get(tempBid.getBid());
                    if (tempQueue==null){
                        tempQueue=new LinkedList<>();
                    }
                    tempQueue.add(tempBid);
                    mappedBids.put(tempBid.getBid(), tempQueue);
                }
            }
        }
        return mappedBids;

    }

    @Test
    private void goldBidTest(Game g, DungeonLord dl0, DungeonLord dl1,
            DungeonLord dl2, DungeonLord dl3){
        createAndAssignBids(dl0,dl1,dl2,dl3);
        BroadcastEvents sc =g.getEvaluationPhase().getSc();
        GoldBid gb=new GoldBid(sc);
        createAndAssignBids(dl0,dl1,dl2,dl3);
        Map<BidType, Queue<SingleBid>> mappedBids=mapBids(dl0,dl1,dl2,dl3,g);
        gb.evaluateAllBids(g.getEvaluationPhase(),mappedBids.get(BidType.GOLD));
        assertEquals(1,dl0.getNumberOfImps());
        assertEquals(3,dl0.getGold());
        assertEquals(0,dl2.getNumberOfImps());
        assertEquals(0,dl3.getNumberOfImps());
        assertEquals(3,dl1.getNumberOfImps());

    }

    private void impBidTest(Game g, DungeonLord dl0, DungeonLord dl1,
            DungeonLord dl2, DungeonLord dl3) {
        BroadcastEvents sc = g.getEvaluationPhase().getSc();
        createAndAssignBids(dl0,dl1,dl2,dl3);
        ImpsBid ib = new ImpsBid(sc);
        createAndAssignBids(dl0, dl1, dl2, dl3);
        Map<BidType, Queue<SingleBid>> mappedBids=mapBids(dl0,dl1,dl2,dl3,g);
        ib.evaluateAllBids(g.getEvaluationPhase(),mappedBids.get(BidType.IMPS));
        assertEquals(2,dl0.getFood());
        assertEquals(4,dl0.getNumberOfImps());

        assertEquals(1,dl1.getFood());
        assertEquals(5,dl1.getNumberOfImps());
        assertEquals(2,dl3.getGold());
        assertEquals(2,dl3.getFood());
        assertEquals(5,dl3.getNumberOfImps());
    }

    private void trapBidTest(Game g, DungeonLord dl0, DungeonLord dl1,
            DungeonLord dl2, DungeonLord dl3) {
        BroadcastEvents sc = g.getEvaluationPhase().getSc();
        createAndAssignBids(dl0, dl1, dl2, dl3);
        TrapBid tb = new TrapBid(sc);
        createAndAssignBids(dl0, dl1, dl2, dl3);
        Map<BidType, Queue<SingleBid>> mappedBids = mapBids(dl0, dl1, dl2, dl3, g);
        tb.evaluateAllBids(g.getEvaluationPhase(),mappedBids.get(BidType.TRAP));
        assertEquals(2,dl1.getGold());
        assertEquals(1,dl1.getDungeon().getTraps().size());
        assertEquals(1,dl2.getDungeon().getTraps().size());
        assertEquals(3, dl2.getGold());
        assertEquals(2,dl0.getDungeon().getTraps().size());
        assertEquals(1,dl0.getGold());
    }

    private void roomBidTest(Game g, DungeonLord dl0, DungeonLord dl1,
            DungeonLord dl2, DungeonLord dl3) throws TimeoutException {
        BroadcastEvents sc = g.getEvaluationPhase().getSc();
        roomBidsAssign(dl0, dl1, dl2, dl3);
        RoomBid rb = new RoomBid(sc);
        createAndAssignBids(dl0, dl1, dl2, dl3);
        Map<BidType, Queue<SingleBid>> mappedBids = mapBids(dl0, dl1, dl2, dl3, g);

        List<Room> roomList=g.getEvaluationPhase().getBiddingSquare().getRooms();
        initializeDungeon(dl0,dl3,dl2);
        Room room1= roomList.get(0);
        Room room2=roomList.get(1);
        g.getEvaluationPhase().setSc(sc);

        Mockito.doNothing().when(sc).sendActionFailed(3,"3 bids have already been placed");
        Mockito.doNothing().when(sc).sendActionFailed(2,"3 bids have already been placed");
        Mockito.doNothing().when(sc).sendActionFailed(1,"3 bids have already been placed");
        Mockito.doNothing().when(sc).sendActionFailed(0,"3 bids have already been placed");

        Mockito.doNothing().when (sc).broadcastGoldChanged(
        g.getEvaluationPhase().getDungeonLordSortedList(),
                -1,0);
        Mockito.doNothing().when (sc).
        broadcastGoldChanged(g.getEvaluationPhase().getDungeonLordSortedList(),
                -1,1);
        Mockito.doNothing().when (sc).
        broadcastGoldChanged(g.getEvaluationPhase().getDungeonLordSortedList(),
                -1,2);
        Mockito.doNothing().when (sc).
        broadcastGoldChanged(g.getEvaluationPhase().getDungeonLordSortedList(),
                -1,3);

        Mockito.doNothing().when(sc).sendActionFailed(1,"action failed");
        Mockito.doNothing().when(sc).sendActionFailed(3,"action failed");


        BuildRoom roomBuild0=new BuildRoom(0,0,0,2);
        BuildRoom roombuild1=new BuildRoom(1,0,0,3);
        BuildRoom roombuild2=new BuildRoom(2,0,0,3);

        EndTurn turnEnded=new EndTurn(1);

        Mockito.when(sc.nextAction()).thenReturn(roomBuild0,roombuild1,turnEnded,roombuild2);

        rb.evaluateAllBids(g.getEvaluationPhase(), mappedBids.get(BidType.ROOM));









    }

    private void initializeDungeon(DungeonLord dl0, DungeonLord dl3, DungeonLord dl2){
        Dungeon d0=dl0.getDungeon();
        List<List<Optional<Tile>>> t0=d0.getTiles();
        for (int i=1;i<4;i++) {
            t0.get(0).set(i, Optional.of(new Tunnel(0, 0, false)));
        }

        List<List<Optional<Tile>>> t1=dl3.getDungeon().getTiles();
        if (t1.get(0).get(0).isPresent()){
            Optional<Tile> firstTile=t1.get(0).get(0);
            firstTile.get().setConquered(true);
        }

        List<List<Optional<Tile>>> t2=dl2.getDungeon().getTiles();
        for (int i=1;i<4;i++) {
            t2.get(i).set(0, Optional.of(new Tunnel(0, 0, false)));
        }
     }


    private void roomBidsAssign(DungeonLord dl0, DungeonLord dl1,
            DungeonLord dl2, DungeonLord dl3){
        createListBids(dl0, BidType.ROOM, BidType.IMPS, BidType.TRAP);
        createListBids(dl1, BidType.TRAP, BidType.GOLD, BidType.ROOM);
        createListBids(dl2, BidType.ROOM, BidType.TRAP, BidType.FOOD);
        createListBids(dl3, BidType.NICENESS, BidType.ROOM, BidType.GOLD);

    }

    private void createAndAssignBids(DungeonLord dl0, DungeonLord dl1,
            DungeonLord dl2, DungeonLord dl3){
        createListBids(dl0, BidType.GOLD, BidType.IMPS, BidType.TRAP);
        createListBids(dl1, BidType.TRAP, BidType.IMPS, BidType.GOLD);
        createListBids(dl2, BidType.GOLD, BidType.TRAP, BidType.FOOD);
        createListBids(dl3, BidType.NICENESS, BidType.GOLD, BidType.IMPS);

    }

    private void createListBids(DungeonLord dl, BidType bt1, BidType bt2, BidType bt3){
        SingleBid sb1=new SingleBid(bt1,1,dl.getCommID());
        SingleBid sb2=new SingleBid(bt2,2,dl.getCommID());
        SingleBid sb3=new SingleBid(bt3,3,dl.getCommID());
        List<Optional<SingleBid>> bids = new ArrayList<>(3);
        bids.add(0,Optional.of(sb1));
        bids.add(1,Optional.of(sb2));
        bids.add(2,Optional.of(sb3));
        dl.setBids(bids);
    }



}
*/
