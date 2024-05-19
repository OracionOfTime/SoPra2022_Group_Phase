package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import java.util.List;


public class HireMonster extends Action {

    private int monster;

    public HireMonster(final int commID, final int monster) {
        super(commID);
        this.monster = monster;
    }


    public int getMonster() {
        return monster;
    }

    public void setMonster(final int monster) {
        this.monster = monster;
    }

    @Override
    public boolean isHireMonster() {
        return true;
    }

    @Override
    public boolean exec(final EvaluationPhase ep) {
        final DungeonLord currentPlayer = ep.getDungeonLordMap().get(this.getCommID());
        if (!ep.isExpectingMonster()) {
            ep.getSc().sendActionFailed(currentPlayer.getCommID(),
                    "not expecting monster :(");
            return false;
        }
        Monster selectedMonster = null;
        final List<Monster> monsterList = ep.getBiddingSquare().getMonsters();
        boolean monsterFound = false;
        for (final Monster currentMonster : monsterList) {
            if (currentMonster.getId() == this.getMonster()) {
                selectedMonster = currentMonster;
                monsterFound = true;
                break;
            }
        }
        if (!monsterFound) {
            ep.getSc().sendActionFailed(currentPlayer.getCommID(),
                    "monster not found :(");
            return false;
        }
        return checkResources(currentPlayer, ep, selectedMonster);

    }

    public boolean checkResources(final DungeonLord currentDL,
            final EvaluationPhase ep, final Monster selectedMonster) {
        if (currentDL.getFood() < selectedMonster.getHunger()
                || ((currentDL.getEvilness() + selectedMonster.getEvilness()) > 15)) {
            ep.getSc().sendActionFailed(currentDL.getCommID(),
                    "monster not found :(");
            return false;
        }
        evaluateBids(currentDL, ep, selectedMonster);
        return true;
    }

    public void evaluateBids(final DungeonLord currentDL,
            final EvaluationPhase ep, final Monster selectedMonster) {
        if (selectedMonster.getHunger() > 0) {
            evaluateFood(currentDL, ep, selectedMonster);
        }

        if (selectedMonster.getEvilness() > 0) {
            evaluateEvilness(currentDL, ep, selectedMonster);
        }

        currentDL.getDungeon().getMonsters().add(selectedMonster);
        ep.getBiddingSquare().getMonsters().remove(selectedMonster);

        ep.getSc().broadcastMonsterHired(ep.getDungeonLordSortedList(), selectedMonster.getId(),
                currentDL.getPlayerID());
        ep.setExpectingMonster(false);


    }

    public void evaluateFood(final DungeonLord currentDL,
            final EvaluationPhase ep, final Monster selectedMonster) {
        currentDL.setFood(currentDL.getFood() - selectedMonster.getHunger());
        ep.getSc().broadcastFoodChanged(ep.getDungeonLordSortedList(),
                -selectedMonster.getHunger(), currentDL.getPlayerID());
    }

    public void evaluateEvilness(final DungeonLord currentDL,
            final EvaluationPhase ep, final Monster selectedMonster) {
        currentDL.setEvilness(currentDL.getEvilness() + selectedMonster.getEvilness());
        ep.getSc().broadcastEvilnessChanged(ep.getDungeonLordSortedList(),
                selectedMonster.getEvilness(), currentDL.getPlayerID());
    }
}

