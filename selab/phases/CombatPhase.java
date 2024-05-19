package de.unisaarland.cs.se.selab.phases;

import static java.lang.Math.max;

import de.unisaarland.cs.se.selab.actions.Action;
import de.unisaarland.cs.se.selab.dungeon.Adventurer;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.dungeon.Trap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;


public class CombatPhase extends Phase {

    //The dungeonLord we're currently working with
    List<int[]> allDistances = new ArrayList<>();
    private boolean expectBattleGround;
    private int expectMonster; // 0
    private boolean expectTrap; // false
    private List<Optional<Tile>> possibleBattleGround;
    private DungeonLord currentDungeonLord;
    private boolean hasTurnEnded; // false
    private boolean trapIsFullyDefused;

    private boolean loseGame;

    public CombatPhase(final BroadcastEvents sc,
            final List<DungeonLord> dungeonLordSortedList,
            final Map<Integer, DungeonLord> dungeonLordMap, final boolean expectBattleGround,
            final List<Optional<Tile>> possibleBattleGround) {
        super(sc, dungeonLordSortedList, dungeonLordMap);
        this.expectBattleGround = expectBattleGround;
        this.possibleBattleGround = possibleBattleGround;
    }

    public boolean isTrapIsFullyDefused() {
        return trapIsFullyDefused;
    }

    public void setTrapIsFullyDefused(final boolean trapIsFullyDefused) {
        this.trapIsFullyDefused = trapIsFullyDefused;
    }

    public boolean isHasTurnEnded() {
        return hasTurnEnded;
    }

    public void setHasTurnEnded(final boolean hasTurnEnded) {
        this.hasTurnEnded = hasTurnEnded;
    }

    public boolean isExpectBattleGround() {
        return expectBattleGround;
    }

    public void setExpectBattleGround(final boolean expectBattleGround) {
        this.expectBattleGround = expectBattleGround;
    }

    public int numExpectMonster() {
        return expectMonster;
    }

    public boolean isExpectTrap() {
        return expectTrap;
    }

    public void setExpectTrap(final boolean expectTrap) {
        this.expectTrap = expectTrap;
    }

    public int getExpectMonster() {
        return this.expectMonster;
    }

    public void setExpectMonster(final int expectMonster) {
        this.expectMonster = expectMonster;
    }

    public List<Optional<Tile>> getPossibleBattleGround() {
        return possibleBattleGround;
    }

    public void setPossibleBattleGround(final List<Optional<Tile>> possibleBattleGround) {
        this.possibleBattleGround = possibleBattleGround;
    }

    public boolean isLoseGame() {
        return loseGame;
    }

    public void setLoseGame(final boolean loseGame) {
        this.loseGame = loseGame;
    }

    public DungeonLord getCurrentDungeonLord() {
        return currentDungeonLord;
    }

    public void setCurrentDungeonLord(final DungeonLord currentDungeonLord) {
        this.currentDungeonLord = currentDungeonLord;
    }

    private void setBattleGround() throws IOException {
        findShortestPath();
        setExpectBattleGround(true);
        // if there are no tiles and there are no prisoners go to the next year
        if (currentDungeonLord.getDungeon().getAdventurers().isEmpty()) {
            setLoseGame(true);
            return;
        }
        if (possibleBattleGround.isEmpty()) {
            return;
        }
        // Send to the client event setBattleground
        this.sc.sendSetBattleGround(currentDungeonLord.getCommID());
        // The client keeps choosing coordinates until he picks the right ones
        while (true) {
            final Action currAction = recieveAction(currentDungeonLord.getCommID());
            // if return of the action is true (means the action is valid) then leave loop
            if (currAction.exec(this)) {
                break;
            } else if (currAction.isLeave()) {
                setLoseGame(true);
                break;
            }
            // else send an action failed and ask act now again

        }
        // if he left then leave this function and leave the loop(go to next year)
    }

    private void defense() throws IOException {
        // check if Dungeon is empty && there are still prisoners then go to conquer
        if (possibleBattleGround.isEmpty() && !(currentDungeonLord.getDungeon() //why 2nd con?? TODO
                .getQueueOfImprisonedAdventurers().isEmpty())) {
            return;
        }
        if (currentDungeonLord.getDungeon().getBattleGroundCoordinates().isRoom()) {
            this.expectMonster = 2;
        } else {
            this.expectMonster = 1;
        }
        this.expectTrap = true;
        //check if the current player has rooms to begin with
        this.sc.sendDefendYourself(currentDungeonLord.getCommID());

        while (true) {
            final Action currentAction = recieveAction(this.currentDungeonLord.getCommID());
            currentAction.exec(this);
            if (currentAction.isEndTurn() || currentAction.isLeave()) {
                this.loseGame = currentAction.isLeave();
                break;
            }
            /*
            // handle the leave action
            if(currentAction.isAlreadyLeft()){
                setLoseGame(true);
                break;
            }*/
        }
        this.expectMonster = 0;
        this.expectTrap = false;

    }

    private void reduceAndDamage() {
        // No BG and prisoners
        if (possibleBattleGround.isEmpty() && !(currentDungeonLord.getDungeon()
                .getQueueOfImprisonedAdventurers().isEmpty())) {
            return;
        }
        final List<Adventurer> adventurers = currentDungeonLord.getDungeon().getAdventurers();
        final Queue<Adventurer> prisoners = currentDungeonLord.getDungeon()
                .getQueueOfImprisonedAdventurers();
        // Damage from Traps
        reduceAndDamageTrap(adventurers, prisoners);
        if (loseGame) {
            return;
        }
        // Damage from Monsters
        reduceAndDamageMonster(adventurers, prisoners);
        if (loseGame) {
            return;
        }
        // Fatigue damage
        reduceAndDamageFatigue(adventurers, prisoners);
        if (loseGame) {
            return;
        }
        currentDungeonLord.getDungeon().setAdventurers(adventurers);
        currentDungeonLord.getDungeon().setQueueOfImprisonedAdventurers(prisoners);

    }

    private void reduceAndDamageFatigue(final List<Adventurer> adventurers,
            final Queue<Adventurer> prisoners) {
        if (adventurers.isEmpty()) {
            setLoseGame(true);
            return;
        }
        // get the prisoners of the dungeon
        //iterate over a copy and remove adventurers if they are imprisoned
        final List<Adventurer> copyAdventurers = new ArrayList<>(adventurers);
        for (final Adventurer adv : copyAdventurers) {
            final int index = adventurers.indexOf(adv);
            final Adventurer currAdv = adventurers.get(index);
            currAdv.setCurrentHealthPoints(max(currAdv.getCurrentHealthPoints() - 2, 0));
            if (currAdv.getCurrentHealthPoints() == 0) {
                adventurers.remove(currAdv);
                prisoners.add(currAdv);
                this.sc.broadcastAdventurerImprisoned(dungeonLordSortedList, currAdv.getId());
            } else {
                this.sc.broadcastAdventurerDamaged(dungeonLordSortedList, currAdv.getId(), 2);
            }
        }
    }

    private void reduceAndDamageMonster(final List<Adventurer> adventurers,
            final Queue<Adventurer> prisoners) {
        //get Monsters in the dungeon
        final List<Monster> activeMonsters = currentDungeonLord.getDungeon().getActiveMonsters();
        for (final Monster monster : activeMonsters) {
            if (adventurers.isEmpty()) {
                setLoseGame(true);
                return;
            }
            final String strategy = monster.getAttackStrategy();
            final int monsterDamage = monster.getDamage();
            switch (strategy) {
                case "BASIC" -> {
                    reduceAndDamageMonsterBasic(adventurers, prisoners, monsterDamage);
                }
                case "TARGETED" -> {
                    reduceAndDamageMonsterTargeted(adventurers, prisoners, monster, monsterDamage);
                }
                case "MULTI" -> {
                    reduceAndDamageMonsterMulti(adventurers, prisoners, monsterDamage);
                }
                default -> {
                    return;
                }
            }
        }
    }

    private void reduceAndDamageMonsterMulti(final List<Adventurer> adventurers,
            final Queue<Adventurer> prisoners,
            final int monsterDamage) {
        //iterate over a copy and remove adventurers if they are imprisoned
        final List<Adventurer> copyAdventurers = new ArrayList<>(adventurers);
        for (final Adventurer adv : copyAdventurers) {
            final int index = adventurers.indexOf(adv);
            final Adventurer currAdv = adventurers.get(index);
            currAdv.setCurrentHealthPoints(max(currAdv
                    .getCurrentHealthPoints() - monsterDamage, 0));
            //Broadcast damaged or imprisoned
            if (currAdv.getCurrentHealthPoints() <= 0) {
                adventurers.remove(currAdv);
                prisoners.add(currAdv);
                this.sc.broadcastAdventurerImprisoned(dungeonLordSortedList,
                        currAdv.getId());
            } else {
                this.sc.broadcastAdventurerDamaged(dungeonLordSortedList, currAdv.getId(),
                        monsterDamage);
            }
        }
    }

    /**
     * @param adventurers   list of the 3 queued adventurers in front of each Dungeon
     * @param prisoners     Queue of the prisoners
     * @param monster       Each Monster passed by reduceAndDamageMonster
     * @param monsterDamage The monster's damage
     */
    private void reduceAndDamageMonsterTargeted(final List<Adventurer> adventurers,
            final Queue<Adventurer> prisoners,
            final Monster monster, final int monsterDamage) {
        final int target = monster.getTarget();
        // Damage to the targeted Adventurer
        adventurers.get(target).setCurrentHealthPoints(max(adventurers.get(target)
                .getCurrentHealthPoints() - monsterDamage, 0));
        // Broadcast damaged or imprisoned
        if (adventurers.get(target).getCurrentHealthPoints() <= 0) {
            final Adventurer currAdv = adventurers.get(target);
            adventurers.remove(currAdv);
            prisoners.add(currAdv);
            this.sc.broadcastAdventurerImprisoned(dungeonLordSortedList,
                    currAdv.getId());
        } else {
            this.sc.broadcastAdventurerDamaged(dungeonLordSortedList,
                    adventurers.get(target).getId(), monsterDamage);
        }
    }

    private void reduceAndDamageMonsterBasic(final List<Adventurer> adventurers,
            final Queue<Adventurer> prisoners,
            final int monsterDamage) {
        adventurers.get(0).setCurrentHealthPoints(max(adventurers.get(0)
                .getCurrentHealthPoints() - monsterDamage, 0));
        //Broadcast damaged or imprisoned
        if (adventurers.get(0).getCurrentHealthPoints() <= 0) {
            final Adventurer currAdv = adventurers.get(0);
            adventurers.remove(currAdv);
            prisoners.add(currAdv);
            this.sc.broadcastAdventurerImprisoned(dungeonLordSortedList, currAdv.getId());
        } else {
            this.sc.broadcastAdventurerDamaged(dungeonLordSortedList,
                    adventurers.get(0).getId(), monsterDamage);
        }
    }

    private void reduceAndDamageTrap(final List<Adventurer> adventurers,
            final Queue<Adventurer> prisoners) {
        setTrapIsFullyDefused(false);
        final Trap selectedTrap = currentDungeonLord.getDungeon().getActiveTrap();
        if (selectedTrap == null) {
            return;
        }
        int sumOfDiffuseValues = 0;
        // get the sum of all diffuse Values
        for (final Adventurer adv : adventurers) {
            final int diffuseValue = adv.getDiffuseValue();
            if (diffuseValue > 0) {
                sumOfDiffuseValues += diffuseValue;
            }
        }

        //get selected Trap
        int trapDamage = selectedTrap.getDamage();

        // Conditions for different Attack Strategies:
        //get the damage amount of the trap
        if (sumOfDiffuseValues >= trapDamage) {
            trapDamage = 0;
            setTrapIsFullyDefused(true);
        } else {
            trapDamage = trapDamage - sumOfDiffuseValues;
        }
        if (adventurers.isEmpty()) {
            setLoseGame(true);
            return;
        }
        final String trapStrategy = selectedTrap.getAttackStrategy();
        switch (trapStrategy) {
            //decreasing the health points of the first adventurer only
            case ("BASIC") -> {
                reduceAndDamageTrapBasic(adventurers, prisoners, trapDamage);
            }
            // decreasing the health points of all the adventurers
            case ("MULTI") -> {
                reduceAndDamageTrapMulti(adventurers, prisoners, sumOfDiffuseValues,
                        selectedTrap);
            }
            case ("TARGETED") -> {
                reduceAndDamageTrapTargeted(adventurers, prisoners, selectedTrap,
                        trapDamage);
            }
            default -> {
            }
        }
    }

    private void reduceAndDamageTrapTargeted(final List<Adventurer> adventurers,
            final Queue<Adventurer> prisoners,
            final Trap selectedTrap, final int trapDamage) {
        if (trapIsFullyDefused) {
            return;
        }
        final int target = selectedTrap.getTarget();
        adventurers.get(target).setCurrentHealthPoints(max(
                adventurers.get(target).getCurrentHealthPoints() - trapDamage, 0));
        //Broadcast damaged or imprisoned
        if (adventurers.get(target).getCurrentHealthPoints() <= 0) {
            final Adventurer currAdv = adventurers.get(target);
            prisoners.add(currAdv);
            adventurers.remove(currAdv);

            this.sc.broadcastAdventurerImprisoned(dungeonLordSortedList,
                    currAdv.getId());
        } else {

            this.sc.broadcastAdventurerDamaged(dungeonLordSortedList,
                    adventurers.get(target).getId(), trapDamage);
        }
    }

    private void reduceAndDamageTrapMulti(final List<Adventurer> adventurers,
            final Queue<Adventurer> prisoners,
            final int sumOfDiffuseValues, final Trap selectedTrap) {
        int temp = sumOfDiffuseValues;
        final List<Adventurer> copyAdventurers = new ArrayList<>(adventurers);
        for (final Adventurer adv : copyAdventurers) {
            final int index = adventurers.indexOf(adv);
            final Adventurer currAdv = adventurers.get(index);
            final int trapDamageEach = selectedTrap.getDamage();
            currAdv.setCurrentHealthPoints(currAdv.getCurrentHealthPoints() - max(0,
                    trapDamageEach - temp));

            //Broadcast damaged or imprisoned
            if (currAdv.getCurrentHealthPoints() <= 0) {
                adventurers.remove(currAdv);
                prisoners.add(currAdv);
                this.sc.broadcastAdventurerImprisoned(dungeonLordSortedList, currAdv.getId());
            } else if (temp < trapDamageEach) {
                temp = max(0, temp - trapDamageEach);
                this.sc.broadcastAdventurerDamaged(dungeonLordSortedList, currAdv.getId(),
                        trapDamageEach - temp);
            }
        }
    }


    private void reduceAndDamageTrapBasic(final List<Adventurer> adventurers,
            final Queue<Adventurer> prisoners,
            final int trapDamage) {
        if (trapIsFullyDefused) {
            return;
        }
        adventurers.get(0).setCurrentHealthPoints(
                max(adventurers.get(0).getCurrentHealthPoints() - trapDamage, 0));
        // broadcast Event ( if less than 0 then imprisoned else damaged)
        if (adventurers.get(0).getCurrentHealthPoints() <= 0) {
            final Adventurer currAdv = adventurers.get(0);
            prisoners.add(currAdv);
            adventurers.remove(currAdv);

            this.sc.broadcastAdventurerImprisoned(dungeonLordSortedList,
                    adventurers.get(0).getId());
        } else {
            this.sc.broadcastAdventurerDamaged(dungeonLordSortedList,
                    adventurers.get(0).getId(), trapDamage);
        }
    }

    //if all tiles are conquered and prisoners are present then a prisoner flees
    //each conquered tile and each fled adv results in evil-O-meter-1
    private void conquer() {
        final Queue<Adventurer> imprisonedAdventurers = currentDungeonLord.getDungeon()
                .getQueueOfImprisonedAdventurers();

        //check if all tiles have been conquered
        if (this.possibleBattleGround.isEmpty()) {
            //check if there are any imprisoned adventurer
            if (imprisonedAdventurers.size() > 0) {
                final Adventurer adventurerToFlee = imprisonedAdventurers.poll();
                this.getSc().broadcastAdventurerFled(this.getDungeonLordSortedList(),
                        adventurerToFlee.getId());
                evilnessMeter(currentDungeonLord);
            } else {
                setLoseGame(true);
            }
        } else {
            //conquer the tile
            final Tile conquerThisTile = this.currentDungeonLord.getDungeon()
                    .getBattleGroundCoordinates();
            conquerThisTile.setConquered(true);
            final Adventurer firstAdventurer = currentDungeonLord.getDungeon().getAdventurers()
                    .get(0);
            this.getSc().broadcastTunnelConquered(this.getDungeonLordSortedList(),
                    firstAdventurer.getId(), conquerThisTile.getRow(), conquerThisTile.getColumn());
            evilnessMeter(currentDungeonLord);

        }

    }

    //helper function to check evilness
    private void evilnessMeter(final DungeonLord currentDungeonLord) {
        if (currentDungeonLord.getEvilness() > 0) {
            currentDungeonLord.setEvilness(currentDungeonLord.getEvilness() - 1);
            this.getSc().broadcastEvilnessChanged(this.getDungeonLordSortedList(),
                    -1, currentDungeonLord.getPlayerID());
        }
    }

    private void heal() {
        if (possibleBattleGround.isEmpty()) {
            return;
        }
        final List<Adventurer> allAdventurers = this.currentDungeonLord.getDungeon()
                .getAdventurers();
        final List<Adventurer> allPriest = checkPriest(allAdventurers);
        //if there is a priest then heal
        if (!allPriest.isEmpty()) {
            for (final Adventurer adventurer : allPriest) {
                final Adventurer tempPriest = adventurer;
                adventurerCheck(allAdventurers, tempPriest);
            }

        }

    }

    private void adventurerCheck(final Iterable<Adventurer> allAdventurers,
            final Adventurer tempPriest) {
        for (final Adventurer tempAdventurer : allAdventurers) {
            final int healthCurrent = tempAdventurer.getCurrentHealthPoints();
            //check if adventurer needs to be healed and heal them
            if (tempAdventurer.getHealthPoints() > healthCurrent) {
                final int differenceHealthPoints =
                        tempAdventurer.getHealthPoints() - healthCurrent;
                if (differenceHealthPoints > tempPriest.getHealValue()) {
                    tempAdventurer.setCurrentHealthPoints(
                            healthCurrent + tempPriest.getHealValue());
                    tempPriest.setHealValue(0);
                    this.getSc().broadcastAdventurerHealed(this.dungeonLordSortedList,
                            differenceHealthPoints - tempPriest.getHealValue(),
                            tempPriest.getId(),
                            tempAdventurer.getId());
                    evilnessMeter(currentDungeonLord);
                    break;
                } else {
                    final int healValueLeft = tempPriest.getHealValue() - differenceHealthPoints;
                    tempAdventurer.setCurrentHealthPoints(
                            healthCurrent + differenceHealthPoints);
                    tempPriest.setHealValue(healValueLeft);
                    this.getSc().broadcastAdventurerHealed(this.dungeonLordSortedList,
                            differenceHealthPoints,
                            tempPriest.getId(), tempAdventurer.getId());
                    evilnessMeter(currentDungeonLord);
                }
            }
        }
    }


    //helper method to check if there is a priest
    private List<Adventurer> checkPriest(final Iterable<Adventurer> allAdventurers) {
        final List<Adventurer> allPriest = new ArrayList<>();
        for (final Adventurer tempAdventurer : allAdventurers) {

            if (tempAdventurer.getHealValue() > 0) {
                allPriest.add(tempAdventurer);
            }
        }
        return allPriest;
    }

    public void execute() throws IOException {
        final List<DungeonLord> realDLSortedList = this.getDungeonLordSortedList();
        final List<DungeonLord> copyDLSortedList = new ArrayList<>(realDLSortedList);
        for (final DungeonLord dlCopy : copyDLSortedList) {
            final int index = realDLSortedList.indexOf(dlCopy);
            final DungeonLord dlReal = realDLSortedList.get(index);
            setLoseGame(false);
            executeInnerLoop(dlReal);
        }
        // it empties the adventurers set in Dungeon, activates Monsters and traps from last year
        clearUpAfterEachYear();
    }

    private void clearUpAfterEachYear() {
        for (final DungeonLord dl : this.getDungeonLordSortedList()) {
            final List<Monster> monsters = dl.getDungeon().getMonsters();
            if (!monsters.isEmpty()) {
                for (final Monster mon : monsters) {
                    mon.setAvailable(true);
                }
            }
            final List<Trap> traps = dl.getDungeon().getTraps();
            if (!traps.isEmpty()) {
                for (final Trap tr : traps) {
                    tr.setAvailable(true);
                }
            }
            // Empty adventurers list
            dl.getDungeon().getAdventurers().clear();
        }
    }

    private void executeInnerLoop(final DungeonLord dl) throws IOException {
        for (int i = 0; i < 4; i++) {
            sc.broadcastNextRound(this.getDungeonLordSortedList(), i + 1);
            this.currentDungeonLord = dl;
            setBattleGround();
            if (loseGame) {
                break;
            }
            if (possibleBattleGround.isEmpty() && currentDungeonLord.getDungeon()
                    .getQueueOfImprisonedAdventurers().isEmpty()) {
                continue;
            }
            defense();
            if (loseGame) {
                break;
            }
            reduceAndDamage();
            if (loseGame) {
                break;
            }
            conquer();
            if (loseGame) {
                break;
            }
            heal();

            // clear up the active trap and the list of active monsters
            dl.getDungeon().getActiveMonsters().clear();
            dl.getDungeon().setActiveTrap(null);

        }
    }

    /* If the adventurers come from right, they can only move down,
     left and up */
    public void findShortestPath() {

        final List<List<Optional<Tile>>> tiles = currentDungeonLord.getDungeon().getTiles();
        //clear the list
        allDistances.clear();
        possibleBattleGround.clear();
        possibleTiles(tiles);
        //we now sort the list in ascending order
        Collections.sort(allDistances, new SortByDistance());

        final int minDistance = allDistances.get(0)[0];
        //adds tiles with minimum distance to the possible battleground
        for (final int[] allDistance : allDistances) {
            if (allDistance[0] == minDistance) {
                final int selectedRow = allDistance[1];
                final int selectedColumn = allDistance[2];

                if ((tiles.get(selectedRow).get(selectedColumn).isPresent())) {
                    possibleBattleGround.add(tiles.get(selectedRow).get(selectedColumn));
                }

            } else {
                break;
            }

        }

    }

    //Checks if the index is out of bounds
    private boolean withinBounds(final int row, final int column) {
        final int dungeonSideLength = currentDungeonLord.getDungeon().getTiles().size();
        return (row >= 0 & row < dungeonSideLength & column >= 0 & column < dungeonSideLength);
    }

    //finds all possible distances in round 1, regardless of how far they are

    private void possibleTiles(final List<List<Optional<Tile>>> tiles) {

        int currentDistance = 0;
        final int currentRow = 0;
        final int currentColumn = 0;

        //checks if the dungeon lord has any tiles to begin with (or is just a sore loser)
        if (tiles.get(currentRow).get(currentColumn).isPresent()) {
            if (tiles.get(currentRow).get(currentColumn).get().getIsConquered()) {
                //first tile has been conquered, we now move right and down

                currentDistance += 1;
                moveAround("down", currentDistance, currentRow + 1, currentColumn, tiles);
                moveAround("right", currentDistance, currentRow, currentColumn + 1, tiles);

            } else {
                //first tile has not been conquered, we simply add it
                final int[] found1 = {currentDistance, currentRow, currentColumn};
                this.allDistances.add(found1);
            }

        }


    }

    //if adventurers come from up, they can only move
    private void moveAround(final String lastMove, final int currentDistance, final int currentRow,
            final int currentColumn,
            final List<List<Optional<Tile>>> tiles) {
        if (withinBounds(currentRow, currentColumn)) {

            if ((tiles.get(currentRow).get(currentColumn).isPresent())) {

                movesIndirection(lastMove, currentDistance, currentRow, currentColumn, tiles);

            }

        }
    }

    private void movesIndirection(final String lastMove, final int currentDistance,
            final int currentRow, final int currentColumn,
            final List<List<Optional<Tile>>> tiles) {
        if ((tiles.get(currentRow).get(currentColumn).get().getIsConquered())) {
            // tile has been conquered, we now move down, right and left
            final int tempDistance = currentDistance + 1;
            if (!Objects.equals(lastMove, "left")) {
                moveAround("right", tempDistance, currentRow,
                        currentColumn + 1, tiles);
            }
            if (!Objects.equals(lastMove, "right")) {
                moveAround("left", tempDistance, currentRow,
                        currentColumn - 1, tiles);
            }
            if (!Objects.equals(lastMove, "down")) {
                moveAround("up", tempDistance, currentRow - 1,
                        currentColumn, tiles);
            }
            if (!Objects.equals(lastMove, "up")) {
                moveAround("down", tempDistance, currentRow + 1,
                        currentColumn, tiles);
            }
        } else {
            //first tile has not been conquered, we simply add it
            final int[] found1 = {currentDistance, currentRow, currentColumn};
            this.allDistances.add(found1);
        }
    }


    //Helper function to implement a comparator method to sort distances
    static class SortByDistance implements Comparator<int[]> {

        // Method
        // Sorting in ascending order of distance
        @Override
        public int compare(final int[] a, final int[] b) {

            return a[0] - b[0];
        }
    }
}

