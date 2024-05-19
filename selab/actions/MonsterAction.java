package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.phases.CombatPhase;
import java.util.List;
import java.util.Objects;

public class MonsterAction extends Action {

    private int monster;


    public MonsterAction(final int commID, final int monster) {
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
    public boolean exec(final CombatPhase cp) {
        final DungeonLord currentPlayer = cp.getDungeonLordMap().get(this.getCommID());
        final List<Monster> monsterList = currentPlayer.getDungeon().getMonsters();
        if (cp.numExpectMonster() > 0 && !monsterList.isEmpty()) {
            final Monster selectedMonster = monsterFound(monsterList);

            if (selectedMonster == null
                    || Objects.equals(selectedMonster.getAttackStrategy(), "TARGETED")
                    || (!selectedMonster.isAvailable())) {
                cp.getSc().sendActionFailed(getCommID(), "unable to place monster");
                return false;
            } else {
                selectedMonster.setAvailable(false);
                cp.setExpectMonster(cp.numExpectMonster() - 1);
                cp.getSc().broadcastMonsterPlaced(cp.getDungeonLordSortedList(),
                        this.monster, currentPlayer.getPlayerID());
                final List<Monster> activeMonster = currentPlayer.getDungeon().getActiveMonsters();
                activeMonster.add(selectedMonster);
                return true;
            }
        } else {
            cp.getSc().sendActionFailed(getCommID(), "unable to place monster");
            return false;
        }
    }


    //helper function to check if monster in list
    private Monster monsterFound(final Iterable<Monster> monsterList) {
        Monster selectedMonster = null;
        for (final Monster value : monsterList) {
            if (value.getId() == this.monster) {
                selectedMonster = value;
                break;
            }
        }
        return selectedMonster;
    }

    /*private boolean haveAvailableSpace(final DungeonLord currentDL) {
        if (currentDL.getDungeon().getBattleGroundCoordinates().isRoom()) {
            return (currentDL.getDungeon().getActiveMonsters().size() < 2);
        } else {
            return (currentDL.getDungeon().getActiveMonsters().size() < 1);
        }
    }*/
}
