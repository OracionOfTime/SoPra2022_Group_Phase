package de.unisaarland.cs.se.selab.dungeon;


public class Imp {

    private ActionType action;

    public Imp(final ActionType action) {
        this.action = action;
    }

    public ActionType getAction() {
        return this.action;
    }

    public void setAction(final ActionType action) {
        this.action = action;
    }

    public enum ActionType {
        MINEGOLD,
        MINETUNNEL,
        SUPERVISE,
        RESTING,
        PRODUCING
    }

}
