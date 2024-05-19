package de.unisaarland.cs.se.selab.biddingsquare.totalbid;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Imp;
import de.unisaarland.cs.se.selab.dungeon.Imp.ActionType;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.Queue;

public class ImpsBid extends TotalBid {

    public ImpsBid(final BroadcastEvents serverConnection) {
        super(BidType.IMPS, serverConnection);
    }

    @Override
    public void evaluateAllBids(final EvaluationPhase evaluationPhase,
            final Queue<SingleBid> singleBids) {
        if (singleBids.isEmpty()) {
            return;
        }

        int counter = 1;
        for (final SingleBid singleBid : singleBids) {
            if (!evaluationPhase.getDungeonLordMap().containsKey(singleBid.getCommID())) {
                counter++;
                continue;
            }
            if (counter > 3) {
                break;
            }
            final DungeonLord player = evaluationPhase.getDungeonLordMap()
                    .get(singleBid.getCommID());
            if (checkResources(player, counter)) {
                changeResources(evaluationPhase, player, counter);
            }
            counter++;
        }
    }

    private boolean checkResources(final DungeonLord player, final int slot) {
        return switch (slot) {
            case 1 -> player.getFood() >= 1;
            case 2 -> player.getFood() >= 2;
            case 3 -> player.getFood() >= 1 && player.getGold() >= 1;
            default -> false;
        };
    }

    private void changeResources(final EvaluationPhase evalPhase, final DungeonLord player,
            final int slot) {
        switch (slot) {
            case 1 -> {
                player.setFood(player.getFood() - 1);
                notifyPlayers(evalPhase, EventType.FOOD, -1, player.getPlayerID());
                final Imp acquiredImp = new Imp(ActionType.RESTING);
                player.getImps().add(acquiredImp);
                player.setNUmberOfImps(player.getNumberOfImps() + 1);
                notifyPlayers(evalPhase, EventType.IMP, 1, player.getPlayerID());
            }
            case 2 -> {
                player.setFood(player.getFood() - 2);
                notifyPlayers(evalPhase, EventType.FOOD, -2, player.getPlayerID());
                final Imp firstAcquiredImp = new Imp(ActionType.RESTING);
                final Imp secondAcquiredImp = new Imp(ActionType.RESTING);
                player.getImps().add(firstAcquiredImp);
                player.getImps().add(secondAcquiredImp);
                player.setNUmberOfImps(player.getNumberOfImps() + 2);
                notifyPlayers(evalPhase, EventType.IMP, 2, player.getPlayerID());
            }
            case 3 -> {
                player.setFood(player.getFood() - 1);
                notifyPlayers(evalPhase, EventType.FOOD, -1, player.getPlayerID());
                player.setGold(player.getGold() - 1);
                notifyPlayers(evalPhase, EventType.GOLD, -1, player.getPlayerID());
                final Imp firstAcquiredImp = new Imp(ActionType.RESTING);
                final Imp secondAcquiredImp = new Imp(ActionType.RESTING);
                player.getImps().add(firstAcquiredImp);
                player.getImps().add(secondAcquiredImp);
                player.setNUmberOfImps(player.getNumberOfImps() + 2);
                notifyPlayers(evalPhase, EventType.IMP, 2, player.getPlayerID());
            }
            default -> {
            }
        }
    }

    private void notifyPlayers(final EvaluationPhase evalPhase, final EventType event,
            final int amount, final int player) {
        switch (event) {
            case FOOD -> {
                evalPhase.getSc().broadcastFoodChanged(evalPhase.getDungeonLordSortedList(),
                        amount, player);
            }
            case GOLD -> {
                evalPhase.getSc().broadcastGoldChanged(evalPhase.getDungeonLordSortedList(),
                        amount, player);
            }
            case IMP -> {
                evalPhase.getSc().broadcastImpsChanged(evalPhase.getDungeonLordSortedList(),
                        amount, player);
            }
            default -> {
            }
        }
    }

    private enum EventType {
        FOOD,
        IMP,
        GOLD
    }
}
