package de.unisaarland.cs.se.selab.dungeon;

public abstract class Tile {

    private int column;
    private int row;
    private boolean isConquered;


    public Tile(final int column, final int row, final boolean conquered) {
        this.column = column;
        this.row = row;
        this.isConquered = conquered;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(final int column) {
        this.column = column;
    }


    public int getRow() {
        return row;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    public boolean getIsConquered() {
        return isConquered;
    }

    public void setConquered(final boolean conquered) {
        this.isConquered = conquered;
    }

    public boolean isRoom() {
        return false;
    }

    public boolean isTunnel() {
        return false;
    }
}
