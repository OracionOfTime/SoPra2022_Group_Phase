package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class EvaluateMonsterBids extends SystemTest {

    public EvaluateMonsterBids(final Class<?> subclass, final boolean fail) {
        super(subclass, fail);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4);
    }

    public void drawingAdventurers(final Set<Integer> socket, final int adv)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertAdventurerDrawn(s, adv);
        }
    }

    public void drawingMonsters(final Set<Integer> socket, final int mons) throws TimeoutException {
        for (final int s : socket) {
            this.assertMonsterDrawn(s, mons);
        }
    }

    public void adventurersArrive(final Set<Integer> socket, final int adv, final int player)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertAdventurerArrived(s, adv, player);
        }
    }

    public void goldChange(final Set<Integer> socket, final int amount, final int player)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertGoldChanged(s, amount, player);
        }
    }

    public void impsChange(final Set<Integer> socket, final int amount, final int player)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertImpsChanged(s, amount, player);
        }
    }

    public void retrieveBid(final Set<Integer> socket, final BidType bid, final int player)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertBidRetrieved(s, bid, player);
        }
    }

    public void monsterHired(final Set<Integer> socket, final int monster, final int player)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertMonsterHired(s, monster, player);
        }
    }

    public void drawingRooms(final Set<Integer> socket, final int room) throws TimeoutException {
        for (final int s : socket) {
            this.assertRoomDrawn(s, room);
        }
    }

    public void biddingStart(final Set<Integer> socket) throws TimeoutException {
        for (final int s : socket) {
            this.assertBiddingStarted(s);
        }
    }

    public void nextRound(final Set<Integer> socket, final int round) throws TimeoutException {
        for (final int s : socket) {
            this.assertNextRound(s, round);
        }
    }

    public void roomBuilt(final Set<Integer> socket, final int player, final int id, final int x,
            final int y)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertRoomBuilt(s, player, id, x, y);
        }
    }

    public void evilnessChange(final Set<Integer> socket, final int amount, final int player)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertEvilnessChanged(s, amount, player);
        }
    }

    public void foodChange(final Set<Integer> socket, final int amount, final int player)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertFoodChanged(s, amount, player);
        }
    }

    public void trapAcquired(final Set<Integer> socket, final int player, final int id)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertTrapAcquired(s, player, id);
        }
    }

    public void placeBid(final Set<Integer> socket, final BidType bid, final int player,
            final int slot)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertBidPlaced(s, bid, player, slot);
        }
    }

    public void gameStarted(final Set<Integer> socket) throws TimeoutException {
        for (final int s : socket) {
            this.assertGameStarted(s);
        }
    }

    public void player(final Set<Integer> socket, final String name, final int player)
            throws TimeoutException {
        for (final int s : socket) {
            this.assertPlayer(s, name, player);
        }
    }

    public void nextYear(final Set<Integer> socket, final int year) throws TimeoutException {
        for (final int s : socket) {
            this.assertNextYear(s, year);
        }
    }

    public void tunnelDug(final Set<Integer> socket, final int player,
            final int x, final int y) throws TimeoutException {
        for (final int s : socket) {
            this.assertTunnelDug(s, player, x, y);
        }
    }

    public void left(final Set<Integer> socket, final int player) throws
            TimeoutException {
        for (final int s : socket) {
            this.assertLeft(s, player);
        }
    }

    public void anyEvent(final Set<Integer> socket) throws
            TimeoutException {
        for (final int s : socket) {
            this.assertEvent(s);
        }
    }

    public void registration(final Set<Integer> socket, final String config)
            throws TimeoutException, AssertionError {

        this.sendRegister(1, "Player 1");
        this.assertConfig(1, config);
        this.sendRegister(2, "Player 2");
        this.assertConfig(2, config);
        this.sendRegister(3, "Player 3");
        this.assertConfig(3, config);
        this.sendRegister(4, "Player 4");
        this.assertConfig(4, config);
        //this.sendStartGame(3);
        gameStarted(socket);
        player(socket, "Player 1", 0);
        player(socket, "Player 2", 1);
        player(socket, "Player 3", 2);
        player(socket, "Player 4", 3);

        nextYear(socket, 1);

        nextRound(socket, 1);
        // Drawing
        // Adventurers:

        drawingAdventurers(socket, 29);
        drawingAdventurers(socket, 23);
        drawingAdventurers(socket, 2);
        drawingAdventurers(socket, 0);

        // Monsters

        drawingMonsters(socket, 23);
        drawingMonsters(socket, 13);
        drawingMonsters(socket, 9);

        // Rooms

        drawingRooms(socket, 5);
        drawingRooms(socket, 4);

        //bidding started

        biddingStart(socket);
    }

    public void firstBid(final Set<Integer> socket) throws TimeoutException, AssertionError {
        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);
        this.assertActNow(4);

        //first bids
        this.sendPlaceBid(1, BidType.MONSTER, 1);
        placeBid(socket, BidType.MONSTER, 0, 1);
        this.assertActNow(1);
        this.sendPlaceBid(2, BidType.MONSTER, 1);
        placeBid(socket, BidType.MONSTER, 1, 1);
        this.assertActNow(2);
        this.sendPlaceBid(3, BidType.FOOD, 1);
        placeBid(socket, BidType.FOOD, 2, 1);
        this.assertActNow(3);
        this.sendPlaceBid(4, BidType.MONSTER, 1);
        placeBid(socket, BidType.MONSTER, 3, 1);
        this.assertActNow(4);

        //second bids
        this.sendPlaceBid(1, BidType.FOOD, 2);
        placeBid(socket, BidType.FOOD, 0, 2);
        this.assertActNow(1);
        this.sendPlaceBid(2, BidType.GOLD, 2);
        placeBid(socket, BidType.GOLD, 1, 2);
        this.assertActNow(2);
        this.sendPlaceBid(3, BidType.GOLD, 2);
        placeBid(socket, BidType.GOLD, 2, 2);
        this.assertActNow(3);
        this.sendPlaceBid(4, BidType.TUNNEL, 2);
        placeBid(socket, BidType.TUNNEL, 3, 2);
        this.assertActNow(4);

    }

    public void firstEvaluation(final Set<Integer> socket)
            throws TimeoutException, AssertionError {

        // third bids
        this.sendPlaceBid(1, BidType.GOLD, 3);
        placeBid(socket, BidType.GOLD, 0, 3);
        this.sendPlaceBid(2, BidType.FOOD, 3);
        placeBid(socket, BidType.FOOD, 1, 3);
        this.sendPlaceBid(3, BidType.MONSTER, 3);
        placeBid(socket, BidType.MONSTER, 2, 3);
        this.sendPlaceBid(4, BidType.FOOD, 3);
        placeBid(socket, BidType.FOOD, 3, 3);

        // Evaluate FOOD

        goldChange(socket, -1, 2);
        foodChange(socket, 2, 2);

        evilnessChange(socket, 1, 0);
        foodChange(socket, 3, 0);

        evilnessChange(socket, 2, 1);
        foodChange(socket, 3, 1);
        goldChange(socket, 1, 1);

        // Evaluate NICENESS
        // Evaluate TUNNEL

        this.assertDigTunnel(4);
        this.assertActNow(4);
        this.sendDigTunnel(4, 3, 3);
        this.assertActionFailed(4);
        this.assertActNow(4);
        this.sendDigTunnel(4, 0, 1);
        impsChange(socket, -1, 3);
        tunnelDug(socket, 3, 0, 1);
        this.assertActNow(4);
        this.sendDigTunnel(4, 1, 0);
        impsChange(socket, -1, 3);
        tunnelDug(socket, 3, 1, 0);

        // Evaluate GOLD

        impsChange(socket, -1, 1);
        impsChange(socket, -1, 2);
        impsChange(socket, -1, 0);

        // Evaluate IMPS
        // Evaluate TRAPS
        // Evaluate MONSTER

        this.assertSelectMonster(1);
        this.assertActNow(1);
        //this.sendHireMonster(1, 2);
        //this.assertActionFailed(1);
        //this.assertActNow(1);
        this.sendHireMonster(1, 13);
        foodChange(socket, -1, 0);
        evilnessChange(socket, 1, 0);
        monsterHired(socket, 13, 0);

        this.assertSelectMonster(2);
        this.assertActNow(2);
        //this.sendHireMonster(2, 13);
        //this.assertActionFailed(2);
        //this.assertActNow(2);
        this.sendHireMonster(2, 23);
        monsterHired(socket, 23, 1);

        foodChange(socket, -1, 3);
        this.assertSelectMonster(4);
        this.assertActNow(4);
        //this.sendHireMonster(4, 13);
        //this.assertActionFailed(4);
        //this.assertActNow(4);
        //this.sendHireMonster(4, 23);
        //this.assertActionFailed(4);
        //this.assertActNow(4);
        this.sendHireMonster(4, 9);
        evilnessChange(socket, 3, 3);
        monsterHired(socket, 9, 3);

        // Evaluate ROOM
    }

    public void endOfFirstRound(final Set<Integer> socket)
            throws TimeoutException {

        //Bids retrieved (first round nothing locked)

        retrieveBid(socket, BidType.MONSTER, 0);
        retrieveBid(socket, BidType.MONSTER, 1);
        retrieveBid(socket, BidType.FOOD, 2);
        retrieveBid(socket, BidType.MONSTER, 3);

        //GOLD/Tunnel mining imps changed and gold changed (first round)
        // what happens if the same person bids on Gold and Tunnel
        impsChange(socket, 1, 0);
        goldChange(socket, 1, 0);
        impsChange(socket, 1, 1);
        goldChange(socket, 1, 1);
        impsChange(socket, 1, 2);
        goldChange(socket, 1, 2);
        impsChange(socket, 2, 3);

        // Room production (nothing)
        // Adventurers
        //try here again

        adventurersArrive(socket, 0, 2);
        adventurersArrive(socket, 2, 0);
        adventurersArrive(socket, 29, 1);
        adventurersArrive(socket, 23, 3);
    }

    public void startOfRound2(final Set<Integer> socket) throws TimeoutException, AssertionError {
        // ROUND=2
        nextRound(socket, 2);
        // Drawing
        // Adventurers:

        drawingAdventurers(socket, 18);
        drawingAdventurers(socket, 11);
        drawingAdventurers(socket, 20);
        drawingAdventurers(socket, 6);

        // Monsters

        drawingMonsters(socket, 7);
        drawingMonsters(socket, 22);
        drawingMonsters(socket, 1);

        // Rooms

        drawingRooms(socket, 8);
        drawingRooms(socket, 15);

        //bidding started

        biddingStart(socket);

        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);
        this.assertActNow(4);


    }

    public void secondBidding(final Set<Integer> socket) throws TimeoutException, AssertionError {
        // first bids
        this.sendPlaceBid(1, BidType.IMPS, 1);
        placeBid(socket, BidType.IMPS, 0, 1);
        this.assertActNow(1);
        this.sendPlaceBid(2, BidType.TUNNEL, 1);
        placeBid(socket, BidType.TUNNEL, 1, 1);
        this.assertActNow(2);
        this.sendPlaceBid(3, BidType.TUNNEL, 1);
        placeBid(socket, BidType.TUNNEL, 2, 1);
        this.assertActNow(3);
        this.sendPlaceBid(4, BidType.ROOM, 1);
        placeBid(socket, BidType.ROOM, 3, 1);
        this.assertActNow(4);

        // second bids
        this.sendPlaceBid(1, BidType.TUNNEL, 2);
        placeBid(socket, BidType.TUNNEL, 0, 2);
        this.assertActNow(1);
        this.sendPlaceBid(2, BidType.IMPS, 2);
        placeBid(socket, BidType.IMPS, 1, 2);
        this.assertActNow(2);
        this.sendPlaceBid(3, BidType.ROOM, 2);
        placeBid(socket, BidType.ROOM, 2, 2);
        this.assertActNow(3);
        this.sendPlaceBid(4, BidType.TRAP, 2);
        placeBid(socket, BidType.TRAP, 3, 2);
        this.assertActNow(4);

    }

    public void secondEval(final Set<Integer> socket) throws TimeoutException, AssertionError {

        // third bids
        this.sendPlaceBid(1, BidType.NICENESS, 3);
        placeBid(socket, BidType.NICENESS, 0, 3);
        this.sendPlaceBid(2, BidType.TRAP, 3);
        placeBid(socket, BidType.TRAP, 1, 3);
        this.sendPlaceBid(3, BidType.FOOD, 3);
        placeBid(socket, BidType.FOOD, 2, 3);
        this.sendPlaceBid(4, BidType.GOLD, 3);
        placeBid(socket, BidType.GOLD, 3, 3);
        // Evaluate FOOD

        goldChange(socket, -1, 2);
        foodChange(socket, +2, 2);

        // Evaluate NICENESS

        evilnessChange(socket, -1, 0);

        // Evaluate TUNNEL
        this.assertDigTunnel(2);
        this.assertActNow(2);
        this.sendDigTunnel(2, 0, 1);
        impsChange(socket, -1, 1);
        tunnelDug(socket, 1, 0, 1);
        this.assertActNow(2);
        this.sendDigTunnel(2, 1, 0);
        impsChange(socket, -1, 1);
        tunnelDug(socket, 1, 1, 0);

        this.assertDigTunnel(3);
        this.assertActNow(3);
        this.sendDigTunnel(3, 0, 1);
        impsChange(socket, -1, 2);
        tunnelDug(socket, 2, 0, 1);
        this.assertActNow(3);
        this.sendDigTunnel(3, 0, 2);
        impsChange(socket, -1, 2);
        tunnelDug(socket, 2, 0, 2);
        //this.assertActNow(3);
        //this.sendDigTunnel(3, 2, 3);
        //this.assertActionFailed(3);
        this.assertActNow(3);
        this.sendDigTunnel(3, 0, 3);
        impsChange(socket, -1, 2);
        tunnelDug(socket, 2, 0, 3);

        this.assertDigTunnel(1);
        this.assertActNow(1);
        this.sendDigTunnel(1, 1, 0);
        impsChange(socket, -1, 0);
        tunnelDug(socket, 0, 1, 0);
        this.assertActNow(1);
        this.sendDigTunnel(1, 2, 0);
        impsChange(socket, -1, 0);
        tunnelDug(socket, 0, 2, 0);
        this.assertActNow(1);
        this.sendDigTunnel(1, 0, 1);
        impsChange(socket, -1, 0);
        tunnelDug(socket, 0, 0, 1);
        this.assertActNow(1);
        //this.sendDigTunnel(1,0,2);
        //this.assertActionFailed(4);
        this.sendEndTurn(1);

        // Evaluate GOLD

        impsChange(socket, -2, 3);

        // Evaluate IMPS

        foodChange(socket, -1, 0);
        impsChange(socket, +1, 0);

        foodChange(socket, -2, 1);
        impsChange(socket, +2, 1);
    }

    public void secondEval2(final Set<Integer> socket) throws TimeoutException, AssertionError {
        // Evaluate TRAPS
        goldChange(socket, -1, 3);
        trapAcquired(socket, 3, 26);

        trapAcquired(socket, 1, 6);

        // Evaluate MONSTER
        // Evaluate ROOM

        goldChange(socket, -1, 3);
        this.assertPlaceRoom(4);
        this.assertActNow(4);
        //this.sendBuildRoom(4, 2, 2, 8);
        //this.assertActionFailed(4);
        //this.assertActNow(4);
        //this.sendBuildRoom(4, 0, 1, 8);
        //this.assertActionFailed(4);
        //this.assertActNow(4);
        this.sendEndTurn(4);

        goldChange(socket, -1, 2);
        this.assertPlaceRoom(3);
        this.assertActNow(3);
        //this.sendBuildRoom(3, 0, 1, 15);
        //this.assertActionFailed(3);
        //this.assertActNow(3);
        // TODO: CHANGE HERE x and y coordinates
        this.sendBuildRoom(3, 0, 3, 15);
        roomBuilt(socket, 2, 15, 0, 3);

    }

    public void endOfRound2(final Set<Integer> socket) throws TimeoutException {
        //Bids retrieved

        retrieveBid(socket, BidType.FOOD, 0);
        retrieveBid(socket, BidType.GOLD, 0);
        retrieveBid(socket, BidType.IMPS, 0);

        retrieveBid(socket, BidType.GOLD, 1);
        retrieveBid(socket, BidType.FOOD, 1);
        retrieveBid(socket, BidType.TUNNEL, 1);

        retrieveBid(socket, BidType.GOLD, 2);
        retrieveBid(socket, BidType.MONSTER, 2);
        retrieveBid(socket, BidType.TUNNEL, 2);

        retrieveBid(socket, BidType.TUNNEL, 3);
        retrieveBid(socket, BidType.FOOD, 3);
        retrieveBid(socket, BidType.ROOM, 3);

        //GOLD/Tunnel mining imps changed and gold changed (first round)

        impsChange(socket, +3, 0);
        impsChange(socket, +2, 1);
        impsChange(socket, +3, 2);
        impsChange(socket, +2, 3);
        goldChange(socket, +2, 3);

        // Room production (nothing)
        // Adventurers

        adventurersArrive(socket, 18, 2);
        adventurersArrive(socket, 11, 0);
        adventurersArrive(socket, 20, 1);
        adventurersArrive(socket, 6, 3);
    }

    public void startOfRound3(final Set<Integer> socket) throws TimeoutException {

        // ROUND=3
        nextRound(socket, 3);

        // Drawing
        // Adventurers:

        drawingAdventurers(socket, 9);
        drawingAdventurers(socket, 15);
        drawingAdventurers(socket, 26);
        drawingAdventurers(socket, 16);

        // Monsters

        drawingMonsters(socket, 14);
        drawingMonsters(socket, 3);
        drawingMonsters(socket, 20);

        // Rooms

        drawingRooms(socket, 0);
        drawingRooms(socket, 10);

        //bidding started

        biddingStart(socket);

        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);
        this.assertActNow(4);

    }

    public void round3(final Set<Integer> socket) throws TimeoutException, AssertionError {

        // first bids
        // if you remove 2 from the socket which ones are present ?
        this.sendPlaceBid(3, BidType.TUNNEL, 1);
        this.assertBidPlaced(3, BidType.TUNNEL, 2, 1);
        this.assertBidPlaced(4, BidType.TUNNEL, 2, 1);
        this.assertActNow(3);
        this.sendPlaceBid(4, BidType.ROOM, 1);
        this.assertBidPlaced(3, BidType.ROOM, 3, 1);
        this.assertBidPlaced(4, BidType.ROOM, 3, 1);
        this.assertActNow(4);
        //this.sendPlaceBid(4,BidType.ROOM,5);
        //this.assertActionFailed(4);
        //this.assertActNow(4);

        // second bids

        //this.sendPlaceBid(3, BidType.ROOM, 2);
        //this.assertActionFailed(3);
        //this.assertActNow(3);
        this.sendPlaceBid(3, BidType.IMPS, 2);
        this.assertBidPlaced(3, BidType.IMPS, 2, 2);
        this.assertBidPlaced(4, BidType.IMPS, 2, 2);
        this.assertActNow(3);
        this.sendPlaceBid(4, BidType.NICENESS, 2);
        this.assertBidPlaced(3, BidType.NICENESS, 3, 2);
        this.assertBidPlaced(4, BidType.NICENESS, 3, 2);
        this.assertActNow(4);

        // third bids
        this.sendPlaceBid(3, BidType.MONSTER, 3);
        this.assertBidPlaced(3, BidType.MONSTER, 2, 3);
        this.assertBidPlaced(4, BidType.MONSTER, 2, 3);
        //this.sendPlaceBid(4, BidType.NICENESS, 3);
        //this.assertActionFailed(4);
        //this.assertActNow(4);
        this.sendPlaceBid(4, BidType.MONSTER, 3);
        this.assertBidPlaced(3, BidType.MONSTER, 3, 3);
        this.assertBidPlaced(4, BidType.MONSTER, 3, 3);

        // Evaluate FOOD
        // Evaluate NICENESS

        this.assertEvilnessChanged(3, -1, 3);
        this.assertEvilnessChanged(4, -1, 3);

        // Evaluate TUNNEL

        this.assertDigTunnel(3);
        this.assertActNow(3);
        this.sendDigTunnel(3, 1, 1);
        this.assertImpsChanged(3, -1, 2);
        this.assertImpsChanged(4, -1, 2);
        this.assertTunnelDug(3, 2, 1, 1);
        this.assertTunnelDug(4, 2, 1, 1);
        this.assertActNow(3);
        // (1,2) test Action failed
        this.sendDigTunnel(3, 2, 1);
        this.assertImpsChanged(3, -1, 2);
        this.assertImpsChanged(4, -1, 2);
        this.assertTunnelDug(3, 2, 2, 1);
        this.assertTunnelDug(4, 2, 2, 1);

        // Evaluate GOLD
        // Evaluate IMPS

        this.assertFoodChanged(3, -1, 2);
        this.assertFoodChanged(4, -1, 2);
        this.assertImpsChanged(3, 1, 2);
        this.assertImpsChanged(4, 1, 2);

        // Evaluate MONSTER
        this.assertSelectMonster(3);
        this.assertActNow(3);
        this.sendHireMonster(3, 14);
        this.assertFoodChanged(3, -1, 2);
        this.assertFoodChanged(4, -1, 2);
        this.assertEvilnessChanged(3, 1, 2);
        this.assertEvilnessChanged(4, 1, 2);
        this.assertMonsterHired(3, 14, 2);
        this.assertMonsterHired(4, 14, 2);

    }

    public void endOfRound3(final Set<Integer> socket) throws TimeoutException {
        //Bids retrieved

        this.assertSelectMonster(4);
        this.assertActNow(4);
        this.sendHireMonster(4, 20);
        this.assertActionFailed(4);
        this.assertActNow(4);
        this.sendEndTurn(4);
        //this.assertFoodChanged(3,-3,3);
        //this.assertFoodChanged(4,-3,3);
        //this.assertMonsterHired(3,20,3);
        //this.assertMonsterHired(4,20,3);

        // Evaluate ROOM( maybe a problem)

        this.assertGoldChanged(3, -1, 3);
        this.assertGoldChanged(4, -1, 3);
        this.assertPlaceRoom(4);
        this.assertActNow(4);
        this.sendEndTurn(4);

        this.assertBidRetrieved(3, BidType.ROOM, 2);
        this.assertBidRetrieved(4, BidType.ROOM, 2);
        this.assertBidRetrieved(3, BidType.FOOD, 2);
        this.assertBidRetrieved(4, BidType.FOOD, 2);
        this.assertBidRetrieved(3, BidType.TUNNEL, 2);
        this.assertBidRetrieved(4, BidType.TUNNEL, 2);

        this.assertBidRetrieved(3, BidType.TRAP, 3);
        this.assertBidRetrieved(4, BidType.TRAP, 3);
        this.assertBidRetrieved(3, BidType.GOLD, 3);
        this.assertBidRetrieved(4, BidType.GOLD, 3);
        this.assertBidRetrieved(3, BidType.ROOM, 3);
        this.assertBidRetrieved(4, BidType.ROOM, 3);

        //GOLD/Tunnel mining imps changed and gold changed (first round)

        this.assertImpsChanged(3, +2, 2);
        this.assertImpsChanged(4, +2, 2);

        //Maybe add room production
        // Adventurers

        this.assertAdventurerArrived(3, 16, 2);
        this.assertAdventurerArrived(4, 16, 2);
        this.assertAdventurerArrived(3, 9, 3);
        this.assertAdventurerArrived(4, 9, 3);
    }

    public void startOfRound4(final Set<Integer> socket) throws TimeoutException, AssertionError {
        // ROUND=4

        this.assertNextRound(3, 4);
        this.assertNextRound(4, 4);

        // Drawing
        // Adventurers(None)
        // Monsters

        this.assertMonsterDrawn(3, 6);
        this.assertMonsterDrawn(4, 6);
        this.assertMonsterDrawn(3, 11);
        this.assertMonsterDrawn(4, 11);
        this.assertMonsterDrawn(3, 16);
        this.assertMonsterDrawn(4, 16);

        // Rooms

        this.assertRoomDrawn(3, 2);
        this.assertRoomDrawn(4, 2);
        this.assertRoomDrawn(3, 9);
        this.assertRoomDrawn(4, 9);

        //bidding started

        this.assertBiddingStarted(3);
        this.assertBiddingStarted(4);
        this.assertActNow(3);
        this.assertActNow(4);

    }

    public void endOfRound4(final Set<Integer> socket) throws TimeoutException {
        // first bids
        this.sendPlaceBid(3, BidType.ROOM, 1);
        this.assertBidPlaced(3, BidType.ROOM, 2, 1);
        this.assertBidPlaced(4, BidType.ROOM, 2, 1);
        this.assertActNow(3);
        this.sendPlaceBid(4, BidType.FOOD, 1);
        this.assertBidPlaced(3, BidType.FOOD, 3, 1);
        this.assertBidPlaced(4, BidType.FOOD, 3, 1);
        this.assertActNow(4);

        //second bids
        this.sendPlaceBid(3, BidType.TRAP, 2);
        this.assertBidPlaced(3, BidType.TRAP, 2, 2);
        this.assertBidPlaced(4, BidType.TRAP, 2, 2);
        this.assertActNow(3);
        this.sendPlaceBid(4, BidType.IMPS, 2);
        this.assertBidPlaced(3, BidType.IMPS, 3, 2);
        this.assertBidPlaced(4, BidType.IMPS, 3, 2);
        this.assertActNow(4);
        //third bids
        this.sendPlaceBid(3, BidType.TUNNEL, 3);
        this.assertBidPlaced(3, BidType.TUNNEL, 2, 3);
        this.assertBidPlaced(4, BidType.TUNNEL, 2, 3);
        this.sendPlaceBid(4, BidType.TRAP, 3);
        this.assertBidPlaced(3, BidType.TRAP, 3, 3);
        this.assertBidPlaced(4, BidType.TRAP, 3, 3);
        // Evaluate FOOD
        this.assertGoldChanged(3, -1, 3);
        this.assertGoldChanged(4, -1, 3);
        this.assertFoodChanged(3, +2, 3);
        this.assertFoodChanged(4, +2, 3);
        // Evaluate NICENESS
        // Evaluate TUNNEL
        this.assertDigTunnel(3);
        this.assertActNow(3);
        this.sendDigTunnel(3, 1, 3);
        this.assertImpsChanged(3, -1, 2);
        this.assertImpsChanged(4, -1, 2);
        this.assertTunnelDug(3, 2, 1, 3);
        this.assertTunnelDug(4, 2, 1, 3);
        this.assertActNow(3);
        this.sendDigTunnel(3, 2, 3);
        this.assertImpsChanged(3, -1, 2);
        this.assertImpsChanged(4, -1, 2);
        this.assertTunnelDug(3, 2, 2, 3);
        this.assertTunnelDug(4, 2, 2, 3);
        // Evaluate GOLD
        // Evaluate IMPS
        this.assertFoodChanged(3, -1, 3);
        this.assertFoodChanged(4, -1, 3);
        this.assertImpsChanged(3, 1, 3);
        this.assertImpsChanged(4, 1, 3);
        // Evaluate Trap
        this.assertGoldChanged(3, -1, 2);
        this.assertGoldChanged(4, -1, 2);
        this.assertTrapAcquired(3, 2, 19);
        this.assertTrapAcquired(4, 2, 19);

        this.assertTrapAcquired(3, 3, 5);
        this.assertTrapAcquired(4, 3, 5);

        //Bids retrieved

        this.assertBidRetrieved(3, BidType.IMPS, 2);
        this.assertBidRetrieved(4, BidType.IMPS, 2);
        this.assertBidRetrieved(3, BidType.MONSTER, 2);
        this.assertBidRetrieved(4, BidType.MONSTER, 2);
        this.assertBidRetrieved(3, BidType.ROOM, 2);
        this.assertBidRetrieved(4, BidType.ROOM, 2);

        this.assertBidRetrieved(3, BidType.NICENESS, 3);
        this.assertBidRetrieved(4, BidType.NICENESS, 3);
        this.assertBidRetrieved(3, BidType.MONSTER, 3);
        this.assertBidRetrieved(4, BidType.MONSTER, 3);
        this.assertBidRetrieved(3, BidType.FOOD, 3);
        this.assertBidRetrieved(4, BidType.FOOD, 3);

        //GOLD/Tunnel mining imps changed and gold changed (first round)

        this.assertImpsChanged(3, 2, 2);
        this.assertImpsChanged(4, 2, 2);

        // Room production (nothing)
        // Adventurers (no adventurers)
        this.assertNextRound(3, 1);
        this.assertNextRound(4, 1);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        /*
        final String config = createConfig();
        final Set<Integer> socket = createSockets();
        registration(socket, config);
        firstBid(socket);
        firstEvaluation(socket);
        endOfFirstRound(socket);
        startOfRound2(socket);
        secondBidding(socket);
        secondEval(socket);
        secondEval2(socket);
        endOfRound2(socket);
        startOfRound3(socket);
        round3(socket);
        this.sendLeave(1);
        socket.remove(1);
        left(socket, 0);
        this.sendLeave(2);
        socket.remove(2);
        left(socket, 1);
        endOfRound3(socket);
        startOfRound4(socket);
        endOfRound4(socket);
        //player3 leaves
        this.assertSetBattleGround(3);
        this.assertActNow(3);
        this.sendLeave(3);
        left(socket, 2);
        //player4 leaves
        this.assertSetBattleGround(4);
        this.assertActNow(4);
        this.sendLeave(4);
        left(socket, 3);
        */

    }

    /*public void evaluate() throws TimeoutException, AssertionError {
        final String config = createConfig();
        final Set<Integer> socket = createSockets();
        registration(socket, config);
        firstBid(socket);
        firstEvaluation(socket);
        endOfFirstRound(socket);
        secondBidding(socket);
        secondEval(socket);
        endOfRound2(socket);
        startOfRound3(socket);
        round3(socket);
        endOfRound3(socket);
        startOfRound4(socket);
        endOfRound4(socket);
    }*/
}
