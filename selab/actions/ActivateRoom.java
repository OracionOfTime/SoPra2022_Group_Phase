package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Imp;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.phases.BuildingPhase;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import de.unisaarland.cs.se.selab.phases.Phase;
import java.util.Iterator;
import java.util.List;

public class ActivateRoom extends Action {

    private int room;

    public ActivateRoom(final int commID, final int room) {
        super(commID);
        this.room = room;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(final int room) {
        this.room = room;
    }

    @Override
    public boolean isActivateRoom() {
        return true;
    }

    /**
     * @param bp current building phase
     * @return true if the action could be executed
     */
    @Override
    public boolean exec(final BuildingPhase bp) {
        return activate(bp);
    }

    /**
     * @param ep current evaluation phase
     * @return true if the action was successfully executed
     */
    @Override
    public boolean exec(final EvaluationPhase ep) {
        return activate(ep);
    }

    private boolean activate(final Phase phase) {
        final DungeonLord player = phase.getDungeonLordMap().get(getCommID());
        final Dungeon dungeon = player.getDungeon();
        final List<Room> rooms = dungeon.getAvailableRooms();
        final Room roomToActivate = checkRoomValidity(rooms);

        if (roomToActivate != null) {
            if (roomToActivate.isCurrentProducing()) {
                phase.getSc().sendActionFailed(getCommID(), "room is already producing");
                return false;
            }
            final int cost = roomToActivate.getActivation();
            int availableImps = player.getNumberOfImps();
            if (availableImps >= cost) {
                final Iterator<Imp> impIterator = player.getImps().iterator();
                while (impIterator.hasNext() && availableImps > 0) {
                    final Imp imp = impIterator.next();
                    if (imp.getAction() == Imp.ActionType.RESTING) {
                        imp.setAction(Imp.ActionType.PRODUCING);
                        availableImps--;
                    }
                }
                roomToActivate.setCurrentProducing(true);
                player.setNUmberOfImps(player.getNumberOfImps() - cost);
                phase.getSc().broadcastImpsChanged(phase.getDungeonLordSortedList(),
                        -cost, player.getPlayerID());
                phase.getSc().broadcastRoomActivated(phase.getDungeonLordSortedList(),
                        player.getPlayerID(), getRoom());
                return true;
            }
        }
        phase.getSc().sendActionFailed(player.getCommID(), "invalid room ID");
        return false;
    }

    /**
     * @param rooms - available rooms in dungeon
     * @return room object from dungeon that needs to be activated; if the passed roomID was wrong,
     * return null
     */
    private Room checkRoomValidity(final Iterable<Room> rooms) {
        Room roomToActivate = null;
        for (final Room r : rooms) {
            if (r.getId() == this.getRoom()) {
                roomToActivate = r;
                break;
            }
        }
        return roomToActivate;
    }
}
