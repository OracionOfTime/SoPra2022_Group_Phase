package de.unisaarland.cs.se.selab.biddingsquare.totalbid;

import de.unisaarland.cs.se.selab.actions.Action;
import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.io.IOException;
import java.util.Queue;

/**
 * MonsterBid class is used to evaluate all the monster Bids and assign and extract resources as per
 * needed
 */
public class MonsterBid extends TotalBid {

    public MonsterBid(final BroadcastEvents serverConnection) {
        super(BidType.MONSTER, serverConnection);
    }


    /**
     * function to evaluate the monster bids. It checks if the DungeonLord has the specific
     * resources or not. If it has then it extracts the required resources and assignes the desired
     * monster to it.
     *
     * @param ep         instance of the combat phase as this action is used in Combat phase
     * @param singleBids queue of single bids storing all the monster bids
     */
    @Override
    public void evaluateAllBids(final EvaluationPhase ep, final Queue<SingleBid> singleBids)
            throws IOException {

        //final int singleBidsLength = singleBids.size() + 1;

        //for (int slot = 1; slot < singleBidsLength; slot++) {
        int slot = 1;
        for (final SingleBid tempBid : singleBids) {
            if (slot == 4) {
                break;
            }

            acceptMonsterAction(ep, tempBid, slot);
            slot++;
        }
        ep.setExpectingMonster(false);

    }

    private void acceptMonsterAction(final EvaluationPhase ep,
            final SingleBid tempBid, final int slot) throws IOException {
        ep.setExpectingMonster(true);
        ep.setExpectingTunnel(false);

        if (!ep.getDungeonLordMap().containsKey(tempBid.getCommID())) {
            return;
        }

        final DungeonLord dl = ep.getDungeonLordMap().get(tempBid.getCommID());
        if (slot == 3) {

            if (dl.getFood() < 1) {
                return;
            }
            ep.getSc().broadcastFoodChanged(ep.getDungeonLordSortedList(), -1, dl.getPlayerID());
            dl.setFood(dl.getFood() - 1);
        }

        ep.getSc().sendSelectMonster(dl.getCommID());

        while (true) {
            final Action currentAction = ep.recieveAction(tempBid.getCommID());
            final boolean actionSuccessful = currentAction.exec(ep);
            if ((actionSuccessful && (currentAction.isHireMonster()
                    || currentAction.isEndTurn())) || currentAction.isLeave()) {
                // player hired monster
                // expecting monster flag set to false in hire monster action
                break;
            }
        }
    }


}
