package de.unisaarland.cs.se.selab.biddingsquare.totalbid;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Imp;
import de.unisaarland.cs.se.selab.dungeon.Imp.ActionType;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class GoldBid extends TotalBid {

    public GoldBid(final BroadcastEvents serverConnection) {
        super(BidType.GOLD, serverConnection);
    }


    @Override
    public void evaluateAllBids(final EvaluationPhase evaluationPhase,
            final Queue<SingleBid> singleBids) {

        int i = 1;
        for (final SingleBid tempBid : singleBids) {
            if (i >= 4) {
                break;
            }

            assert tempBid != null;
            if (!evaluationPhase.getDungeonLordMap().containsKey(tempBid.getCommID())) {
                i++;
                continue;
            }

            final DungeonLord tempDL = evaluationPhase.getDungeonLordMap().get(tempBid.getCommID());
            final int numberOfTiles = calculateNumberOfTiles(tempDL);
            final int availableImps = tempDL.getNumberOfImps();

            switchCase(evaluationPhase, i, tempDL, numberOfTiles, availableImps);
            i++;
        }
    }

    private void switchCase(final EvaluationPhase evaluationPhase, final int i,
            final DungeonLord tempDL, final int numberOfTiles, final int availableImps) {
        switch (i) {
            case 1 -> {
                final int impsToAssign = Math.min(numberOfTiles, Math.min(availableImps, 2));
                if (impsToAssign > 0) {
                    assignImpsToMining(tempDL, impsToAssign);
                    sendEventImpsChanged(evaluationPhase, tempDL, impsToAssign);
                }
            }
            case 2 -> {
                final int impsToAssign = Math.min(numberOfTiles, Math.min(availableImps, 3));
                if (impsToAssign > 0) {
                    assignImpsToMining(tempDL, impsToAssign);
                    sendEventImpsChanged(evaluationPhase, tempDL, impsToAssign);
                }
            }
            case 3 -> {
                final int impsToAssign = Math.min(availableImps, 5);
                if (impsToAssign > 0) {
                    assignImpsSlot3(evaluationPhase, tempDL, impsToAssign, numberOfTiles);
                }
            }
            default -> {

            }
        }
    }

    private void assignImpsToMining(final DungeonLord dl, final int impsToAssign) {
        int i = 0;
        for (final Imp tempImp : dl.getImps()) {
            if (i >= impsToAssign) {
                break;
            }
            if (tempImp.getAction() == ActionType.RESTING) {
                tempImp.setAction(ActionType.MINEGOLD);
                i++;
            }
        }
    }


    private void assignImpsSlot3(final EvaluationPhase ep, final DungeonLord tempDL,
            final int impsToAssign, final int numberOfTiles) {

        if (impsToAssign >= 5 && numberOfTiles >= 4) {
            assignImpsToMining(tempDL, 4);
            assignImpsToSupervise(tempDL);
            sendEventImpsChanged(ep, tempDL, 5);
        } else if (impsToAssign == 4) {
            assignImpsToMining(tempDL, 3);
            sendEventImpsChanged(ep, tempDL, 3);
        } else {
            assignImpsToMining(tempDL, Math.min(impsToAssign, numberOfTiles));
            sendEventImpsChanged(ep, tempDL, Math.min(impsToAssign, numberOfTiles));
        }

    }

    private void assignImpsToSupervise(final DungeonLord tempDL) {
        for (final Imp tempImp : tempDL.getImps()) {
            if (tempImp.getAction() == ActionType.RESTING) {
                tempImp.setAction(ActionType.SUPERVISE);
                break;
            }
        }
    }

    private void sendEventImpsChanged(final EvaluationPhase ep, final DungeonLord dl,
            final int impsToAssign) {
        dl.setNUmberOfImps(dl.getNumberOfImps() - impsToAssign);
        serverConnection.broadcastImpsChanged(ep.getDungeonLordSortedList(),
                -impsToAssign, dl.getPlayerID());
    }

    private int calculateNumberOfTiles(final DungeonLord dl) {
        final List<List<Optional<Tile>>> tiles = dl.getDungeon().getTiles();
        int num = 0;
        for (final List<Optional<Tile>> row : tiles) {
            for (final Optional<Tile> oneTile : row) {
                if (oneTile.isPresent() && !(oneTile.get().getIsConquered()) && !(oneTile.get()
                        .isRoom())) {
                    num++;
                }
            }
        }

        return num;
    }

}
