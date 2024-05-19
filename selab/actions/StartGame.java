package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.phases.RegistrationPhase;

/**
 * StartGame Action class is used in the RegistrationPhase to start the game. It basically
 * broadcasts the events to every player that game has started but the server changes the phase from
 * Registration to Building
 */
public class StartGame extends Action {

    public StartGame(final int commID) {
        super(commID);
    }

    @Override
    public boolean isStartGame() {
        return true;
    }


    /**
     * @param rp instance of the RegistrationPhase as this action is used in RegistrationPhase
     * @return true if the action was successful or false if action was unsuccessful
     */
    @Override
    public boolean exec(final RegistrationPhase rp) {

        // Only a registered player can start a game, so we check if the player already exists
        if (rp.getDungeonLordMap().containsKey(this.getCommID())) {
            rp.getSc().broadcastGameStarted(rp.getDungeonLordSortedList());
            return true;
        } else { // if an unregistered player tries to start a game
            rp.getSc().sendActionFailed(getCommID(), "you should register first");
            return false;
        }
    }
}
