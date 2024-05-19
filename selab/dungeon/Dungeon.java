package de.unisaarland.cs.se.selab.dungeon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class Dungeon {

    private List<Adventurer> adventurers;
    private List<List<Optional<Tile>>> tiles;
    private Queue<Adventurer> queueOfImprisonedAdv;
    private List<Monster> monsters;
    private List<Trap> traps;
    private List<Monster> activeMonsters;
    private List<Room> availableRooms;
    private Trap activeTrap;
    private Tile battleGroundCoordinates;

    public Dungeon(final List<List<Optional<Tile>>> tiles) {
        this.adventurers = new LinkedList<>();
        this.tiles = tiles;
        this.queueOfImprisonedAdv = new LinkedList<>();
        this.monsters = new ArrayList<>();
        this.traps = new ArrayList<>();
        this.activeMonsters = new ArrayList<>();
        this.availableRooms = new ArrayList<>();
        this.activeTrap = null;
    }

    public List<Adventurer> getAdventurers() {
        return adventurers;
    }

    public void setAdventurers(final List<Adventurer> adventurers) {
        this.adventurers = adventurers;
    }

    public List<List<Optional<Tile>>> getTiles() {
        return tiles;
    }

    public void setTiles(final List<List<Optional<Tile>>> tiles) {
        this.tiles = tiles;
    }


    public Queue<Adventurer> getQueueOfImprisonedAdventurers() {
        return queueOfImprisonedAdv;
    }

    public void setQueueOfImprisonedAdventurers(
            final Queue<Adventurer> queueOfImprisonedAdv) {
        this.queueOfImprisonedAdv = queueOfImprisonedAdv;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(final List<Monster> monsters) {
        this.monsters = monsters;
    }

    public List<Trap> getTraps() {
        return traps;
    }

    public void setTraps(final List<Trap> traps) {
        this.traps = traps;
    }

    public List<Monster> getActiveMonsters() {
        return activeMonsters;
    }

    public void setActiveMonsters(final List<Monster> activeMonsters) {
        this.activeMonsters = activeMonsters;
    }

    public List<Room> getAvailableRooms() {
        return this.availableRooms;
    }

    public void setAvailableRooms(final List<Room> availableRooms) {
        this.availableRooms = availableRooms;
    }

    public Trap getActiveTrap() {
        return activeTrap;
    }

    public void setActiveTrap(final Trap activeTrap) {
        this.activeTrap = activeTrap;
    }

    public Tile getBattleGroundCoordinates() {
        return battleGroundCoordinates;
    }

    public void setBattleGroundCoordinates(final Tile battleGroundCoordinates) {
        this.battleGroundCoordinates = battleGroundCoordinates;
    }
}
