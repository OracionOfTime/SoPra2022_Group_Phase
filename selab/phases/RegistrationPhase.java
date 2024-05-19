package de.unisaarland.cs.se.selab.phases;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Imp;
import de.unisaarland.cs.se.selab.dungeon.Imp.ActionType;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.dungeon.Tunnel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RegistrationPhase extends Phase {

    protected int sideLength;

    protected String config;

    protected int maxPlayer;

    private int initialFood;
    private int initialGold;
    private int initialImps;

    private int initialE;


    public RegistrationPhase(final BroadcastEvents sc, final List<DungeonLord> dungeonLordArrayList,
            final Map<Integer, DungeonLord> dungeonLordMap, final int sideLength,
            final String config, final int maxPlayer, final int initialFood, final int initialGold,
            final int initialImps, final int initialEvilness) {
        super(sc, dungeonLordArrayList, dungeonLordMap);
        this.sideLength = sideLength;
        this.config = config;
        this.maxPlayer = maxPlayer;
        this.initialFood = initialFood;
        this.initialGold = initialGold;
        this.initialImps = initialImps;
        this.initialE = initialEvilness;

    }

    public int getInitialFood() {
        return initialFood;
    }

    public void setInitialFood(final int initialFood) {
        this.initialFood = initialFood;
    }


    public int getInitialGold() {
        return initialGold;
    }

    public void setInitialGold(final int initialGold) {
        this.initialGold = initialGold;
    }


    public int getInitialImps() {
        return initialImps;
    }

    public void setInitialImps(final int initialImps) {
        this.initialImps = initialImps;
    }


    public int getMaxPlayer() {
        return this.maxPlayer;
    }

    public void setMaxPlayer(final int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public int getSideLength() {
        return sideLength;
    }

    public void setSideLength(final int sideLength) {
        this.sideLength = sideLength;
    }

    public String getConfig() {
        return this.config;
    }

    public void setConfig(final String config) {
        this.config = config;
    }

    public int getInitialE() {
        return initialE;
    }

    public void setInitialE(final int initialE) {
        this.initialE = initialE;
    }

    public void createDL(final String playerName, final int commID) {

        final Map<Integer, DungeonLord> dungeonLordMap = this.getDungeonLordMap();
        final int sizeOfDungeonLordMap = dungeonLordMap.size();
        final Tile firstTile = new Tunnel(0, 0, false);

        List<List<Optional<Tile>>> tilesDungeon = new ArrayList<>(sideLength);
        List<Optional<SingleBid>> bidsOfDungeonLord = new ArrayList<>(3);
        List<Optional<BidType>> lockedBids = new ArrayList<>(2);

        tilesDungeon = initializeDungeonTiles(tilesDungeon, firstTile);
        final Dungeon dungeon = new Dungeon(tilesDungeon);
        bidsOfDungeonLord = initializeBids(bidsOfDungeonLord);
        lockedBids = initializeLockedBids(lockedBids);

        List<Imp> impArrayList = new ArrayList<>();
        impArrayList = initializeImpList(impArrayList);

        final DungeonLord dl = new DungeonLord(sizeOfDungeonLordMap, commID, bidsOfDungeonLord,
                playerName, initialE, lockedBids,
                dungeon, impArrayList, getInitialImps(), getInitialFood(), getInitialGold(),
                0, true);
        this.dungeonLordMap.put(commID, dl);
        this.dungeonLordSortedList.add(dl);
        sendConfig(this.getConfig(), commID);
    }

    private List<Imp> initializeImpList(final List<Imp> impArrayList) {
        final List<Imp> impList = impArrayList;
        for (int i = 0; i < getInitialImps(); i++) {
            final Imp imp = new Imp(ActionType.RESTING);
            impList.add(imp);
        }
        return impList;
    }

    private List<Optional<BidType>> initializeLockedBids(
            final List<Optional<BidType>> lockedBids) {
        final List<Optional<BidType>> currentLockedBids = lockedBids;
        for (int i = 0; i < 2; i++) {
            currentLockedBids.add(Optional.empty());
        }
        return currentLockedBids;
    }

    private List<Optional<SingleBid>> initializeBids(
            final List<Optional<SingleBid>> bidsOfDungeonLord) {
        final List<Optional<SingleBid>> currentBids = bidsOfDungeonLord;
        for (int i = 0; i < 3; i++) {
            currentBids.add(Optional.empty());
        }
        return currentBids;
    }

    private List<List<Optional<Tile>>> initializeDungeonTiles(
            final List<List<Optional<Tile>>> dungeonTiles, final Tile firstTile) {
        for (int i = 0; i < 4; i++) {
            dungeonTiles.add(createEmptyList());
        }
        dungeonTiles.get(0).set(0, Optional.of(firstTile));
        return dungeonTiles;
    }

    private List<Optional<Tile>> createEmptyList() {
        final List<Optional<Tile>> tempRow = new ArrayList<>(sideLength);
        for (int j = 0; j < 4; j++) {
            tempRow.add(Optional.empty());
        }
        return tempRow;
    }


    public void registrationAborted() {
        sc.broadcastRegistrationAborted(this.dungeonLordSortedList);
    }

    public void sendConfig(final String config, final int commID) {
        sc.sendConfig(commID, config);
    }
}
