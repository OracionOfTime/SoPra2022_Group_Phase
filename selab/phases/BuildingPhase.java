package de.unisaarland.cs.se.selab.phases;

import de.unisaarland.cs.se.selab.actions.Action;
import de.unisaarland.cs.se.selab.actions.Leave;
import de.unisaarland.cs.se.selab.biddingsquare.BiddingSquare;
import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.dungeon.Adventurer;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.game.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class BuildingPhase extends Phase {

    private int biddingPriority = -1;
    private BiddingSquare bs;
    private Queue<Monster> monsterPool;
    private Adventurer[] availableAdventurers;
    private Queue<Room> roomPool;

    public BuildingPhase(final BroadcastEvents sc,
            final List<DungeonLord> dungeonLordArray,
            final Map<Integer, DungeonLord> dungeonLordMap,
            final Queue<Monster> monsterPool,
            final Queue<Room> roomPool) {
        super(sc, dungeonLordArray, dungeonLordMap);
        this.monsterPool = monsterPool;
        this.roomPool = roomPool;

    }

    public int getBiddingPriority() {
        return biddingPriority;
    }

    public void setBiddingPriority(final int biddingPriority) {
        this.biddingPriority = biddingPriority;
    }

    public BiddingSquare getBs() {
        return bs;
    }

    public void setBs(final BiddingSquare bs) {
        this.bs = bs;
    }

    public Queue<Monster> getMonsterPool() {
        return monsterPool;
    }

    public void setMonsterPool(final Queue<Monster> monsterPool) {
        this.monsterPool = monsterPool;
    }

    public Adventurer[] getAvailableAdventurers() {
        return availableAdventurers.clone();
    }

    public void setAvailableAdventurers(
            final Adventurer[] availableAdventurers) {
        this.availableAdventurers = availableAdventurers;
    }

    public Queue<Room> getRoomPool() {
        return roomPool;
    }

    public void setRoomPool(final Queue<Room> roomPool) {
        this.roomPool = roomPool;
    }


    public BiddingSquare execute(final Game game) throws IOException {
        if (game.getRounds() < 4) {
            drawAdventurers(game);
        }
        this.biddingPriority++;
        this.bs = initializeBiddingSquare(game);
        sc.broadcastBiddingStarted(dungeonLordSortedList);
        sc.broadcastActNow(dungeonLordSortedList);

        while (!areAllBidsPlaced()) {
            final Action nextBid = getNextBid();

            if (nextBid != null && nextBid.exec(this)) {
                if (!allBidsOfDl(dungeonLordMap.get(nextBid.getCommID()))) {
                    sc.sendActNow(nextBid.getCommID());
                }
            }
        }

        return this.bs;
    }

    private boolean allBidsOfDl(final DungeonLord tempDL) {

        final List<Optional<SingleBid>> tempBids = tempDL.getBids();

        for (final Optional<SingleBid> tempOpBid : tempBids) {
            if (tempOpBid.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private void kickDl(final Iterable<DungeonLord> dungeonLordList) throws IOException {

        for (final DungeonLord tempDL : dungeonLordList) {
            final List<Optional<SingleBid>> tempBids = tempDL.getBids();
            for (final Optional<SingleBid> tempOpBid : tempBids) {
                if (tempOpBid.isEmpty()) {
                    final Action leave = new Leave(tempDL.getCommID());
                    leave.exec(this);
                }
            }
        }
    }

    private boolean areAllBidsPlaced() {
        for (final DungeonLord tempDL : dungeonLordSortedList) {

            final List<Optional<SingleBid>> tempBids = tempDL.getBids();

            for (final Optional<SingleBid> tempOpBid : tempBids) {
                if (tempOpBid.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Action getNextBid() throws IOException {
        try {
            return this.sc.nextAction();
        } catch (TimeoutException e) {
            kickDl(this.dungeonLordSortedList);
            return null;
        }
    }

    private BiddingSquare initializeBiddingSquare(final Game game) {

        final ArrayList<Monster> drawnMonsters = new ArrayList<>();

        Monster tempMonster = this.monsterPool.poll();
        drawnMonsters.add(tempMonster);
        sc.broadcastMonsterDrawn(dungeonLordSortedList, tempMonster.getId());

        tempMonster = this.monsterPool.poll();
        drawnMonsters.add(tempMonster);
        sc.broadcastMonsterDrawn(dungeonLordSortedList, tempMonster.getId());

        tempMonster = this.monsterPool.poll();
        drawnMonsters.add(tempMonster);
        sc.broadcastMonsterDrawn(dungeonLordSortedList, tempMonster.getId());

        final ArrayList<Room> drawnRooms = new ArrayList<>();
        Room tempRoom = roomPool.poll();
        drawnRooms.add(tempRoom);
        sc.broadcastRoomDrawn(dungeonLordSortedList, tempRoom.getId());

        tempRoom = roomPool.poll();
        drawnRooms.add(tempRoom);
        sc.broadcastRoomDrawn(dungeonLordSortedList, tempRoom.getId());

        return new BiddingSquare(dungeonLordMap, drawnMonsters, game.getTrapPool(), drawnRooms,
                biddingPriority, sc);
    }

    protected void drawAdventurers(final Game game) {

        final Adventurer[] drawnAdv = new Adventurer[game.getDungeonLordMap().size()];

        for (int i = 0; i < game.getDungeonLordMap().size(); i++) {
            final Adventurer tempAdv = game.getAdventurerPool().poll();
            drawnAdv[i] = tempAdv;
            assert tempAdv != null;
            sc.broadcastAdventurerDrawn(dungeonLordSortedList, tempAdv.getId());
        }

        this.availableAdventurers = drawnAdv;

    }
}




