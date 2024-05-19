package de.unisaarland.cs.se.selab.biddingsquare.totalbid;

import de.unisaarland.cs.se.selab.actions.Action;
import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.io.IOException;
import java.util.Queue;

public class RoomBid extends TotalBid {

    public RoomBid(final BroadcastEvents serverConnection) {
        super(BidType.ROOM, serverConnection);
    }


    @Override
    public void evaluateAllBids(final EvaluationPhase evalPhase,
            final Queue<SingleBid> singleBids) throws IOException {
        if (singleBids.isEmpty()) {
            return;
        }
        int availableRooms = 2;
        int counter = 1;
        for (final SingleBid singleBid : singleBids) {
            if (!evalPhase.getDungeonLordMap().containsKey(singleBid.getCommID())) {
                counter++;
                continue;
            }
            final DungeonLord player = evalPhase.getDungeonLordMap().get(singleBid.getCommID());
            if (counter < 3 && checkResources(player)) {
                changeResources(evalPhase, player);
                if (roomChosen(evalPhase, player)) {
                    availableRooms -= 1;
                }
            }
            if (counter == 3 && availableRooms > 0) {
                roomChosen(evalPhase, player);
            }
            counter++;
        }
        evalPhase.setExpectingRoom(false);
    }

    private boolean checkResources(final DungeonLord player) {
        return player.getGold() >= 1;
    }

    private void changeResources(final EvaluationPhase evalPhase, final DungeonLord player) {
        player.setGold(player.getGold() - 1);
        evalPhase.getSc().broadcastGoldChanged(evalPhase.getDungeonLordSortedList(),
                -1, player.getPlayerID());
    }

    /**
     * @param evalPhase current phase
     * @param player    current player
     * @return true if player built a room
     */
    private boolean roomChosen(final EvaluationPhase evalPhase, final DungeonLord player)
            throws IOException {
        // should check if player chose a room
        evalPhase.setExpectingRoom(true);
        evalPhase.getSc().sendPlaceRoom(player.getCommID());
        while (true) {
            final Action receivedAction = evalPhase.recieveAction(player.getCommID());
            final boolean successful = receivedAction.exec(evalPhase);
            if (receivedAction.isBuildRoom() && successful) {
                // player built room successfully
                evalPhase.setExpectingRoom(false);
                return true;
            } else if (receivedAction.isEndTurn() && successful) {
                // player ended turn successfully
                return false;
            } else if (receivedAction.isLeave()) {
                return false;
            }
        }
    }
}