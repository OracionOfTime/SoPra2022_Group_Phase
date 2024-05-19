package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.phases.CombatPhase;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;

public class EndTurn extends Action {

    public EndTurn(final int commID) {
        super(commID);
    }

    @Override
    public boolean isEndTurn() {
        return true;
    }

    @Override
    public boolean exec(final EvaluationPhase ep) {

        //Checking if EndTurn can be called during tunnel evaluation
        if (ep.isExpectingTunnel()) {
            ep.setExpectingTunnel(false);
            return true;
            /*
            final Queue<SingleBid> tunnelBidQueue = mappedBids.get(BidType.TUNNEL);
            //From the queue of placed bids(tunnel) of every dungeon-lord in bidding square
            //it is checked which slot was assigned to the dungeon-lord
            //by using a counter, then it is checked what the imps of the dungeon-lord
            //are doing.
            //Three 'if-cases' check if the dungeon-lord has enough imps to mine the tunnels:
            //If they have enough resources an ActionFailed is sent
            final boolean res = helperForExecutingTunnelAction(tunnelBidQueue, impArrayList);
            if (!res) {
                ep.getSc().sendActionFailed(getCommID(), "action expected");
                ep.getSc().sendActNow(getCommID());
            }
            return res;

             */
        }

        if (ep.isExpectingMonster()) {
            ep.setExpectingMonster(false);
            return true;
        }

        return true;
    }

    //since EndTurn can be called at any given moment in the Compat Phase, nothing
    //needs to be checked
    @Override
    public boolean exec(final CombatPhase cp) {
        return !cp.isExpectBattleGround();
    }


    /*private boolean helperForExecutingTunnelAction(
            final Queue<SingleBid> tunnelBidQueue, final List<Imp> impArrayList) {
        int counter = 0;
        for (int i = 0; i < tunnelBidQueue.size(); i++) {
            final SingleBid tunnelBid = tunnelBidQueue.element();
            if (tunnelBid.getCommID() == this.getCommID()) {
                final int counterForRestingImps = countRestingImps(impArrayList);
                final int counterForMiningImps = counterMiningImps(impArrayList);

                if (counter == 1 && impArrayList.size() >= 2) {
                    return !(helperForCheckingImps(counter, counterForMiningImps,
                            counterForRestingImps));
                } else if (counter == 2 && impArrayList.size() >= 3) {
                    return !(helperForCheckingImps(counter, counterForMiningImps,
                            counterForRestingImps));
                } else if (counter == 3 && impArrayList.size() >= 5) {
                    return !(helperForCheckingImps(counter, counterForMiningImps,
                            counterForRestingImps));
                }
            }
            counter++;
        }
        return true;
    }

     */

    /*
    private int counterMiningImps(final Iterable<Imp> impArrayList) {
        int counterForMiningImps = 0;
        for (final Imp imp : impArrayList) {
            if (imp.getAction() == ActionType.MINETUNNEL) {
                counterForMiningImps++;
            }
        }
        return counterForMiningImps;
    }

    private int countRestingImps(final Iterable<Imp> impArrayList) {
        int counterForRestingImps = 0;
        for (final Imp imp : impArrayList) {
            if (imp.getAction() == ActionType.RESTING) {
                counterForRestingImps++;
            }
        }
        return counterForRestingImps;
    }

     */

    /*
    //helper function to check every possible scenario in every slot of tunnel-bid,
    //since player gets asked with every assigned imp for next move
    private boolean helperForCheckingImps(final int counter, final int miningImps,
            final int restingImps) {
        if (counter == 1) {
            return scenarioChecker1(miningImps, restingImps);
        } else if (counter == 2) {
            return scenarioChecker2(miningImps, restingImps);
        } else if (counter == 3) {
            return scenarioChecker3(miningImps, restingImps);
        }
        return false;
    }

     */

    /*
    private boolean scenarioChecker3(final int miningImps, final int restingImps) {
        if (miningImps == 0 && restingImps >= 1) {
            return true;
        } else if (miningImps == 1 && restingImps >= 1) {
            return true;
        } else if (miningImps == 2 && restingImps >= 1) {
            return true;
        } else if (miningImps == 3 && restingImps >= 1) {
            return true;
        } else {
            return miningImps == 4 && restingImps >= 1;
        }
    }

    private boolean scenarioChecker2(final int miningImps, final int restingImps) {
        if (miningImps == 0 && restingImps >= 1) {
            return true;
        } else if (miningImps == 1 && restingImps >= 1) {
            return true;
        } else {
            return miningImps == 2 && restingImps >= 1;
        }
    }

    private boolean scenarioChecker1(final int miningImps, final int restingImps) {
        if (miningImps == 0 && restingImps >= 1) {
            return true;
        } else {
            return miningImps == 1 && restingImps >= 1;
        }
    }

     */
}

