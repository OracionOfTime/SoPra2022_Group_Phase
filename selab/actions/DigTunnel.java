package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.dungeon.Tunnel;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.List;
import java.util.Optional;

public class DigTunnel extends Action {

    private int row;
    private int column;

    public DigTunnel(final int commID, final int column, final int row) {
        super(commID);
        this.row = row;
        this.column = column;
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

    @Override
    public boolean exec(final EvaluationPhase ep) {

        if (ep.isExpectingTunnel()) {

            final DungeonLord dl = ep.getDungeonLordMap().get(this.getCommID());
            final Dungeon dungeon = dl.getDungeon();

            final List<List<Optional<Tile>>> tiles = dungeon.getTiles();

            if (withinBounds(dungeon, row, column) && tiles.get(row).get(column).isEmpty()) {
                if (canBeDug(dungeon, row, column) && dl.getNumberOfImps() >= 1) {

                    final Optional<Tile> tileToAdd = Optional.of((new Tunnel(column, row, false)));
                    final List<Optional<Tile>> listToAdd = tiles.get(row);
                    listToAdd.set(column, tileToAdd);
                    tiles.set(row, listToAdd);

                    digTunnel5imps(ep, dl);

                    ep.getSc().broadcastTunnelDug(ep.getDungeonLordSortedList(), dl.getPlayerID(),
                            column, row);
                    ep.setExpectingTunnel(false);
                    return true;
                } else {
                    ep.setExpectingTunnel(false);
                    ep.getSc().sendActionFailed(getCommID(), "tile cannot be dug");
                    return false;
                }
            } else {
                ep.setExpectingTunnel(false);
                ep.getSc().sendActionFailed(getCommID(), "tile cannot be dug");
                return false;
            }
        }
        ep.getSc().sendActionFailed(getCommID(), "wrong action");
        ep.setExpectingTunnel(false);
        return false;

    }

    private void digTunnel5imps(final EvaluationPhase ep, final DungeonLord dl) {
        if (ep.isDigTunnel5Imps()) {
            ep.getSc().broadcastImpsChanged(ep.getDungeonLordSortedList(), -2,
                    dl.getPlayerID());
        } else {
            ep.getSc().broadcastImpsChanged(ep.getDungeonLordSortedList(), -1,
                    dl.getPlayerID());
        }
    }


    public boolean canBeDug(final Dungeon d, final int row, final int column) {
        final List<List<Optional<Tile>>> tiles = d.getTiles();

        if (withinBounds(d, row - 1, column) && tiles.get(row - 1).get(column).isPresent()) {
            return isNotASquare(d, row, column);
        } else if (withinBounds(d, row + 1, column) && tiles.get(row + 1).get(column).isPresent()) {
            return isNotASquare(d, row, column);
        } else if (withinBounds(d, row, column - 1) && tiles.get(row).get(column - 1).isPresent()) {
            return isNotASquare(d, row, column);
        } else if (withinBounds(d, row, column + 1) && tiles.get(row).get(column + 1).isPresent()) {
            return isNotASquare(d, row, column);
        }

        return false;
    }

    private boolean isNotASquare(final Dungeon d, final int row, final int column) {
        final List<List<Optional<Tile>>> tiles = d.getTiles();

        if (withinBounds(d, row - 1, column) && tiles.get(row - 1).get(column).isPresent()
                && withinBounds(d, row - 1, column - 1) && tiles.get(row - 1).get(column - 1)
                .isPresent()
                && withinBounds(d, row, column - 1) && tiles.get(row).get(column - 1).isPresent()) {
            return false;
        } else if (withinBounds(d, row, column - 1) && tiles.get(row).get(column - 1).isPresent()
                && withinBounds(d, row + 1, column - 1) && tiles.get(row + 1).get(column - 1)
                .isPresent()
                && withinBounds(d, row + 1, column) && tiles.get(row + 1).get(column).isPresent()) {
            return false;
        } else if (withinBounds(d, row + 1, column) && tiles.get(row + 1).get(column).isPresent()
                && withinBounds(d, row + 1, column + 1) && tiles.get(row + 1).get(column
                + 1).isPresent()
                && withinBounds(d, row, column + 1) && tiles.get(row).get(column + 1).isPresent()) {
            return false;
        } else {
            return !withinBounds(d, row, column + 1) || tiles.get(row).get(column + 1).isEmpty()
                    || !withinBounds(d, row - 1, column + 1) || tiles.get(row - 1).get(column
                    + 1).isEmpty()
                    || !withinBounds(d, row - 1, column) || tiles.get(row - 1).get(column)
                    .isEmpty();
        }
    }

    private boolean withinBounds(final Dungeon dungeon, final int row, final int column) {
        final int dungeonSideLength = dungeon.getTiles().size();
        return (row >= 0 && row < dungeonSideLength && column >= 0 && column < dungeonSideLength);
    }
}
