package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.dungeon.Adventurer;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.dungeon.Trap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The GameFactory class is used to parse the config file and creates the pool of elements:
 * Adventurers, Monsters, Rooms, and Traps
 */

public class GameFactory {

    final String jsonString;
    final long randomSeed;
    private final List<Room> roomsPoolQueue;
    private final List<Monster> monsterPoolQueue;
    //    private void shuffle() {
    //
    //        Random randomObj = new Random(this.randomSeed);
    //
    //        Collections.shuffle(roomsPoolQueue, randomObj);
    //        Queue<Room> shuffledRoomsPoolQueue = new LinkedList<>(roomsPoolQueue);
    //
    //        Collections.shuffle(monsterPoolQueue, randomObj);
    //        Queue<Monster> shuffledMonsterPoolQueue = new LinkedList<>(monsterPoolQueue);
    //
    //        Collections.shuffle(adventurerPoolQueue, randomObj);
    //        Queue<Adventurer> shuffledAdventurerPoolQueue = new LinkedList<>(adventurerPoolQueue);
    //
    //        Collections.shuffle(trapsPoolQueue, randomObj);
    //        Queue<DungeonTrap> shuffledDungeonTrapPoolQueue = new LinkedList<>(trapsPoolQueue);
    //
    //
    //
    //    }
    private final List<Adventurer> adventurerPoolQueue;
    private final List<Trap> trapsPoolQueue;
    String jsonContent;
    private int maxPlayers;
    private int years;

    private int dungeonSideLength;

    private int initialFood;

    private int initialGold;

    private int initialImps;

    private int initialEvilness;

    public GameFactory(final long randomSeed, final String jsonStringPath) {
        this.monsterPoolQueue = new LinkedList<>();
        this.adventurerPoolQueue = new LinkedList<>();
        this.trapsPoolQueue = new LinkedList<>();
        this.roomsPoolQueue = new LinkedList<>();
        this.randomSeed = randomSeed;
        this.jsonString = jsonStringPath;

    }

    /**
     * Checks for valid maxplayer from config file
     *
     * @param maxPlayers value of max player from config file
     * @return true if valid
     */
    private boolean validMaxPlayers(final int maxPlayers) {
        return maxPlayers >= 1;
    }

    /**
     * Checks for valid years from config file
     *
     * @param years value of years from config file
     * @return true if valid
     */
    private boolean validYears(final int years) {
        return years >= 1;
    }

    /**
     * Checks for valid dungeonSideLength from config file
     *
     * @param dungeonSideLength value of dungeonSideLength from config file
     * @return true if valid
     */
    private boolean validDungeonSideLength(final int dungeonSideLength) {
        return dungeonSideLength >= 1 && dungeonSideLength <= 15;
    }

    /**
     * Checks for valid number of monster as mentioned in specification
     *
     * @param monsterJsonArrLength number of monsters from config file
     * @param years                number of years
     * @return true if valid or false if invalid
     */
    private boolean validMonsterJsonArrLength(final int monsterJsonArrLength, final int years) {

        return monsterJsonArrLength >= (3 * 4 * years);
    }

    /**
     * checks if all the properties are valid or not for monster based on specification
     *
     * @param monsterDamage         damage value of the monster
     * @param monsterAttackStrategy monster's attcak strategy
     * @param monsterHunger         monster's hunger value
     * @param monsterEvilness       monster's evilness
     * @return true if all properties are valid or false if invalid
     */
    private boolean validMonsterProperties(final int monsterDamage,
            final String monsterAttackStrategy,
            final int monsterHunger, final int monsterEvilness) {
        if (monsterDamage <= 0) {
            return false;
        }
        if (monsterHunger < 0) {
            return false;
        }
        if (monsterEvilness < 0) {
            return false;
        }
        return "MULTI".equals(monsterAttackStrategy) || "TARGETED".equals(monsterAttackStrategy)
                || "BASIC".equals(monsterAttackStrategy);
    }

    /**
     * Checks for valid number of monster as mentioned in specification
     *
     * @param adventurersJsonArrLength number of monsters from config file
     * @param years                    number of years
     * @param maxPlayers               number of player
     * @return true if valid or false if invalid
     */
    private boolean validAdventurersJsonArrLength(final int adventurersJsonArrLength,
            final int years,
            final int maxPlayers) {

        return adventurersJsonArrLength >= (maxPlayers * 3 * years);
    }

    /**
     * checks if all the properties are valid or not for adventure based on specification
     *
     * @param adventurerDifficulty   adventurer's difficulty value
     * @param adventurerHealthPoints adventurer's health value
     * @param adventurerHealValue    adventurer's heal value
     * @param adventurerDefuseValue  adventurer's defuse value
     * @return true if all properties are valid or false if invalid
     */
    private boolean validAdventurerProperties(final int adventurerDifficulty,
            final int adventurerHealthPoints,
            final int adventurerHealValue, final int adventurerDefuseValue) {
        if (adventurerDifficulty < 1 || adventurerDifficulty > 8) {
            return false;
        }
        if (adventurerHealthPoints < 1) {
            return false;
        }
        if (adventurerHealValue < 0) {
            return false;
        }
        return adventurerDefuseValue >= 0;
    }

    /**
     * Checks for valid number of monster as mentioned in specification
     *
     * @param trapsJsonArrLength number of monsters from config file
     * @param years              number of years
     * @return true if valid or false if invalid
     */
    private boolean validTrapsJsonArrLength(final int trapsJsonArrLength, final int years) {

        return trapsJsonArrLength >= (4 * 4 * years);
    }

    /**
     * checks if all the properties are valid or not for traps based on specification
     *
     * @param trapAttackStrategy trap's attack strategy
     * @param trapDamage         trap's damage
     * @param trapTarget         trap's target
     * @return true if all properties are valid or false if invalid
     */
    private boolean validTrapProperties(final String trapAttackStrategy, final int trapDamage,
            final int trapTarget) {
        if (!("MULTI".equals(trapAttackStrategy) || "TARGETED".equals(trapAttackStrategy)
                || "BASIC".equals(trapAttackStrategy))) {
            return false;
        }
        if (trapDamage < 1) {
            return false;
        }
        return trapTarget >= 0 && trapTarget <= 3;
    }

    /**
     * Checks for valid number of monster as mentioned in specification
     *
     * @param roomsJsonArrLength number of monsters from config file
     * @param years              number of years
     * @return true if valid or false if invalid
     */
    private boolean validRoomsJsonArrLength(final int roomsJsonArrLength, final int years) {

        return roomsJsonArrLength >= (2 * 4 * years);
    }

    /**
     * checks if all the properties are valid or not for room based on specification
     *
     * @param roomActivation  requirement for room activation
     * @param roomRestriction restrictions of the room
     * @return true if all properties are valid or false if invalid
     */
    private boolean validRoomProperties(final int roomActivation, final String roomRestriction) {

        if (!("UPPER_HALF".equals(roomRestriction) || "LOWER_HALF".equals(roomRestriction)
                || "OUTER_RING".equals(roomRestriction)
                || "INNER_RING".equals(roomRestriction))) {
            return false;
        }
        return roomActivation >= 1;
    }

    /**
     * checks for uniqueness and validity (<0) of list elements
     *
     * @param list list to be checked for uniqueness of elemnts
     * @return true if all the elements are unique and greater than 0 otherwise false
     */
    private boolean checkUniqueAndValidElements(final Collection<Integer> list) {
        final Set<Integer> set = new HashSet<>(list);

        if (set.size() != list.size()) {
            return false;
        }

        for (final int element : list) {
            if (element < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * creates a new monster object
     *
     * @param id             monster id
     * @param hunger         hunger value of monster
     * @param evilness       evilness of monster
     * @param damage         damage value of monster
     * @param attackStrategy attack strategy of monster
     * @return a new monster object
     */
    private Monster createMonster(final int id, final int hunger, final int evilness,
            final int damage,
            final String attackStrategy) {

        return new Monster(true, id, hunger, evilness, damage, attackStrategy);
    }

    /**
     * creates a new adventurer object
     *
     * @param name         name of adventurer
     * @param healthPoints healthPoints of adventurers
     * @param difficulty   difficulty of adventurer
     * @param charge       charge of adventurer
     * @param healValue    healValue of adventurer
     * @param id           id of adventurer
     * @param diffuseValue diffuseValue of adventurer
     * @return a new adventurer object
     */
    private Adventurer createAdventurer(final String name, final int healthPoints,
            final int difficulty,
            final boolean charge, final int healValue, final int id, final int diffuseValue) {

        return new Adventurer(name, healthPoints, difficulty, false, charge, healValue,
                diffuseValue, id);
    }

    /**
     * creates a new DungeonTrap object
     *
     * @param id             trap id
     * @param damage         damage made by trap
     * @param target         target of the trap
     * @param attackStrategy attcak strategy of trap
     * @return a new DungeonTrap object
     */
    private Trap createDungeonTrap(final int id, final int damage, final int target,
            final String attackStrategy) {

        return new Trap(id, true, damage, target, attackStrategy);
    }

    /**
     * creates a new Room object
     *
     * @param activation         activation cost of the room
     * @param placingRestriction where a room can be placed in dungeon
     * @param food               food to be produced
     * @param gold               gold to be produced
     * @param imps               imps to be produced
     * @param niceness           niceness to be produced
     * @return a new DungeonTrap object
     */
    private Room createRoom(final int activation, final String placingRestriction, final int food,
            final int gold, final int imps,
            final int niceness, final int id) {

        return new Room(-1, -1, false, activation, placingRestriction, food, gold, imps, niceness,
                id);
    }

    /**
     * json string which needs to be parsed and checked for the validity
     */
    public Game initializeGame() {

        final Random randomObj = new Random(this.randomSeed);

        Collections.shuffle(monsterPoolQueue, randomObj);
        final Queue<Monster> shuffledMonsterPool = new LinkedList<>(monsterPoolQueue);

        Collections.shuffle(adventurerPoolQueue, randomObj);
        final Queue<Adventurer> shuffledAdventurerPool = new LinkedList<>(adventurerPoolQueue);

        Collections.shuffle(trapsPoolQueue, randomObj);
        final Queue<Trap> shuffledTrapPool = new LinkedList<>(trapsPoolQueue);

        Collections.shuffle(roomsPoolQueue, randomObj);
        final Queue<Room> shuffledRoomsPool = new LinkedList<>(roomsPoolQueue);

        return new Game(1, years, maxPlayers, 0,
                shuffledMonsterPool, shuffledAdventurerPool,
                shuffledRoomsPool, shuffledTrapPool,
                dungeonSideLength, this.jsonContent, initialFood, initialGold, initialImps,
                initialEvilness);

    }


    /**
     * checks for validity of json string and create pool of objects
     *
     * @return true of json string is valid and false if json string id broken
     */
    //TODO: parseConfig complexity of 17 to fix
    public boolean parseConfig() throws IOException {

        final String contents = Files.readString(Paths.get(jsonString));

        this.jsonContent = contents;

        final JSONObject obj = new JSONObject(contents);

        if (!obj.has("maxPlayers")) {
            return false;
        }

        maxPlayers = obj.getInt("maxPlayers");
        if (!validMaxPlayers(maxPlayers)) {
            return false;
        }

        if (!obj.has("years")) {
            return false;
        }

        years = obj.getInt("years");
        if (!validYears(years)) {
            return false;
        }

        if (!obj.has("dungeonSideLength")) {
            return false;
        }

        dungeonSideLength = obj.getInt("dungeonSideLength");
        if (!validDungeonSideLength(dungeonSideLength)) {
            return false;
        }

        initialFood = 3;
        if (obj.has("initialFood")) {
            initialFood = obj.getInt("initialFood");
        }

        initialGold = 3;
        if (obj.has("initialGold")) {
            initialGold = obj.getInt("initialGold");
        }

        initialImps = 3;
        if (obj.has("initialImps")) {
            initialImps = obj.getInt("initialImps");
        }

        initialEvilness = 5;
        if (obj.has("initialEvilness")) {
            initialEvilness = obj.getInt("initialEvilness");
        }

        if (!checkInitialThings(initialFood, initialGold, initialImps, initialEvilness)) {
            return false;
        }

        return checkAndExtractPools(obj);
    }

    private boolean checkInitialThings(final int initialFood, final int initialGold,
            final int initialImps, final int initialEvilness) {
        if ((initialFood < 0) || (initialGold < 0) || (initialImps < 0)) {
            return false;
        }
        return (initialEvilness >= 0 && initialEvilness <= 15);

    }


    private boolean checkAndExtractPools(final JSONObject obj) {
        // FOR MONSTERS
        if (!obj.has("monsters")) {
            return false;
        }

        final JSONArray monsterJsonArr = obj.getJSONArray("monsters");
        if (!checkValidAndCreateMonsterPool(years, monsterJsonArr)) {
            return false;
        }

        if (!obj.has("adventurers")) {
            return false;
        }
        //  FOR ADVENTURERS
        final JSONArray adventurersJsonArr = obj.getJSONArray("adventurers");
        if (!checkValidAndCreateAdventurerPool(maxPlayers, years, adventurersJsonArr)) {
            return false;
        }

        if (!obj.has("traps")) {
            return false;
        }
        //  FOR DUNGEONTRAPS
        final JSONArray trapsJsonArr = obj.getJSONArray("traps");
        if (!checkValidAndCreateTrapsPool(years, trapsJsonArr)) {
            return false;
        }

        if (!obj.has("rooms")) {
            return false;
        }
        //  FOR ROOMS
        final JSONArray roomsJsonArr = obj.getJSONArray("rooms");
        return checkValidAndCreateRoomsPool(years, roomsJsonArr);
    }

    /**
     * check all room properties and create a pool of rooms
     *
     * @param years        years for which game will run
     * @param roomsJsonArr json array containing room's data
     * @return true if all the rooms properties were valid
     */

    //TODO: complexity of 22 to fix + use roomName
    private boolean checkValidAndCreateRoomsPool(final int years, final JSONArray roomsJsonArr) {
        if (!validRoomsJsonArrLength(roomsJsonArr.length(), years)) {
            return false;
        }
        // TODO: conversion of number type to int?
        return addToRoomPool(roomsJsonArr);
    }


    private boolean addToRoomPool(final JSONArray roomsJsonArr) {
        final List<Integer> roomsIdList = new ArrayList<>(roomsJsonArr.length());
        for (int i = 0; i < roomsJsonArr.length(); i++) {

            if (!checkMandatoryRoomPro(roomsJsonArr, i)) {
                return false;
            }

            final int roomActivation = roomsJsonArr.getJSONObject(i).getInt("activation");
            final String roomRestriction = roomsJsonArr.getJSONObject(i).getString("restriction");
            final int roomId = roomsJsonArr.getJSONObject(i).getInt("id");
            roomsIdList.add(roomId);

            int roomGold = 0;
            if (roomsJsonArr.getJSONObject(i).has("gold")) {
                roomGold = roomsJsonArr.getJSONObject(i).getInt("gold");
            }

            int roomImps = 0;
            if (roomsJsonArr.getJSONObject(i).has("imps")) {
                roomImps = roomsJsonArr.getJSONObject(i).getInt("imps");
            }

            int roomNiceness = 0;
            if (roomsJsonArr.getJSONObject(i).has("niceness")) {
                roomNiceness = roomsJsonArr.getJSONObject(i).getInt("niceness");
            }

            // TODO: check if all monsters have unique id
            if (!validRoomProperties(roomActivation, roomRestriction)) {
                return false;
            }

            if (!checkUniqueAndValidElements(roomsIdList)) {
                return false;
            }
            final int roomFood = getRoomFood(roomsJsonArr, i);
            // adding it to the pool
            roomsPoolQueue.add(
                    createRoom(roomActivation, roomRestriction, roomFood, roomGold, roomImps,
                            roomNiceness, roomId));
        }
        return true;
    }

    private int getRoomFood(final JSONArray roomsJsonArr, final int i) {
        int roomFood = 0;
        if (roomsJsonArr.getJSONObject(i).has("food")) {
            roomFood = roomsJsonArr.getJSONObject(i).getInt("food");
        }
        return roomFood;
    }

    private boolean checkMandatoryRoomPro(final JSONArray roomsJsonArr, final int i) {
        return (roomsJsonArr.getJSONObject(i).has("activation")
                && roomsJsonArr.getJSONObject(i).has("restriction")
                && roomsJsonArr.getJSONObject(i).has("id"));
    }

    /**
     * check all traps properties and create a pool of traps
     *
     * @param years        years for which game will run
     * @param trapsJsonArr json array containing traps's data
     * @return true if all the traps properties were valid
     */
    // TODO: Complexity of 16 to fix use TrapName
    private boolean checkValidAndCreateTrapsPool(final int years, final JSONArray trapsJsonArr) {
        if (!validTrapsJsonArrLength(trapsJsonArr.length(), years)) {
            return false;
        }

        return addToTrapPool(trapsJsonArr);
    }

    private boolean addToTrapPool(final JSONArray trapsJsonArr) {
        final List<Integer> trapsIdList = new ArrayList<>(trapsJsonArr.length());
        for (int i = 0; i < trapsJsonArr.length(); i++) {

            if (!(trapsJsonArr.getJSONObject(i).has("attackStrategy")
                    && trapsJsonArr.getJSONObject(i).has("damage")
                    && trapsJsonArr.getJSONObject(i).has("id"))) {
                return false;
            }

            final String trapAttackStrategy = trapsJsonArr.getJSONObject(i)
                    .getString("attackStrategy");

            //            if(!("TARGETED".equals(trapAttackStrategy)
            //            && trapsJsonArr.getJSONObject(i).has("target"))) {
            //                return false;
            //            }

            final int trapDamage = trapsJsonArr.getJSONObject(i).getInt("damage");
            final int trapId = trapsJsonArr.getJSONObject(i).getInt("id");
            trapsIdList.add(trapId);

            int trapTarget = 0;
            if (trapsJsonArr.getJSONObject(i).has("target")) {
                trapTarget = trapsJsonArr.getJSONObject(i).getInt("target");
            }

            if (!validTrapProperties(trapAttackStrategy, trapDamage, trapTarget)) {
                return false;
            }
            if (!checkUniqueAndValidElements(trapsIdList)) {
                return false;
            }

            // adding it to the pool
            trapsPoolQueue.add(createDungeonTrap(trapId, trapDamage, trapTarget,
                    trapAttackStrategy));
        }
        return true;
    }

    /**
     * check all adventurers properties and create a pool of adventurers
     *
     * @param maxPlayers         max player for which game will run
     * @param years              years for which game will run
     * @param adventurersJsonArr json array containing adventurer's data
     * @return true if all the adventurers properties were valid
     */
    // TODO: cognitive complexity of 20
    private boolean checkValidAndCreateAdventurerPool(final int maxPlayers, final int years,
            final JSONArray adventurersJsonArr) {
        if (!validAdventurersJsonArrLength(adventurersJsonArr.length(), years, maxPlayers)) {
            return false;
        }

        return addToAdventurePool(adventurersJsonArr);
    }

    private boolean addToAdventurePool(final JSONArray adventurersJsonArr) {
        final List<Integer> adventurersIdList = new ArrayList<>(adventurersJsonArr.length());
        for (int i = 0; i < adventurersJsonArr.length(); i++) {

            if (!(adventurersJsonArr.getJSONObject(i).has("id")
                    && adventurersJsonArr.getJSONObject(i).has("difficulty")
                    && adventurersJsonArr.getJSONObject(i).has("healthPoints"))) {
                return false;
            }

            final int adventurerId = adventurersJsonArr.getJSONObject(i).getInt("id");
            final int adventurerDifficulty = adventurersJsonArr.getJSONObject(i)
                    .getInt("difficulty");
            final int adventurerHealthPoints = adventurersJsonArr.getJSONObject(i)
                    .getInt("healthPoints");
            adventurersIdList.add(adventurerId);

            int adventurerHealValue = 0;
            if (adventurersJsonArr.getJSONObject(i).has("healValue")) {
                adventurerHealValue = adventurersJsonArr.getJSONObject(i)
                        .getInt("healValue");
            }

            int adventurerDefuseValue = 0;
            if (adventurersJsonArr.getJSONObject(i).has("defuseValue")) {
                adventurerDefuseValue = adventurersJsonArr.getJSONObject(i)
                        .getInt("defuseValue");
            }

            boolean adventurerCharge = false;
            if (adventurersJsonArr.getJSONObject(i).has("charge")) {
                adventurerCharge = adventurersJsonArr.getJSONObject(i).getBoolean("charge");
            }

            if (!validAdventurerProperties(adventurerDifficulty, adventurerHealthPoints,
                    adventurerHealValue, adventurerDefuseValue)) {
                return false;
            }

            if (!checkUniqueAndValidElements(adventurersIdList)) {
                return false;
            }
            final String adventurerName = getAdventurerName(adventurersJsonArr, i);

            // adding it to the pool
            adventurerPoolQueue.add(
                    createAdventurer(adventurerName, adventurerHealthPoints, adventurerDifficulty,
                            adventurerCharge, adventurerHealValue, adventurerId,
                            adventurerDefuseValue));

        }
        return true;
    }

    private String getAdventurerName(final JSONArray adventurersJsonArr, final int i) {
        String adventurerName = "";
        if (adventurersJsonArr.getJSONObject(i).has("name")) {
            adventurerName = adventurersJsonArr.getJSONObject(i).getString("name");
        }
        return adventurerName;
    }

    /**
     * check all monster properties and create a pool of monsters
     *
     * @param years          years for which game will run
     * @param monsterJsonArr json array containing monster's data
     * @return true if all the monster properties were valid
     */

    // TODO: cognitive complexity of 18
    private boolean checkValidAndCreateMonsterPool(final int years,
            final JSONArray monsterJsonArr) {
        if (!validMonsterJsonArrLength(monsterJsonArr.length(), years)) {
            return false;
        }

        final List<Integer> monsterIdList = new ArrayList<>(monsterJsonArr.length());
        for (int i = 0; i < monsterJsonArr.length(); i++) {

            if (!(monsterJsonArr.getJSONObject(i).has("id")
                    && monsterJsonArr.getJSONObject(i).has("damage")
                    && monsterJsonArr.getJSONObject(i).has("attackStrategy"))) {
                return false;
            }

            final int monsterId = monsterJsonArr.getJSONObject(i).getInt("id");
            final int monsterDamage = monsterJsonArr.getJSONObject(i).getInt("damage");
            final String monsterAttackStrategy = monsterJsonArr.getJSONObject(i)
                    .getString("attackStrategy");
            monsterIdList.add(monsterId);

            int monsterHunger = 0;
            if (monsterJsonArr.getJSONObject(i).has("hunger")) {
                monsterHunger = monsterJsonArr.getJSONObject(i).getInt("hunger");
            }

            int monsterEvilness = 0;
            if (monsterJsonArr.getJSONObject(i).has("evilness")) {
                monsterEvilness = monsterJsonArr.getJSONObject(i).getInt("evilness");
            }

            // TODO: check if all monsters have unique id
            if (!validMonsterProperties(monsterDamage, monsterAttackStrategy, monsterHunger,
                    monsterEvilness)) {
                return false;
            }

            if (!checkUniqueAndValidElements(monsterIdList)) {
                return false;
            }

            // adding it to the pool
            monsterPoolQueue.add(
                    createMonster(monsterId, monsterHunger, monsterEvilness, monsterDamage,
                            monsterAttackStrategy));

        }
        return true;
    }

}
