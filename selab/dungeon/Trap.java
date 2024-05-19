package de.unisaarland.cs.se.selab.dungeon;

public class Trap {

    private int id;
    private boolean available;
    private int damage;
    private int target;
    private String attackStrategy;


    public Trap(final int id, final boolean available, final int damage, final int target,
            final String attackStrategy) {
        this.id = id;
        this.available = available;
        this.damage = damage;
        this.target = target;
        this.attackStrategy = attackStrategy;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(final boolean available) {
        this.available = available;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(final int damage) {
        this.damage = damage;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(final int target) {
        this.target = target;
    }

    public String getAttackStrategy() {
        return attackStrategy;
    }

    public void setAttackStrategy(final String attackStrategy) {
        this.attackStrategy = attackStrategy;
    }
}
