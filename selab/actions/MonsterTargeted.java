package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.phases.CombatPhase;
import java.util.List;
import java.util.Objects;

public class MonsterTargeted extends Action {

    private int monster;
    private int position;


    public MonsterTargeted(final int commID, final int monster, final int position) {
        super(commID);
        this.monster = monster;
        this.position = position;
    }

    public int getMonster() {
        return monster;
    }

    public void setMonster(final int monster) {
        this.monster = monster;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(final int position) {
        this.position = position;
    }

    @Override
    public boolean exec(final CombatPhase cp) {
        final DungeonLord currentPlayer = cp.getDungeonLordMap().get(this.getCommID());
        final List<Monster> monsterList = currentPlayer.getDungeon().getMonsters();
        if (cp.numExpectMonster() > 0 && (!monsterList.isEmpty())) {
            final Monster selectedMonster = monsterFound(monsterList);
            final boolean adventurerInList = isAdventurerInList(currentPlayer);

            if (selectedMonster == null || !(adventurerInList)
                    || !Objects.equals(selectedMonster.getAttackStrategy(), "TARGETED")
                    || (!selectedMonster.isAvailable())) {
                cp.getSc().sendActionFailed(getCommID(), "can't execute monsterTargeted");
                return false;
            } else {
                selectedMonster.setAvailable(false);
                cp.setExpectMonster(cp.numExpectMonster() - 1);
                selectedMonster.setTarget(this.position);
                cp.getSc().broadcastMonsterPlaced(cp.getDungeonLordSortedList(),
                        this.monster, currentPlayer.getPlayerID());
                final List<Monster> activeMonster = currentPlayer.getDungeon().getActiveMonsters();
                activeMonster.add(selectedMonster);
                return true;
            }
        } else {
            cp.getSc().sendActionFailed(getCommID(), "monster not expected or unavailable");
            return false;
        }
    }

    /*private boolean haveAvailableSpace(final DungeonLord currentDL) {
        if (currentDL.getDungeon().getBattleGroundCoordinates().isRoom()) {
            return (currentDL.getDungeon().getActiveMonsters().size() < 2);
        } else {
            return (currentDL.getDungeon().getActiveMonsters().size() < 1);
        }
    }*/

    //helper function to check if adventurer in list
    private boolean isAdventurerInList(final DungeonLord dl) {

        return (dl.getDungeon().getAdventurers().size() >= this.position
                && this.position > 0);
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
}
