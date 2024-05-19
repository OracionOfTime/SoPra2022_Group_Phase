package de.unisaarland.cs.se.selab.phases;


import de.unisaarland.cs.se.selab.biddingsquare.BiddingSquare;
import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.Adventurer;
import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Imp;
import de.unisaarland.cs.se.selab.dungeon.Imp.ActionType;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.dungeon.Trap;
import de.unisaarland.cs.se.selab.game.Game;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class EvaluationPhase extends Phase {

    private BiddingSquare biddingsquare;
    private boolean expectingMonster;
    private boolean expectingRoom;
    private boolean expectingTunnel;

    private boolean digTunnel5Imps;
    private Adventurer[] poolAdventurers;
    private Queue<Trap> trapPool;

    public EvaluationPhase(final BroadcastEvents sc,
            final List<DungeonLord> dungeonLordArray,
            final Map<Integer, DungeonLord> dungeonLordMap, final boolean expectingMonster,
            final boolean expectingRoom, final BiddingSquare biddingsquare,
            final Adventurer[] poolAdventurers,
            final Queue<Trap> trapPool, final boolean expectingTunnel) {
        super(sc, dungeonLordArray, dungeonLordMap);
        this.expectingMonster = expectingMonster;
        this.expectingRoom = expectingRoom;
        this.expectingTunnel = expectingTunnel;
        this.biddingsquare = biddingsquare;
        this.poolAdventurers = poolAdventurers;
        this.trapPool = trapPool;
        this.digTunnel5Imps = false;
    }

    public boolean isDigTunnel5Imps() {
        return digTunnel5Imps;
    }

    public void setDigTunnel5Imps(final boolean digTunnel5Imps) {
        this.digTunnel5Imps = digTunnel5Imps;
    }

    public boolean isExpectingMonster() {
        return expectingMonster;
    }

    public void setExpectingMonster(final boolean expectingMonster) {
        this.expectingMonster = expectingMonster;
    }

    public boolean isExpectingTunnel() {
        return expectingTunnel;
    }

    public void setExpectingTunnel(final boolean expectingTunnel) {
        this.expectingTunnel = expectingTunnel;
    }

    public boolean isExpectingRoom() {
        return expectingRoom;
    }

    public void setExpectingRoom(final boolean expectingRoom) {
        this.expectingRoom = expectingRoom;
    }

    public BiddingSquare getBiddingSquare() {
        return biddingsquare;
    }

    //public Adventurer[] getPoolAdventurers() {
    // return poolAdventurers;
    //}

    public void setPoolAdventurers(final Adventurer[] poolAdventurers) {
        this.poolAdventurers = poolAdventurers;
    }

    public Queue<Trap> getTrapPool() {
        return trapPool;
    }

    public void setTrapPool(final Queue<Trap> trapPool) {
        this.trapPool = trapPool;
    }

    /**
     * evaluate all the bids in the bidding square. It just calls the evaluateAllBids function of
     * the bidding square
     *
     * @param bidingSq biddingSquare object received from Building Phase
     */
    private void evaluateAllBids(final BiddingSquare bidingSq) throws IOException {
        bidingSq.evaluateAllBids(this);
    }

    /**
     * locks the 2nd and 3rd bid of all the dungeon lords
     *
     * @param singleBid singlebid object which is used to lock the bids
     */
    private void checkAndLockBids(final SingleBid singleBid) {
        // only the bids 2 and 3 will be locked
        if (singleBid.getBidNum() == 2 || singleBid.getBidNum() == 3) {

            if (getDungeonLordMap().containsKey(singleBid.getCommID())) {
                dungeonLordMap.get(singleBid.getCommID()).getLockedBids().set(
                        singleBid.getBidNum() - 2, Optional.of(singleBid.getBid()));
            }
        }
    }

    /**
     * executes all the methods of the evaluation phase in order
     *
     * @param bs   biddingSquare object received from Building Phase
     * @param game Game object to check for number of rounds
     */
    public void execute(final BiddingSquare bs, final Game game) throws IOException {
        this.biddingsquare = bs;

        // 1. evaluate all bids
        evaluateAllBids(this.biddingsquare);

        retrieve2n3Bid(game.getRounds());

        // 2. locking 2 n 3 bid of the dungeon lords
        this.biddingsquare.getMappedBids().forEach(
                (bidType, singleBidsQueue) -> {
                    for (final SingleBid singleBid : singleBidsQueue) {
                        checkAndLockBids(singleBid);
                    }
                }
        );

        returnImpsAndChangeGold();

        checkAndActivateRoom();
        // if rounds == 4 we need to assign adventures to the dungeon lords
        if (game.getRounds() < 4) {
            assignAdventurers();
        }

        for (final DungeonLord dungeonLord : getDungeonLordSortedList()) {
            dungeonLord.getBids().set(0, Optional.empty());
            dungeonLord.getBids().set(1, Optional.empty());
            dungeonLord.getBids().set(2, Optional.empty());
        }
    }

    private void returnImpsAndChangeGold() {
        int goldMined;
        int impressed; // impsFreed
        for (final DungeonLord dungeonLord : getDungeonLordSortedList()) {
            goldMined = 0;
            impressed = 0;

            for (final Imp imp : dungeonLord.getImps()) {
                if (imp.getAction() == ActionType.MINEGOLD) {
                    goldMined++;
                    impressed++;
                    imp.setAction(ActionType.RESTING);
                } else if (imp.getAction() == ActionType.SUPERVISE) {
                    impressed++;
                    imp.setAction(ActionType.RESTING);
                } else if (imp.getAction() == ActionType.MINETUNNEL) {
                    impressed++;
                    imp.setAction(ActionType.RESTING);
                }
            }

            if (impressed != 0) {
                getSc().broadcastImpsChanged(getDungeonLordSortedList(), impressed,
                        dungeonLord.getPlayerID());
                dungeonLord.setNUmberOfImps(dungeonLord.getNumberOfImps() + impressed);
                if (goldMined != 0) {
                    getSc().broadcastGoldChanged(getDungeonLordSortedList(), goldMined,
                            dungeonLord.getPlayerID());
                    dungeonLord.setGold(dungeonLord.getGold() + goldMined);
                }
            }
        }
    }


    private void retrieveDlFirstBid(final DungeonLord dungeonLord) {

        for (final Optional<SingleBid> singleBid : dungeonLord.getBids()) {
            if (singleBid.get().getBidNum() == 1) {
                getSc().broadcastBidRetrieved(getDungeonLordSortedList(), singleBid.get().getBid(),
                        dungeonLord.getPlayerID());
            }
        }


    }

    private void retrieve2n3Bid(final int currRound) {
        final List<DungeonLord> dungeonLordList = getDungeonLordSortedList();

        for (final DungeonLord dl : dungeonLordList) {

            if (currRound > 1) {
                for (int bidNum = 0; bidNum < 2; bidNum++) {
                    final Optional<BidType> bidType = dl.getLockedBids().get(bidNum);
                    getSc().broadcastBidRetrieved(getDungeonLordSortedList(), bidType.get(),
                            dl.getPlayerID());
                }
            }
            retrieveDlFirstBid(dl);
        }
    }

    private void checkAndActivateRoom() {

        for (final DungeonLord dungeonLord : getDungeonLordSortedList()) {
            final Dungeon currDlDungeon = dungeonLord.getDungeon();

            final Iterator<Room> roomIterator = currDlDungeon.getAvailableRooms().iterator();
            while (roomIterator.hasNext()) {
                final Room room = roomIterator.next();
                if (room.isCurrentProducing()) {
                    room.retrieveProducedGoods(this, dungeonLord.getCommID());
                }
            }
        }
    }

    /*
    public void execute(final BiddingSquare bs) {
        // TODO: not used function
    }
    */

    /**
     * sorts the array as per difficulty/ strength and also as per id
     *
     * @param sortedAdventures array of adventurers
     * @return the sorted array
     */
    //    private Adventurer[] sortAdventures(Adventurer[] sortedAdventures) {
    //        final int n = sortedAdventures.length;
    //        for (int i = 1; i < n; ++i) {
    //            final Adventurer key = sortedAdventures[i];
    //            int j = i - 1;
    //
    //            /* Move elements of arr[0..i-1], that are
    //               greater than key, to one position ahead
    //               of their current position */
    //            while (j >= 0 && sortedAdventures[j].getDifficulty() >= key.getDifficulty()) {
    //
    //                // if they have same difficulty
    //                if (sortedAdventures[j + 1].getDifficulty()
    //                        == sortedAdventures[j].getDifficulty()) {
    //                    if (sortedAdventures[j].getId() < sortedAdventures[j + 1].getId()) {
    //                        continue;
    //                    }
    //                }
    //                sortedAdventures[j + 1] = sortedAdventures[j];
    //                j = j - 1;
    //            }
    //            sortedAdventures[j + 1] = key;
    //        }
    //
    //        return sortedAdventures;
    //    }

    /**
     * sorts the array as per difficulty/ strength and also as per id
     *
     * @param sortedDungeonLords array of adventurers to be sorted as per difficulty/ strength
     * @return the sorted array
     */
    //    private DungeonLord[] sortDungeonLords(DungeonLord[] sortedDungeonLords) {
    //        final int n = sortedDungeonLords.length;
    //        for (int i = 1; i < n; ++i) {
    //            final DungeonLord key = sortedDungeonLords[i];
    //            int j = i - 1;
    //
    //            /* Move elements of arr[0..i-1], that are
    //               greater than key, to one position ahead
    //               of their current position */
    //            while (j >= 0 && sortedDungeonLords[j].getEvilness() > key.getEvilness()) {
    //
    //                // if they have same evilness
    //                if (sortedDungeonLords[j + 1].getEvilness()
    //                        == sortedDungeonLords[j].getEvilness()) {
    //                    if (sortedDungeonLords[j].getPlayerID() < sortedDungeonLords[j
    //                            + 1].getPlayerID()) {
    //                        continue;git
    //                    }
    //                }
    //                sortedDungeonLords[j + 1] = sortedDungeonLords[j];
    //                j = j - 1;
    //            }
    //            sortedDungeonLords[j + 1] = key;
    //        }
    //
    //        return sortedDungeonLords;
    //    }

    /**
     * First sorts the Dungeon lord and adventurers and then assigns the   to the dungeon lords
     */
    private void assignAdventurers() {
        //        Adventurer[] sorterAdventures = new Adventurer[poolAdventurers.length];
        //        DungeonLord[] sortedDungeonLordEvil = new DungeonLord[dungeonLordMap.size()];
        //        sortAdventures(poolAdventurers);

        final List<Adventurer> advForThisYear = new LinkedList<>();
        Collections.addAll(advForThisYear, poolAdventurers);

        final List<DungeonLord> dungeonLordList = new LinkedList<>();

        for (final DungeonLord dungeonLord : dungeonLordMap.values()) {
            dungeonLordList.add(dungeonLord);
        }

        Collections.sort(advForThisYear, Comparator.comparing(Adventurer::getDifficulty)
                .thenComparing(Adventurer::getId));

        Collections.sort(dungeonLordList, Comparator.comparing(DungeonLord::getEvilness)
                .thenComparing(DungeonLord::getPlayerID));

        //        Collections.sort(advForThisYear,
        //                (u1, u2) -> u1.getDifficulty.compareTo(u2.getDifficulty()));

        //        int i = 0;
        //        for (final DungeonLord dungeonLord : dungeonLordMap.values()) {
        //            sortedDungeonLordEvil[i] = dungeonLord;
        //            i++;
        //        }
        //        // TODO: implement sort by player id after evilness in  sorting DungeonLord: Done
        //        // TODO: if the adventure is warrior then put it in the front?
        //        // TODO: what to do with traps
        //        sortDungeonLords(sortedDungeonLordEvil);

        for (int j = 0; j < dungeonLordList.size(); j++) {

            final Adventurer currAdv = advForThisYear.get(j);

            if (currAdv.isCharge()) {
                dungeonLordList.get(j).getDungeon().getAdventurers().add(0, currAdv);
                sc.broadcastAdventurerArrived(dungeonLordSortedList, advForThisYear.get(j).getId(),
                        dungeonLordList.get(j).getPlayerID());
            } else {

                dungeonLordList.get(j).getDungeon().getAdventurers().add(currAdv);
                sc.broadcastAdventurerArrived(dungeonLordSortedList, advForThisYear.get(j).getId(),
                        dungeonLordList.get(j).getPlayerID());
            }
        }
    }

}

