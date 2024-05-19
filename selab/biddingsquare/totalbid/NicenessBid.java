package de.unisaarland.cs.se.selab.biddingsquare.totalbid;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.Queue;

public class NicenessBid extends TotalBid {

    public NicenessBid(final BroadcastEvents serverConnection) {
        super(BidType.NICENESS, serverConnection);
    }

    @Override
    public void evaluateAllBids(final EvaluationPhase evalPhase,
            final Queue<SingleBid> singleBids) {

        if (singleBids.isEmpty()) {
            return;
        }

        int counter = 1;
        for (final SingleBid singleBid : singleBids) {
            if (!evalPhase.getDungeonLordMap().containsKey(singleBid.getCommID())) {
                counter++;
                continue;
            }
            if (counter > 3) {
                break;
            }
            final DungeonLord player = evalPhase.getDungeonLordMap().get(singleBid.getCommID());
            if (checkResources(player, counter)) {
                changeResources(evalPhase, player, counter);
            }
            counter++;
        }
    }

    private boolean checkResources(final DungeonLord player,
            final int slot) {
        switch (slot) {
            case 1 -> {
                return player.getEvilness() >= 1;
            }
            case 2 -> {
                return player.getEvilness() >= 2;
            }
            case 3 -> {
                return player.getGold() >= 1 && player.getEvilness() >= 2;
            }
            default -> {
                return false;
            }
        }
    }

    private void changeResources(final EvaluationPhase evalPhase, final DungeonLord player,
            final int slot) {
        switch (slot) {
            case 1 -> {
                player.setEvilness(player.getEvilness() - 1);
                evalPhase.getSc().broadcastEvilnessChanged(evalPhase.getDungeonLordSortedList(),
                        -1, player.getPlayerID());
            }
            case 2 -> {
                player.setEvilness(player.getEvilness() - 2);
                evalPhase.getSc().broadcastEvilnessChanged(evalPhase.getDungeonLordSortedList(),
                        -2, player.getPlayerID());
            }
            case 3 -> {
                player.setGold(player.getGold() - 1);
                evalPhase.getSc().broadcastGoldChanged(evalPhase.getDungeonLordSortedList(),
                        -1, player.getPlayerID());
                player.setEvilness(player.getEvilness() - 2);
                evalPhase.getSc().broadcastEvilnessChanged(evalPhase.getDungeonLordSortedList(),
                        -2, player.getPlayerID());
            }
            default -> {
            }
        }
    }
}
