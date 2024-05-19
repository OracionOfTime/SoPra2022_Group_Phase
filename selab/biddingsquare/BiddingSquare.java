package de.unisaarland.cs.se.selab.biddingsquare;

import de.unisaarland.cs.se.selab.biddingsquare.totalbid.FoodBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.GoldBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.ImpsBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.MonsterBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.NicenessBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.RoomBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.TotalBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.TrapBid;
import de.unisaarland.cs.se.selab.biddingsquare.totalbid.TunnelBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.dungeon.Trap;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BiddingSquare {

    private Map<Integer, DungeonLord> dungeonLords;
    private List<Monster> monsters;
    private Queue<Trap> traps;
    private List<Room> rooms;
    private int priority;
    private TotalBid[] totalBids;
    private BroadcastEvents serverConnection;
    private Map<BidType, Queue<SingleBid>> mappedBids;

    public BiddingSquare(final Map<Integer, DungeonLord> dungeonLords,
            final List<Monster> monsters,
            final Queue<Trap> traps, final List<Room> rooms, final int priority,
            final BroadcastEvents serverConnection) {
        this.dungeonLords = dungeonLords;
        this.monsters = monsters;
        this.traps = traps;
        this.rooms = rooms;
        this.priority = priority;
        this.serverConnection = serverConnection;
    }

    public Map<Integer, DungeonLord> getDungeonLords() {
        return dungeonLords;
    }

    public void setDungeonLords(final Map<Integer, DungeonLord> dungeonLords) {
        this.dungeonLords = dungeonLords;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(final List<Monster> monsters) {
        this.monsters = monsters;
    }

    public Queue<Trap> getTraps() {
        return traps;
    }

    public void setTraps(final Queue<Trap> traps) {
        this.traps = traps;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(final List<Room> rooms) {
        this.rooms = rooms;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    public BroadcastEvents getServerConnection() {
        return serverConnection;
    }

    public void setServerConnection(final BroadcastEvents serverConnection) {
        this.serverConnection = serverConnection;
    }

    public Map<BidType, Queue<SingleBid>> getMappedBids() {
        return mappedBids;
    }

    public void setMappedBids(final Map<BidType, Queue<SingleBid>> mappedBids) {
        this.mappedBids = mappedBids;
    }


    public void sortBidType(final EvaluationPhase ep) {
        final List<DungeonLord> idList = ep.getDungeonLordSortedList();
        final List<DungeonLord> sortedByPriority = new ArrayList<>(idList.size());

        int temp = 0;
        for (final DungeonLord currDL : idList) {

            final DungeonLord dlToAdd = idList.get(
                    (currDL.getPlayerID() + priority) % idList.size());
            sortedByPriority.add(dlToAdd);
            if (temp > idList.size()) {
                break;
            }
            temp++;
        }

        for (int i = 0; i < 3; i++) {
            for (final DungeonLord dungeonLord : sortedByPriority) {
                if (dungeonLord.getBids().get(i).isPresent()) {
                    final SingleBid tempBid = dungeonLord.getBids().get(i).get();
                    final Queue<SingleBid> tempQueue = mappedBids.get(tempBid.getBid());
                    tempQueue.add(tempBid);
                    mappedBids.put(tempBid.getBid(), tempQueue);
                }
            }
        }

    }

    public void initTotalBids() {
        this.totalBids = new TotalBid[8];
        totalBids[0] = new FoodBid(serverConnection);
        totalBids[1] = new NicenessBid(serverConnection);
        totalBids[2] = new TunnelBid(serverConnection);
        totalBids[3] = new GoldBid(serverConnection);
        totalBids[4] = new ImpsBid(serverConnection);
        totalBids[5] = new TrapBid(serverConnection);
        totalBids[6] = new MonsterBid(serverConnection);
        totalBids[7] = new RoomBid(serverConnection);
    }

    public Map<BidType, Queue<SingleBid>> initMap() {
        final Map<BidType, Queue<SingleBid>> map = new EnumMap<>(BidType.class);
        map.put(BidType.FOOD, new LinkedList<>());
        map.put(BidType.NICENESS, new LinkedList<>());
        map.put(BidType.TUNNEL, new LinkedList<>());
        map.put(BidType.GOLD, new LinkedList<>());
        map.put(BidType.IMPS, new LinkedList<>());
        map.put(BidType.TRAP, new LinkedList<>());
        map.put(BidType.MONSTER, new LinkedList<>());
        map.put(BidType.ROOM, new LinkedList<>());

        return map;
    }

    public void evaluateAllBids(final EvaluationPhase evaluationPhase) throws IOException {
        initTotalBids();
        this.mappedBids = initMap();
        sortBidType(evaluationPhase);
        totalBids[0].evaluateAllBids(evaluationPhase, mappedBids.get(BidType.FOOD));
        totalBids[1].evaluateAllBids(evaluationPhase, mappedBids.get(BidType.NICENESS));
        totalBids[2].evaluateAllBids(evaluationPhase, mappedBids.get(BidType.TUNNEL));
        totalBids[3].evaluateAllBids(evaluationPhase, mappedBids.get(BidType.GOLD));
        totalBids[4].evaluateAllBids(evaluationPhase, mappedBids.get(BidType.IMPS));
        totalBids[5].evaluateAllBids(evaluationPhase, mappedBids.get(BidType.TRAP));
        totalBids[6].evaluateAllBids(evaluationPhase, mappedBids.get(BidType.MONSTER));
        totalBids[7].evaluateAllBids(evaluationPhase, mappedBids.get(BidType.ROOM));
    }
}
