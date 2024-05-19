package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.BidType;

public class ActionFactoryImplementation implements ActionFactory<Action> {

    @Override
    public Action createActivateRoom(final int commID, final int room) {
        return new ActivateRoom(commID, room);
    }

    @Override
    public Action createBattleGround(final int commID, final int x, final int y) {
        return new BattleGround(commID, x, y);
    }

    @Override
    public Action createBuildRoom(final int commID, final int x, final int y, final int roomID) {
        return new BuildRoom(commID, x, y, roomID);
    }

    @Override
    public Action createDigTunnel(final int commID, final int x, final int y) {
        return new DigTunnel(commID, x, y);
    }

    @Override
    public Action createEndTurn(final int commID) {
        return new EndTurn(commID);
    }

    @Override
    public Action createHireMonster(final int commID, final int monsterID) {
        return new HireMonster(commID, monsterID);
    }

    @Override
    public Action createLeave(final int commID) {
        return new Leave(commID);
    }

    @Override
    public Action createMonster(final int commID, final int monsterID) {
        return new MonsterAction(commID, monsterID);
    }

    @Override
    public Action createMonsterTargeted(final int commID, final int monsterID, final int position) {
        return new MonsterTargeted(commID, monsterID, position);
    }

    @Override
    public Action createPlaceBid(final int commID, final BidType bidType, final int bidNum) {
        return new PlaceBid(commID, bidType, bidNum);
    }

    @Override
    public Action createRegister(final int commID, final String player) {
        return new Register(commID, player);
    }

    @Override
    public Action createStartGame(final int commID) {
        return new StartGame(commID);
    }

    @Override
    public Action createTrap(final int commID, final int trapID) {
        return new Trap(commID, trapID);
    }
}
