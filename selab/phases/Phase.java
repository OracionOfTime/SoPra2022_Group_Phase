package de.unisaarland.cs.se.selab.phases;

import de.unisaarland.cs.se.selab.actions.Action;
import de.unisaarland.cs.se.selab.actions.Leave;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import java.util.List;
import java.util.Map;

public abstract class Phase {

    protected BroadcastEvents sc;

    protected List<DungeonLord> dungeonLordSortedList;
    protected Map<Integer, DungeonLord> dungeonLordMap;

    public Phase(final BroadcastEvents sc,
            final List<DungeonLord> dungeonLordSortedList,
            final Map<Integer, DungeonLord> dungeonLordMap) {
        this.sc = sc;
        this.dungeonLordSortedList = dungeonLordSortedList;
        this.dungeonLordMap = dungeonLordMap;
    }

    public BroadcastEvents getSc() {
        return sc;
    }

    public void setSc(final BroadcastEvents sc) {
        this.sc = sc;
    }

    public List<DungeonLord> getDungeonLordSortedList() {
        return dungeonLordSortedList;
    }

    public void setDungeonLordSortedList(
            final List<DungeonLord> dungeonLordSortedList) {
        this.dungeonLordSortedList = dungeonLordSortedList;
    }

    public Map<Integer, DungeonLord> getDungeonLordMap() {
        return dungeonLordMap;
    }

    public void setDungeonLordMap(final Map<Integer, DungeonLord> dungeonLordMap) {
        this.dungeonLordMap = dungeonLordMap;
    }

    // java doc everywhere + Exception
    //helper function actnow
    public Action recieveAction(final int commID) {
        this.sc.sendActNow(commID);
        try {
            return this.sc.nextAction();

        } catch (TimeoutException e) {
            return new Leave(commID);
        }
    }
}
