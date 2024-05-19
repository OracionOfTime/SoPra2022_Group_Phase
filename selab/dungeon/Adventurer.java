package de.unisaarland.cs.se.selab.dungeon;

public class Adventurer {

    private String name;
    private int healthPoints;

    private int currentHealthPoints;
    private int difficulty;
    private boolean imprisoned;
    private boolean charge;
    private int healValue;

    private int diffuseValue;

    private int id;

    public Adventurer(final String name, final int healthPoints, final int difficulty,
            final boolean imprisoned,
            final boolean charge,
            final int healValue, final int diffuseValue, final int id) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.currentHealthPoints = healthPoints;
        this.difficulty = difficulty;
        this.imprisoned = imprisoned;
        this.charge = charge;
        this.healValue = healValue;
        this.diffuseValue = diffuseValue;
        this.id = id;
    }

    public int getDiffuseValue() {
        return diffuseValue;
    }

    public void setDiffuseValue(final int diffuseValue) {
        this.diffuseValue = diffuseValue;
    }

    public int getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    public void setCurrentHealthPoints(final int currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(final int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(final int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isImprisoned() {
        return imprisoned;
    }

    public void setImprisoned(final boolean imprisoned) {
        this.imprisoned = imprisoned;
    }

    public boolean isCharge() {
        return charge;
    }

    public void setCharge(final boolean charge) {
        this.charge = charge;
    }

    public int getHealValue() {
        return healValue;
    }

    public void setHealValue(final int healValue) {
        this.healValue = healValue;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }


}
