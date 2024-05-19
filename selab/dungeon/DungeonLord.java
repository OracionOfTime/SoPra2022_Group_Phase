package de.unisaarland.cs.se.selab.dungeon;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import java.util.List;
import java.util.Optional;

public class DungeonLord {

    private int playerID;
    private int commID;
    private List<Optional<SingleBid>> bids; // TODO: size ==3
    private String name;
    private int evilness; // TODO:always starts with 5
    private List<Optional<BidType>> lockedBids; //TODO size ==2
    private Dungeon dungeon;
    private List<Imp> imps;
    private int availableImps;
    private int food;
    private int gold;
    private int points;
    private boolean isAvailable;

    public DungeonLord(final int playerID, final int commID, final List<Optional<SingleBid>> bids,
            final String name,
            final int evilness, final List<Optional<BidType>> lockedBids, final Dungeon dungeon,
            final List<Imp> imps,
            final int availableImps, final int food, final int gold, final int points,
            final boolean isAvailable) {
        this.playerID = playerID;
        this.commID = commID;
        this.bids = bids;
        this.name = name;
        this.evilness = evilness;
        this.lockedBids = lockedBids;
        this.dungeon = dungeon;
        this.imps = imps;
        this.availableImps = availableImps;
        this.food = food;
        this.gold = gold;
        this.points = points;
        this.isAvailable = isAvailable;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(final int playerID) {
        this.playerID = playerID;
    }

    public int getCommID() {
        return commID;
    }

    public void setCommID(final int commID) {
        this.commID = commID;
    }

    public List<Optional<SingleBid>> getBids() {
        return bids;
    }

    public void setBids(final List<Optional<SingleBid>> bids) {
        this.bids = bids;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getEvilness() {
        return evilness;
    }

    public void setEvilness(final int evilness) {
        this.evilness = evilness;
    }

    public List<Optional<BidType>> getLockedBids() {
        return lockedBids;
    }

    public void setLockedBids(final List<Optional<BidType>> lockedBids) {
        this.lockedBids = lockedBids;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setDungeon(final Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public List<Imp> getImps() {
        return imps;
    }

    public void setImps(final List<Imp> imps) {
        this.imps = imps;
    }

    public int getNumberOfImps() {
        return this.availableImps;
    }

    public void setNUmberOfImps(final int availableImps) {
        this.availableImps = availableImps;
    }

    public int getFood() {
        return food;
    }

    public void setFood(final int food) {
        this.food = food;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(final int gold) {
        this.gold = gold;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(final int points) {
        this.points = points;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(final boolean available) {
        isAvailable = available;
    }
}
