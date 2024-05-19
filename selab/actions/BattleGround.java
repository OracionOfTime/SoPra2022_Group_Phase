package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.phases.CombatPhase;
import java.util.List;
import java.util.Optional;

public class BattleGround extends Action {

    int row;
    int col;

    public BattleGround(final int commID, final int col, final int row) {
        super(commID);
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }


    public void setCol(final int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    @Override
    public boolean exec(final CombatPhase cp) {
        //boolean battleGroundSet = false;

        if (cp.isExpectBattleGround()) { //TODO
            final DungeonLord dl = cp.getCurrentDungeonLord();
            final Dungeon dungeon = dl.getDungeon();

            if (outOfBounds(dungeon)) {
                cp.getSc().sendActionFailed(getCommID(), "invalid coordinates");
                return false;
            }

            final List<Optional<Tile>> possibleBattleGround = cp.getPossibleBattleGround();
            for (final Optional<Tile> tempTile : possibleBattleGround) {
                if (tempTile.isPresent()) {
                    if (row == tempTile.get().getRow() && col == tempTile.get().getColumn()
                            && tempTile.get().isTunnel()) {
                        dungeon.setBattleGroundCoordinates(tempTile.get());
                        cp.getSc().broadcastBattleGroundSet(cp.getDungeonLordSortedList(),
                                dl.getPlayerID(), col, row);
                        //battleGroundSet = true;
                        cp.setExpectBattleGround(false);
                        //break;
                        return true;
                    } else {
                        cp.getSc().sendActionFailed(getCommID(), "wrong coordinates");
                        return false;
                    }
                }
            }

        }
        cp.getSc().sendActionFailed(getCommID(), "wrong action");
        //return battleGroundSet;
        return false;
    }

    private boolean outOfBounds(final Dungeon dungeon) {
        return (row < 0 || row >= dungeon.getTiles().size()
                || col < 0 || col >= dungeon.getTiles().size());
    }
}
