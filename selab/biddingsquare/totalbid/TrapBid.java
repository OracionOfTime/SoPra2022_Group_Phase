package de.unisaarland.cs.se.selab.biddingsquare.totalbid;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Trap;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.Queue;

public class TrapBid extends TotalBid {

    public TrapBid(final BroadcastEvents serverConnection) {
        super(BidType.TRAP, serverConnection);
    }

    @Override
    public void evaluateAllBids(final EvaluationPhase evalPhase,
            final Queue<SingleBid> singleBids) {
        if (singleBids.isEmpty()) {
            return;
        }
        int counter = 1;
        for (final SingleBid singleBid : singleBids) {
            if (counter > 3) {
                break;
            }
            if (!evalPhase.getDungeonLordMap().containsKey(singleBid.getCommID())) {
                counter++;
                continue;
            }

            final DungeonLord player = evalPhase.getDungeonLordMap().get(singleBid.getCommID());
            if (checkResources(player, counter)) {
                changeResources(evalPhase, player, counter);
            }
            counter++;
        }
    }

    private boolean checkResources(final DungeonLord player, final int slot) {
        switch (slot) {
            case 1:
                return player.getGold() >= 1;
            case 2:
                return true;
            case 3:
                return player.getGold() >= 2;
            default:
                return false;
        }
    }

    private void changeResources(final EvaluationPhase evalPhase,
            final DungeonLord player, final int slot) {
        switch (slot) {
            case 1 -> {
                player.setGold(player.getGold() - 1);
                evalPhase.getSc().broadcastGoldChanged(evalPhase.getDungeonLordSortedList(),
                        -1, player.getPlayerID());
                final Trap trap = evalPhase.getBiddingSquare().getTraps().poll();
                player.getDungeon().getTraps().add(trap);
                evalPhase.getSc().broadcastTrapAcquired(evalPhase.getDungeonLordSortedList(),
                        player.getPlayerID(), trap.getId());
            }
            case 2 -> {
                final Trap trap = evalPhase.getBiddingSquare().getTraps().poll();
                player.getDungeon().getTraps().add(trap);
                evalPhase.getSc().broadcastTrapAcquired(evalPhase.getDungeonLordSortedList(),
                        player.getPlayerID(), trap.getId());
            }
            case 3 -> {
                player.setGold(player.getGold() - 2);
                evalPhase.getSc().broadcastGoldChanged(evalPhase.getDungeonLordSortedList(),
                        -2, player.getPlayerID());
                final Trap firstTrap = evalPhase.getBiddingSquare().getTraps().poll();
                final Trap secondTrap = evalPhase.getBiddingSquare().getTraps().poll();
                player.getDungeon().getTraps().add(firstTrap);
                player.getDungeon().getTraps().add(secondTrap);
                evalPhase.getSc().broadcastTrapAcquired(evalPhase.getDungeonLordSortedList(),
                        player.getPlayerID(), firstTrap.getId());
                evalPhase.getSc().broadcastTrapAcquired(evalPhase.getDungeonLordSortedList(),
                        player.getPlayerID(), secondTrap.getId());
            }
            default -> {
                return;
            }
        }
    }
}
