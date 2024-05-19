package de.unisaarland.cs.se.selab.dungeon;

public class Monster {

    private boolean available;
    private int id;
    private int hunger;
    private int evilness;
    private int damage;
    private String attackStrategy;
    //private String name;

    private int target;

    public Monster(final boolean available, final int id, final int hunger, final int evilness,
            final int damage,
            final String attackStrategy/*, final String name*/) {
        this.available = available;
        this.id = id;
        this.hunger = hunger;
        this.evilness = evilness;
        this.damage = damage;
        this.attackStrategy = attackStrategy;
        //this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(final boolean available) {
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(final int hunger) {
        this.hunger = hunger;
    }

    public int getEvilness() {
        return evilness;
    }

    public void setEvilness(final int evilness) {
        this.evilness = evilness;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(final int damage) {
        this.damage = damage;
    }

    public String getAttackStrategy() {
        return attackStrategy;
    }

    public void setAttackStrategy(final String attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(final int target) {
        this.target = target;
    }
}
