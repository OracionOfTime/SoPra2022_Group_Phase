package de.unisaarland.cs.se.selab.biddingsquare.totalbid;

import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.Queue;

public class FoodBid extends TotalBid {


    public FoodBid(final BroadcastEvents serverConnection) {
        super(BidType.FOOD, serverConnection);
    }

    @Override
    public void evaluateAllBids(final EvaluationPhase ep, final Queue<SingleBid> singleBids) {
        int i = 1;
        for (final SingleBid tempBid : singleBids) {
            assert tempBid != null;
            if (i >= 4) {
                break;
            } else {
                if (!ep.getDungeonLordMap().containsKey(tempBid.getCommID())) {
                    i++;
                    continue;
                }
                if (checkResources(ep.getDungeonLordMap().get(tempBid.getCommID()), i)) {
                    assignResources(ep, ep.getDungeonLordMap().get(tempBid.getCommID()), i);
                }
            }
            i++;
        }
    }

    private void assignResources(final EvaluationPhase ep, final DungeonLord dungeonLord,
            final int slot) {
        switch (slot) {
            case 1 -> {
                dungeonLord.setGold(dungeonLord.getGold() - 1);
                serverConnection.broadcastGoldChanged(ep.getDungeonLordSortedList(), (-1),
                        dungeonLord.getPlayerID());

                dungeonLord.setFood(dungeonLord.getFood() + 2);
                serverConnection.broadcastFoodChanged(ep.getDungeonLordSortedList(), 2,
                        dungeonLord.getPlayerID());
            }
            case 2 -> {
                dungeonLord.setEvilness(dungeonLord.getEvilness() + 1);
                serverConnection.broadcastEvilnessChanged(ep.getDungeonLordSortedList(), 1,
                        dungeonLord.getPlayerID());

                dungeonLord.setFood(dungeonLord.getFood() + 3);
                serverConnection.broadcastFoodChanged(ep.getDungeonLordSortedList(), 3,
                        dungeonLord.getPlayerID());
            }
            case 3 -> {
                dungeonLord.setEvilness(dungeonLord.getEvilness() + 2);
                serverConnection.broadcastEvilnessChanged(ep.getDungeonLordSortedList(), 2,
                        dungeonLord.getPlayerID());

                dungeonLord.setFood(dungeonLord.getFood() + 3);
                serverConnection.broadcastFoodChanged(ep.getDungeonLordSortedList(), 3,
                        dungeonLord.getPlayerID());

                dungeonLord.setGold(dungeonLord.getGold() + 1);
                serverConnection.broadcastGoldChanged(ep.getDungeonLordSortedList(), 1,
                        dungeonLord.getPlayerID());
            }
            default -> {

            }
        }
    }

    private boolean checkResources(final DungeonLord dungeonLord, final int slot) {
        return switch (slot) {
            case 1 -> dungeonLord.getGold() >= 1;
            case 2 -> dungeonLord.getEvilness() < 15 && dungeonLord.getEvilness() >= 0;
            case 3 -> dungeonLord.getEvilness() < 14 && dungeonLord.getEvilness() >= 0;
            default -> false;
        };
    }
}
