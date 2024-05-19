package de.unisaarland.cs.se.selab.biddingsquare.totalbid;

import de.unisaarland.cs.se.selab.actions.Action;
import de.unisaarland.cs.se.selab.biddingsquare.SingleBid;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Imp;
import de.unisaarland.cs.se.selab.dungeon.Imp.ActionType;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class TunnelBid extends TotalBid {


    public TunnelBid(final BroadcastEvents serverConnection) {
        super(BidType.TUNNEL, serverConnection);
    }

    private static void setActionTypeTunnelAndSup(final Iterable<Imp> tempImps) {
        for (final Imp imp : tempImps) {
            if (imp.getAction() == ActionType.RESTING) {
                imp.setAction(ActionType.MINETUNNEL);
                break;
            }
        }

        for (final Imp imp : tempImps) {
            if (imp.getAction() == ActionType.RESTING) {
                imp.setAction(ActionType.SUPERVISE);
                break;
            }
        }
    }

    private static void setActionTypeOfImp(final Iterable<Imp> tempImps, final DungeonLord tempDL) {
        for (final Imp imp : tempImps) {
            if (imp.getAction() == ActionType.RESTING) {
                tempDL.setNUmberOfImps(tempDL.getNumberOfImps() - 1);
                imp.setAction(ActionType.MINETUNNEL);
                break;
            }
        }
    }

    @Override
    public void evaluateAllBids(final EvaluationPhase ep, final Queue<SingleBid> singleBids)
            throws IOException {

        int i = 1;
        for (final SingleBid tempBid : singleBids) {

            if (!ep.getDungeonLordMap().containsKey(tempBid.getCommID())) {
                i++;
                continue;
            }
            if (i >= 4) {
                break;
            }
            switch (i) {
                case 1 -> {
                    evalSlotOneTwo(ep, tempBid, 2);
                }
                case 2 -> {
                    evalSlotOneTwo(ep, tempBid, 3);
                }
                case 3 -> {
                    evalSlotThree(ep, tempBid);
                }
                default -> {

                }
            }
            i++;
        }
    }

    private void evalSlotOneTwo(final EvaluationPhase ep, final SingleBid tempBid,
            final int impMine) throws IOException {
        final List<Imp> tempImps = ep.getDungeonLordMap().get(tempBid.getCommID()).getImps();
        final DungeonLord tempDl = ep.getDungeonLordMap().get(tempBid.getCommID());
        final int availableImps = tempDl.getNumberOfImps();

        if (availableImps >= impMine) {
            assignMineImps(ep, tempBid, impMine, tempImps);
        } else {
            if (assignMineImps(ep, tempBid, availableImps, tempImps) == 0) {
                notEnoughImps(ep, tempBid); //TODO check again if invalid tests
            }
        }
    }

    private void evalSlotThree(final EvaluationPhase ep, final SingleBid tempBid)
            throws IOException {
        final List<Imp> tempImps = ep.getDungeonLordMap().get(tempBid.getCommID()).getImps();
        final DungeonLord tempDl = ep.getDungeonLordMap().get(tempBid.getCommID());
        final int availableImps = tempDl.getNumberOfImps();

        if (availableImps >= 5) {
            if (assignMineImps(ep, tempBid, 3, tempImps) == 0) {
                assignSupImp(ep, tempBid, tempImps);
            }
        } else if (availableImps == 4) {
            if (assignMineImps(ep, tempBid, 3, tempImps) == 0) {
                notEnoughImps(ep, tempBid);
            }
        } else {
            if (assignMineImps(ep, tempBid, availableImps, tempImps) == 0) {
                notEnoughImps(ep, tempBid);
            }
        }
    }

    private void notEnoughImps(final EvaluationPhase ep, final SingleBid tempBid) {
        Action action;
        do {
            action = ep.recieveAction(tempBid.getCommID());
            if (!action.isEndTurn()) {
                serverConnection.sendActionFailed(tempBid.getCommID(), "please send endTurn");
            }
        } while (!action.isEndTurn());
    }

    // Returns the leftover imps to check if player can still dug or if he send endturn before
    //all imps are placed
    private int assignMineImps(final EvaluationPhase ep, final SingleBid tempBid,
            final int impMine, final List<Imp> tempImps) throws IOException {
        serverConnection.sendDigTunnel(tempBid.getCommID());
        for (int i = 0; i < impMine; i++) {
            final Action action = ep.recieveAction(tempBid.getCommID());
            final DungeonLord tempDL = ep.getDungeonLordMap().get(tempBid.getCommID());

            ep.setExpectingTunnel(true);
            if (action.exec(ep) && !action.isActivateRoom() && !action.isEndTurn()) {
                setActionTypeOfImp(tempImps, tempDL);
            } else if (!action.isLeave()
                    && !action.isActivateRoom() && !action.isEndTurn()) {
                i--;
            } else if (action.isActivateRoom()) {
                i--;
            } else {
                return impMine - i;
            }
        }
        return 0;
    }

    private void assignSupImp(final EvaluationPhase ep, final SingleBid tempBid,
            final Iterable<Imp> tempImps) throws IOException {

        while (true) {
            final Action action = ep.recieveAction(tempBid.getCommID());
            ep.setExpectingTunnel(true);
            ep.setDigTunnel5Imps(true);
            if (action.exec(ep) && !action.isActivateRoom() && !action.isEndTurn()) {
                setActionTypeTunnelAndSup(tempImps);
                ep.getDungeonLordMap().get(tempBid.getCommID()).setNUmberOfImps(
                        ep.getDungeonLordMap().get(tempBid.getCommID()).getNumberOfImps() - 2);
                break;
            } else if (!action.isActivateRoom()) {
                break;
            }

        }
    }
}
