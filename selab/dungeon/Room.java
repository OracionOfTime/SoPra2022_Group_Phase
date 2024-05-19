package de.unisaarland.cs.se.selab.dungeon;

import de.unisaarland.cs.se.selab.dungeon.Imp.ActionType;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;

public class Room extends Tile {

    private final int activation;
    private final String placingRestriction;
    private final int food;
    private final int gold;
    private final int imps;
    private final int niceness;
    private final int id;
    private boolean currentProducing;

    public Room(final int column, final int row, final boolean conquered, final int activation,
            final String placingRestriction,
            final int food, final int gold, final int imps, final int niceness, final int id) {
        super(column, row, conquered);
        this.activation = activation;
        this.placingRestriction = placingRestriction;
        this.food = food;
        this.gold = gold;
        this.imps = imps;
        this.niceness = niceness;
        this.currentProducing = false;
        this.id = id;
    }


    public int getActivation() {
        return activation;
    }

    public String getPlacingRestriction() {
        return placingRestriction;
    }

    public int getFood() {
        return food;
    }

    public int getGold() {
        return gold;
    }

    public int getImps() {
        return imps;
    }

    public int getNiceness() {
        return niceness;
    }

    public int getId() {
        return id;
    }

    public boolean isCurrentProducing() {
        return currentProducing;
    }

    public void setCurrentProducing(final boolean currentProducing) {
        this.currentProducing = currentProducing;
    }

    @Override
    public boolean isRoom() {
        return true;
    }

    public void retrieveProducedGoods(final EvaluationPhase evalPhase, final int commID) {
        final DungeonLord player = evalPhase.getDungeonLordMap().get(commID);
        player.setNUmberOfImps(player.getNumberOfImps() + activation);
        evalPhase.getSc().broadcastImpsChanged(evalPhase.getDungeonLordSortedList(), activation,
                player.getPlayerID());
        if (this.getFood() > 0) {
            player.setFood(player.getFood() + this.getFood());
            evalPhase.getSc().broadcastFoodChanged(evalPhase.getDungeonLordSortedList(),
                    this.getFood(), player.getPlayerID());
        }
        if (this.getNiceness() > 0 && player.getEvilness() - this.getNiceness() > 0) {
            player.setEvilness(player.getEvilness() - this.getNiceness());
            evalPhase.getSc().broadcastEvilnessChanged(evalPhase.getDungeonLordSortedList(),
                    -this.getNiceness(), player.getPlayerID());
        }

        if (this.getGold() > 0) {
            player.setGold(player.getGold() + this.getGold());
            evalPhase.getSc().broadcastGoldChanged(evalPhase.getDungeonLordSortedList(),
                    this.getGold(), player.getPlayerID());
        }
        if (this.getImps() > 0) {
            player.setNUmberOfImps(player.getNumberOfImps() + this.getImps());
            for (int i = 0; i < this.getImps(); i++) {
                final Imp temp = new Imp(ActionType.RESTING);
                player.getImps().add(temp);
            }
            evalPhase.getSc().broadcastImpsChanged(evalPhase.getDungeonLordSortedList(),
                    this.getImps(), player.getPlayerID());
        }

        this.setCurrentProducing(false);
    }
}
