package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.phases.CombatPhase;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * Trap Action class is used in the combat phase to place a trap against adventures.
 */
public class Trap extends Action {

    private int trapId;

    /**
     * @param commID commId of the player using the trap
     * @param trapId trapId of the trap being placed
     */
    public Trap(final int commID, final int trapId) {
        super(commID);
        this.trapId = trapId;
    }

    public int getTrapId() {
        return trapId;
    }

    public void setTrapId(final int trapId) {
        this.trapId = trapId;
    }

    /**
     * executes Trap action
     *
     * @param cp instance of the combat phase as this action is used in Combat phase
     * @return true if the action was successful or false if action was unsuccessful
     */
    @Override
    public boolean exec(final CombatPhase cp) {

        if (!cp.isExpectTrap()) {
            cp.getSc().sendActionFailed(this.getCommID(), "not expecting trap");
            return false;
        }

        // extracting map of commID to DungeonLord to get the specific DungeonLord by its commId
        final DungeonLord player = cp.getDungeonLordMap().get(this.getCommID());

        // getting dungeon of the player
        final Dungeon dungeon = player.getDungeon();
        // getting the traps of the players
        final List<de.unisaarland.cs.se.selab.dungeon.Trap> availableTraps = dungeon.getTraps();
        final de.unisaarland.cs.se.selab.dungeon.Trap selectedTrap = checkValidId(
                availableTraps.iterator());

        // if trap with the given ID is not found inform the client that action has failed and ask
        // player to send a valid action
        if (dungeon.getActiveTrap() != null) {
            cp.getSc().sendActionFailed(getCommID(), "there is already one trap activated");
            return false;

        } else if (selectedTrap == null) {
            cp.getSc().sendActionFailed(getCommID(), "wrong trap ID");
            //cp.getSc().sendActNow(getCommID());
            return false;

        } else if (!selectedTrap.isAvailable()) { // if the trap is already used in this year
            cp.getSc().sendActionFailed(getCommID(), "this trap is unavailable");
            //cp.getSc().sendActNow(getCommID());
            return false;
        } else {

            return activateTrap(cp, player, dungeon, selectedTrap);
        }
    }

    private boolean activateTrap(final CombatPhase cp, final DungeonLord player,
            final Dungeon dungeon,
            final de.unisaarland.cs.se.selab.dungeon.Trap selectedTrap) {
        final Tile battleGroundCords = dungeon.getBattleGroundCoordinates();
        boolean isBattleGroundRoom = false;

        for (final Room room : dungeon.getAvailableRooms()) {

            if (room.getRow() == battleGroundCords.getRow()
                    && room.getColumn() == battleGroundCords.getColumn()) {
                isBattleGroundRoom = true;
                break;
            }
        }

        if (isBattleGroundRoom) {
            if (player.getGold() >= 1) {
                player.setGold(player.getGold() - 1);
                cp.getSc().broadcastGoldChanged(cp.getDungeonLordSortedList(), -1,
                        player.getPlayerID());
            } else {
                cp.getSc().sendActionFailed(getCommID(),
                        "not enough gold to activate trap in a room");
                return false;
            }
        }
        // set the active trap in dungeon
        dungeon.setActiveTrap(selectedTrap);
        selectedTrap.setAvailable(false);
        cp.getSc().broadcastTrapPlaced(cp.getDungeonLordSortedList(),
                player.getPlayerID(), selectedTrap.getId());
        cp.setExpectTrap(false);
        return true;
    }

    @Nullable
    protected de.unisaarland.cs.se.selab.dungeon.Trap checkValidId(
            final Iterator<de.unisaarland.cs.se.selab.dungeon.Trap> availableTraps) {
        // initially this variable has null
        de.unisaarland.cs.se.selab.dungeon.Trap selectedTrap = null;

        // check if the trap with the trapId exists or not. If exists set it to selectedTrap
        while (availableTraps.hasNext()) {
            final de.unisaarland.cs.se.selab.dungeon.Trap trap = availableTraps.next();
            if (trap.getId() == this.trapId) {
                selectedTrap = trap;
                break;
            }
        }
        return selectedTrap;
    }
}


