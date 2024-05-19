package de.unisaarland.cs.se.selab.actions;


import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BuildRoom extends Action {

    private int row;
    private int column;
    private int id;

    public BuildRoom(final int commID, final int column, final int row, final int id) {
        super(commID);
        this.row = row;
        this.column = column;
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(final int column) {
        this.column = column;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public boolean isBuildRoom() {
        return true;
    }

    @Override
    public boolean exec(final EvaluationPhase ep) {
        if (!ep.isExpectingRoom()) {
            ep.getSc().sendActionFailed(getCommID(), "wrong action");
            return false;
        }
        final List<Room> rooms = ep.getBiddingSquare().getRooms();
        Room roomToPlace = null;

        // get the room with ID

        for (final Room r : rooms) {
            if (r.getId() == this.getId()) {
                roomToPlace = r;
                break;
            }
        }

        // return false if the room ID is wrong
        if (roomToPlace == null) {
            ep.getSc().sendActionFailed(getCommID(), "invalid room ID");
            return false;
        }

        final int row = getRow();
        final int col = getColumn();
        final List<List<Optional<Tile>>> tiles =
                ep.getDungeonLordMap().get(getCommID()).getDungeon().getTiles();

        // return false if player sent invalid coordinates
        if (row < 0 || col < 0 || row >= tiles.size() || col >= tiles.size()
                || tiles.get(row).get(col).isEmpty()) {
            ep.getSc().sendActionFailed(getCommID(), "invalid coordinates");
            return false;
        }

        final String restriction = roomToPlace.getPlacingRestriction();

        if (checkValidCoordinates(col, row, tiles)
                && checkPlacementRestriction(col, row, tiles, restriction)) {
            // assign room
            final List<Optional<Tile>> currColumn = tiles.get(row);
            currColumn.set(col, Optional.of(roomToPlace));
            // remove the room from bidding square
            rooms.remove(roomToPlace);
            // add room in the list of available rooms of dungeon lord
            ep.getDungeonLordMap().get(getCommID()).getDungeon()
                    .getAvailableRooms().add(roomToPlace);
            // broadcast event
            ep.getSc().broadcastRoomBuilt(ep.getDungeonLordSortedList(),
                    ep.getDungeonLordMap().get(getCommID()).getPlayerID(), getId(), getColumn(),
                    getRow());
            return true;
        }
        ep.getSc().sendActionFailed(getCommID(), "invalid room placement");
        return false;
    }

    /**
     * checks that the coordinates of the room are valid, i.e. the tile is not conquered and there
     * is no adjacent room
     *
     * @param row    row index of room to place
     * @param column column index of room to place
     * @param tiles  (dungeon of the player)
     * @return true if the room can be placed on tile with coordinates (row, column)
     */
    public boolean checkValidCoordinates(final int column, final int row,
            final List<List<Optional<Tile>>> tiles) {
        // returns true if the tile is not conquered and there is no adjacent room
        if (tiles.get(row).get(column).isEmpty()) {
            return false;
        }
        final Tile roomToBuild = tiles.get(row).get(column).get();
        if (!roomToBuild.isRoom() && !roomToBuild.getIsConquered()) {
            boolean below = false;
            boolean above = false;
            boolean toRight = false;
            boolean toLeft = false;

            if (row != tiles.size() - 1) {
                below = checkBelow(column, row, tiles);
            }
            if (row != 0) {
                above = checkAbove(column, row, tiles);
            }
            if (column != tiles.size() - 1) {
                toRight = checkRight(column, row, tiles);
            }
            if (column != 0) {
                toLeft = checkLeft(column, row, tiles);
            }
            return !(below || above || toLeft || toRight);
        }
        return false;
    }


    public boolean checkPlacementRestriction(final int column, final int row,
            final Collection<List<Optional<Tile>>> tiles, final String restriction) {
        switch (restriction) {
            case "UPPER_HALF":
                return row <= tiles.size() / 2 - 1;
            case "LOWER_HALF":
                return row > tiles.size() / 2 - 1;
            case "OUTER_RING":
                return !(column > 0 && column < tiles.size() - 1)
                        || !(row > 0 && row < tiles.size() - 1);
            case "INNER_RING":
                return (column > 0 && column < tiles.size() - 1)
                        && (row > 0 && row < tiles.size() - 1);
            default:
                return false;
        }
    }

    /**
     * @param row    y coordinate
     * @param column x coordinate
     * @param tiles  dungeon
     * @return true if tile to the left of ( column, row) is a room
     */
    protected boolean checkLeft(final int column, final int row,
            final List<List<Optional<Tile>>> tiles) {

        return (tiles.get(row).get(column - 1).isPresent()
                && tiles.get(row).get(column - 1).get().isRoom());

    }

    /**
     * @param row    y coordinate
     * @param column x coordinate
     * @param tiles  dungeon
     * @return true if tile to the right of (column, row) is a room
     */
    protected boolean checkRight(final int column, final int row,
            final List<List<Optional<Tile>>> tiles) {
        return (tiles.get(row).get(column + 1).isPresent()
                && tiles.get(row).get(column + 1).get().isRoom());

    }

    /**
     * @param row    y coordinate
     * @param column x coordinate
     * @param tiles  dungeon
     * @return true if the tile above of (column, row) is a room
     */
    protected boolean checkAbove(final int column, final int row,
            final List<List<Optional<Tile>>> tiles) {
        return (tiles.get(row - 1).get(column).isPresent()
                && tiles.get(row - 1).get(column).get().isRoom());

    }

    /**
     * @param row    y coordinate
     * @param column x coordinate
     * @param tiles  dungeon
     * @return true if the tile below (column, row) is a room
     */
    protected boolean checkBelow(final int column, final int row,
            final List<List<Optional<Tile>>> tiles) {
        return (tiles.get(row + 1).get(column).isPresent()
                && tiles.get(row + 1).get(column).get().isRoom());
    }
}
