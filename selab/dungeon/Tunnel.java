package de.unisaarland.cs.se.selab.dungeon;

public class Tunnel extends Tile {


    public Tunnel(final int column, final int row, final boolean conquered) {
        super(column, row, conquered);
    }

    @Override
    public boolean isTunnel() {
        return true;
    }
}
