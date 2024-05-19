package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.phases.RegistrationPhase;

/**
 * Register Action class is used in the RegistrationPhase to register a player in the Game.
 */
public class Register extends Action {

    private String player;


    public Register(final int commID, final String player) {
        super(commID);
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(final String player) {
        this.player = player;
    }

    @Override
    public boolean isRegister() {
        return true;
    }

    /**
     * @param rp instance of the RegistrationPhase as this action is used in RegistrationPhase
     * @return true if the action was successful or false if action was unsuccessful
     */
    @Override
    public boolean exec(final RegistrationPhase rp) {

        // if the player is already registered we send action failed
        if (rp.getDungeonLordMap().containsKey(this.getCommID())) {
            rp.getSc().sendActionFailed(getCommID(), "you are already registered");
            rp.getSc().sendActNow(getCommID());
            return false;

            // if we already reached max players
        } else if (rp.getDungeonLordMap().size() == rp.getMaxPlayer()) {
            rp.getSc().sendActionFailed(getCommID(),
                    "maximum number of players already reached");
            return false;
        } else {
            rp.createDL(player, this.getCommID());
            //rp.getSc().sendActNow(getCommID()); //TODO check if this should be here
            return true;
        }
    }
}

