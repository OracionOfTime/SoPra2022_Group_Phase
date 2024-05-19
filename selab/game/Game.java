package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.biddingsquare.BiddingSquare;
import de.unisaarland.cs.se.selab.dungeon.Adventurer;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Monster;
import de.unisaarland.cs.se.selab.dungeon.Room;
import de.unisaarland.cs.se.selab.dungeon.Trap;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.BuildingPhase;
import de.unisaarland.cs.se.selab.phases.CombatPhase;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import de.unisaarland.cs.se.selab.phases.Phase;
import de.unisaarland.cs.se.selab.phases.RegistrationPhase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Game {

    private final int initialFood;
    private final int initialGold;
    private final int initialImps;

    private int initialEvilness;
    private int currYear;
    private int maxYear;
    private int maxPlayers;
    private int rounds;
    private List<DungeonLord> dungeonLordsIndexed;
    private Queue<Monster> monsterPool;
    private Queue<Adventurer> adventurerPool;
    private Queue<Room> roomPool;
    private Queue<Trap> trapPool;
    private Phase currPhase;
    private int dungeonSideLength;
    private RegistrationPhase registrationPhase;
    private BuildingPhase buildingPhase;
    private EvaluationPhase evaluationPhase;
    private CombatPhase combatPhase;
    private Map<Integer, DungeonLord> dungeonLordMap;
    private String config;

    // Constructor does not get phases or dungeonlords


    public Game(final int currYear, final int maxYear, final int maxPlayers,
            final int rounds, final Queue<Monster> monsterPool,
            final Queue<Adventurer> adventurerPool, final Queue<Room> roomPool,
            final Queue<Trap> trapPool, final int dungeonSideLength,
            final String config, final int initialFood, final int initialGold,
            final int initialImps, final int initialEvilness) {
        this.currYear = currYear;
        this.maxYear = maxYear;
        this.maxPlayers = maxPlayers;
        this.rounds = rounds;
        this.monsterPool = monsterPool;
        this.adventurerPool = adventurerPool;
        this.roomPool = roomPool;
        this.trapPool = trapPool;

        this.dungeonSideLength = dungeonSideLength;

        this.config = config;
        this.initialFood = initialFood;
        this.initialGold = initialGold;
        this.initialImps = initialImps;
        this.initialEvilness = initialEvilness;
        this.dungeonLordMap = new HashMap<>();
        this.dungeonLordsIndexed = new ArrayList<>();
    }

    public String getConfig() {
        return this.config;
    }

    public void setConfig(final String config) {
        this.config = config;
    }

    public int getCurrYear() {
        return currYear;
    }

    public void setCurrYear(final int currYear) {
        this.currYear = currYear;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(final int maxYear) {
        this.maxYear = maxYear;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(final int rounds) {
        this.rounds = rounds;
    }

    public List<DungeonLord> getDungeonLordsIndexed() {
        return dungeonLordsIndexed;
    }

    public void setDungeonLordsIndexed(final List<DungeonLord> dungeonLordsIndexed) {
        this.dungeonLordsIndexed = dungeonLordsIndexed;
    }

    public Queue<Monster> getMonsterPool() {
        return monsterPool;
    }

    public void setMonsterPool(final Queue<Monster> monsterPool) {
        this.monsterPool = monsterPool;
    }

    public Queue<Adventurer> getAdventurerPool() {
        return adventurerPool;
    }

    public void setAdventurerPool(final Queue<Adventurer> adventurerPool) {
        this.adventurerPool = adventurerPool;
    }

    public Queue<Room> getRoomPool() {
        return roomPool;
    }

    public void setRoomPool(final Queue<Room> roomPool) {
        this.roomPool = roomPool;
    }

    public Queue<Trap> getTrapPool() {
        return trapPool;
    }

    public void setTrapPool(final Queue<Trap> trapPool) {
        this.trapPool = trapPool;
    }

    public Phase getCurrPhase() {
        return currPhase;
    }

    public void setCurrPhase(final Phase currPhase) {
        this.currPhase = currPhase;
    }

    public int getDungeonSideLength() {
        return dungeonSideLength;
    }

    public void setDungeonSideLength(final int dungeonSideLength) {
        this.dungeonSideLength = dungeonSideLength;
    }

    public RegistrationPhase getRegistrationPhase() {
        return registrationPhase;
    }

    public void setRegistrationPhase(final RegistrationPhase registrationPhase) {
        this.registrationPhase = registrationPhase;
    }

    public BuildingPhase getBuildingPhase() {
        return buildingPhase;
    }

    public void setBuildingPhase(final BuildingPhase buildingPhase) {
        this.buildingPhase = buildingPhase;
    }

    public EvaluationPhase getEvaluationPhase() {
        return evaluationPhase;
    }

    public void setEvaluationPhase(final EvaluationPhase evaluationPhase) {
        this.evaluationPhase = evaluationPhase;
    }

    public CombatPhase getCombatPhase() {
        return combatPhase;
    }

    public void setCombatPhase(final CombatPhase combatPhase) {
        this.combatPhase = combatPhase;
    }

    public Queue<Trap> getDungeonTrapPool() {
        return trapPool;
    }

    public void setDungeonTrapPool(final Queue<Trap> trapPool) {
        this.trapPool = trapPool;
    }

    public int getInitialFood() {
        return initialFood;
    }

    public int getInitialGold() {
        return initialGold;
    }

    public int getInitialImps() {
        return initialImps;
    }

    public int getInitialEvilness() {
        return initialEvilness;
    }

    public void setInitialEvilness(final int initialEvilness) {
        this.initialEvilness = initialEvilness;
    }

    public Map<Integer, DungeonLord> getDungeonLordMap() {
        return dungeonLordMap;
    }

    public void setDungeonLordMap(final Map<Integer, DungeonLord> dungeonLordMap) {
        this.dungeonLordMap = dungeonLordMap;
    }

    public void createRegistrationPhase(final BroadcastEvents serverConnection) {
        final RegistrationPhase regPhase = new RegistrationPhase(serverConnection,
                getDungeonLordsIndexed(), getDungeonLordMap(), getDungeonSideLength(),
                getConfig(), getMaxPlayers(), getInitialFood(), getInitialGold(), getInitialImps(),
                getInitialEvilness());
        this.setRegistrationPhase(regPhase);
    }

    public void createOtherPhases(final BroadcastEvents serverConnection) {
        final BuildingPhase buildingPhase = new BuildingPhase(serverConnection,
                getDungeonLordsIndexed(), getDungeonLordMap(), getMonsterPool(),
                getRoomPool());
        final BiddingSquare biddingSquare = new BiddingSquare(getDungeonLordMap(),
                new ArrayList<>(),
                new LinkedList<>(), new ArrayList<>(), 0, serverConnection);
        final EvaluationPhase evalPhase = new EvaluationPhase(serverConnection,
                getDungeonLordsIndexed(), getDungeonLordMap(), false, false,
                biddingSquare, new Adventurer[dungeonLordMap.size()],
                getTrapPool(), false);
        final CombatPhase combatPhase = new CombatPhase(serverConnection,
                getDungeonLordsIndexed(), getDungeonLordMap(), false, new ArrayList<>());
        this.setBuildingPhase(buildingPhase);
        this.setEvaluationPhase(evalPhase);
        this.setCombatPhase(combatPhase);
    }
}
