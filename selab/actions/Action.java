package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.phases.BuildingPhase;
import de.unisaarland.cs.se.selab.phases.CombatPhase;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import de.unisaarland.cs.se.selab.phases.RegistrationPhase;
import java.io.IOException;

public abstract class Action {

    private int commID;

    public Action(final int commID) {
        this.commID = commID;
    }

    public int getCommID() {
        return commID;
    }

    public void setCommID(final int commID) {
        this.commID = commID;
    }

    public boolean isActivateRoom() {
        return false;
    }

    public boolean isStartGame() {
        return false;
    }

    public boolean isBuildRoom() {
        return false;
    }

    public boolean isHireMonster() {
        return false;
    }

    public boolean isRegister() {
        return false;
    }

    public boolean isEndTurn() {
        return false;
    }

    public boolean isLeave() {
        return false;
    }

    public boolean exec(final RegistrationPhase rp) throws IOException {
        rp.getSc().sendActionFailed(getCommID(), "wrong action");
        rp.getSc().sendActNow(getCommID());
        return false;
    }

    public boolean exec(final BuildingPhase bp) throws IOException {
        bp.getSc().sendActionFailed(getCommID(), "wrong action");
        bp.getSc().sendActNow(getCommID());
        return false;
    }

    public boolean exec(final EvaluationPhase ep) throws IOException {
        ep.getSc().sendActionFailed(getCommID(), "wrong action");
        ep.getSc().sendActNow(getCommID());
        return false;
    }

    public boolean exec(final CombatPhase cp) throws IOException {
        cp.getSc().sendActionFailed(getCommID(), "wrong action");
        cp.getSc().sendActNow(getCommID());
        return false;
    }

}
