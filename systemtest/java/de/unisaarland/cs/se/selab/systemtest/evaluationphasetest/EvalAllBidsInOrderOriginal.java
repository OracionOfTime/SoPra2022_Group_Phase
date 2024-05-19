package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class EvalAllBidsInOrderOriginal extends SystemTest {

    public EvalAllBidsInOrderOriginal(final Class<?> sub, final boolean fail) {
        super(sub, fail);
    }

    public EvalAllBidsInOrderOriginal() {
        super(EvalAllBidsInOrderOriginal.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "configuration.json");
    }

    @Override
    public long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3);
    }

    public void registrationPhase() throws TimeoutException, AssertionError {
        final String config = createConfig();
        this.sendRegister(1, "Player1");
        this.assertConfig(1, config);
        this.sendRegister(2, "Player2");
        this.assertConfig(2, config);
        this.sendRegister(3, "Player3");
        this.assertConfig(3, config);

        this.sendStartGame(1);
        this.assertGameStarted(1);
        this.assertGameStarted(2);
        this.assertGameStarted(3);

        this.assertPlayer(1, "Player1", 0);
        this.assertPlayer(2, "Player1", 0);
        this.assertPlayer(3, "Player1", 0);

        this.assertPlayer(1, "Player2", 1);
        this.assertPlayer(2, "Player2", 1);
        this.assertPlayer(3, "Player2", 1);

        this.assertPlayer(1, "Player3", 2);
        this.assertPlayer(2, "Player3", 2);
        this.assertPlayer(3, "Player3", 2);
    }

    public void firstRoundDrawing() throws TimeoutException, AssertionError {
        this.assertNextYear(1, 1);
        this.assertNextYear(2, 1);
        this.assertNextYear(3, 1);

        this.assertNextRound(1, 1);
        this.assertNextRound(2, 1);
        this.assertNextRound(3, 1);

        this.assertAdventurerDrawn(1, 29);
        this.assertAdventurerDrawn(2, 29);
        this.assertAdventurerDrawn(3, 29);
        this.assertAdventurerDrawn(1, 23);
        this.assertAdventurerDrawn(2, 23);
        this.assertAdventurerDrawn(3, 23);
        this.assertAdventurerDrawn(1, 2);
        this.assertAdventurerDrawn(2, 2);
        this.assertAdventurerDrawn(3, 2);

        this.assertMonsterDrawn(1, 23);
        this.assertMonsterDrawn(2, 23);
        this.assertMonsterDrawn(3, 23);
        this.assertMonsterDrawn(1, 13);
        this.assertMonsterDrawn(2, 13);
        this.assertMonsterDrawn(3, 13);
        this.assertMonsterDrawn(1, 9);
        this.assertMonsterDrawn(2, 9);
        this.assertMonsterDrawn(3, 9);

        this.assertRoomDrawn(1, 5);
        this.assertRoomDrawn(2, 5);
        this.assertRoomDrawn(3, 5);
        this.assertRoomDrawn(1, 4);
        this.assertRoomDrawn(2, 4);
        this.assertRoomDrawn(3, 4);

        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);
        this.assertBiddingStarted(3);

        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);
    }

    public void biddingPhase1FirstBids() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.FOOD, 1);

        this.assertBidPlaced(1, BidType.FOOD, 0, 1);
        this.assertBidPlaced(2, BidType.FOOD, 0, 1);
        this.assertBidPlaced(3, BidType.FOOD, 0, 1);

        this.assertActNow(1);

        this.sendPlaceBid(2, BidType.FOOD, 1);

        this.assertBidPlaced(1, BidType.FOOD, 1, 1);
        this.assertBidPlaced(2, BidType.FOOD, 1, 1);
        this.assertBidPlaced(3, BidType.FOOD, 1, 1);

        this.assertActNow(2);

        this.sendPlaceBid(3, BidType.FOOD, 1);

        this.assertBidPlaced(1, BidType.FOOD, 2, 1);
        this.assertBidPlaced(2, BidType.FOOD, 2, 1);
        this.assertBidPlaced(3, BidType.FOOD, 2, 1);

        this.assertActNow(3);
    }

    public void biddingPhase1SecondBids() throws TimeoutException, AssertionError {
        sendPlaceBid(1, BidType.NICENESS, 2);

        this.assertBidPlaced(1, BidType.NICENESS, 0, 2);
        this.assertBidPlaced(2, BidType.NICENESS, 0, 2);
        this.assertBidPlaced(3, BidType.NICENESS, 0, 2);

        this.assertActNow(1);

        this.sendPlaceBid(2, BidType.NICENESS, 2);

        this.assertBidPlaced(1, BidType.NICENESS, 1, 2);
        this.assertBidPlaced(2, BidType.NICENESS, 1, 2);
        this.assertBidPlaced(3, BidType.NICENESS, 1, 2);

        this.assertActNow(2);

        this.sendPlaceBid(3, BidType.NICENESS, 2);

        this.assertBidPlaced(1, BidType.NICENESS, 2, 2);
        this.assertBidPlaced(2, BidType.NICENESS, 2, 2);
        this.assertBidPlaced(3, BidType.NICENESS, 2, 2);

        this.assertActNow(3);
    }

    public void biddingPhase1ThirdBids() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.TUNNEL, 3);

        this.assertBidPlaced(1, BidType.TUNNEL, 0, 3);
        this.assertBidPlaced(2, BidType.TUNNEL, 0, 3);
        this.assertBidPlaced(3, BidType.TUNNEL, 0, 3);

        this.sendPlaceBid(2, BidType.TUNNEL, 3);

        this.assertBidPlaced(1, BidType.TUNNEL, 1, 3);
        this.assertBidPlaced(2, BidType.TUNNEL, 1, 3);
        this.assertBidPlaced(3, BidType.TUNNEL, 1, 3);

        this.sendPlaceBid(3, BidType.TUNNEL, 3);

        this.assertBidPlaced(1, BidType.TUNNEL, 2, 3);
        this.assertBidPlaced(2, BidType.TUNNEL, 2, 3);
        this.assertBidPlaced(3, BidType.TUNNEL, 2, 3);
    }

    public void evalPhaseFoodBid1() throws TimeoutException, AssertionError {
        //FoodBid
        //player1
        this.assertGoldChanged(1, -1, 0);
        this.assertGoldChanged(2, -1, 0);
        this.assertGoldChanged(3, -1, 0);

        this.assertFoodChanged(1, 2, 0);
        this.assertFoodChanged(2, 2, 0);
        this.assertFoodChanged(3, 2, 0);

        //player2
        this.assertEvilnessChanged(1, 1, 1);
        this.assertEvilnessChanged(2, 1, 1);
        this.assertEvilnessChanged(3, 1, 1);

        this.assertFoodChanged(1, 3, 1);
        this.assertFoodChanged(2, 3, 1);
        this.assertFoodChanged(3, 3, 1);

        //player3
        this.assertEvilnessChanged(1, 2, 2);
        this.assertEvilnessChanged(2, 2, 2);
        this.assertEvilnessChanged(3, 2, 2);

        this.assertFoodChanged(1, 3, 2);
        this.assertFoodChanged(2, 3, 2);
        this.assertFoodChanged(3, 3, 2);

        this.assertGoldChanged(1, 1, 2);
        this.assertGoldChanged(2, 1, 2);
        this.assertGoldChanged(3, 1, 2);
    }

    public void evalPhaseNicenessBid1() throws TimeoutException, AssertionError {
        //NicenessBid
        //player1
        this.assertEvilnessChanged(1, -1, 0);
        this.assertEvilnessChanged(2, -1, 0);
        this.assertEvilnessChanged(3, -1, 0);

        //player2
        this.assertEvilnessChanged(1, -2, 1);
        this.assertEvilnessChanged(2, -2, 1);
        this.assertEvilnessChanged(3, -2, 1);

        //player3
        this.assertGoldChanged(1, -1, 2);
        this.assertGoldChanged(2, -1, 2);
        this.assertGoldChanged(3, -1, 2);

        this.assertEvilnessChanged(1, -2, 2);
        this.assertEvilnessChanged(2, -2, 2);
        this.assertEvilnessChanged(3, -2, 2);
    }

    public void evalPhaseTunnelBid1() throws TimeoutException, AssertionError {
        //TunnelBid
        //player1
        this.assertDigTunnel(1);
        this.assertActNow(1);
        this.sendDigTunnel(1, 1, 0);

        this.assertImpsChanged(1, -1, 0);
        this.assertImpsChanged(2, -1, 0);
        this.assertImpsChanged(3, -1, 0);

        this.assertTunnelDug(1, 0, 1, 0);
        this.assertTunnelDug(2, 0, 1, 0);
        this.assertTunnelDug(3, 0, 1, 0);

        this.assertActNow(1);
        this.sendDigTunnel(1, 0, 1);

        this.assertImpsChanged(1, -1, 0);
        this.assertImpsChanged(2, -1, 0);
        this.assertImpsChanged(3, -1, 0);

        this.assertTunnelDug(1, 0, 0, 1);
        this.assertTunnelDug(2, 0, 0, 1);
        this.assertTunnelDug(3, 0, 0, 1);

        //player2
        this.assertDigTunnel(2);
        this.assertActNow(2);
        this.sendDigTunnel(2, 1, 0);

        this.assertImpsChanged(1, -1, 1);
        this.assertImpsChanged(2, -1, 1);
        this.assertImpsChanged(3, -1, 1);

        this.assertTunnelDug(1, 1, 1, 0);
        this.assertTunnelDug(2, 1, 1, 0);
        this.assertTunnelDug(3, 1, 1, 0);

        this.assertActNow(2);
        this.sendDigTunnel(2, 0, 1);

        this.assertImpsChanged(1, -1, 1);
        this.assertImpsChanged(2, -1, 1);
        this.assertImpsChanged(3, -1, 1);

        this.assertTunnelDug(1, 1, 0, 1);
        this.assertTunnelDug(2, 1, 0, 1);
        this.assertTunnelDug(3, 1, 0, 1);

        this.assertActNow(2);
        this.sendDigTunnel(2, 2, 0);

        this.assertImpsChanged(1, -1, 1);
        this.assertImpsChanged(2, -1, 1);
        this.assertImpsChanged(3, -1, 1);

        this.assertTunnelDug(1, 1, 2, 0);
        this.assertTunnelDug(2, 1, 2, 0);
        this.assertTunnelDug(3, 1, 2, 0);

        //player3
        this.assertDigTunnel(3);
        this.assertActNow(3);
        this.sendDigTunnel(3, 0, 1);

        this.assertImpsChanged(1, -1, 2);
        this.assertImpsChanged(2, -1, 2);
        this.assertImpsChanged(3, -1, 2);

        this.assertTunnelDug(1, 2, 0, 1);
        this.assertTunnelDug(2, 2, 0, 1);
        this.assertTunnelDug(3, 2, 0, 1);

        this.assertActNow(3);
        this.sendDigTunnel(3, 1, 0);

        this.assertImpsChanged(1, -1, 2);
        this.assertImpsChanged(2, -1, 2);
        this.assertImpsChanged(3, -1, 2);

        this.assertTunnelDug(1, 2, 1, 0);
        this.assertTunnelDug(2, 2, 1, 0);
        this.assertTunnelDug(3, 2, 1, 0);

        this.assertActNow(3);
        this.sendDigTunnel(3, 2, 0);

        this.assertImpsChanged(1, -1, 2);
        this.assertImpsChanged(2, -1, 2);
        this.assertImpsChanged(3, -1, 2);

        this.assertTunnelDug(1, 2, 2, 0);
        this.assertTunnelDug(2, 2, 2, 0);
        this.assertTunnelDug(3, 2, 2, 0);

        this.assertActNow(3);
        this.sendEndTurn(3);
    }

    public void endOfRound1() throws TimeoutException, AssertionError {
        //retrieve Imps after Tunnel Mining
        this.assertBidRetrieved(1, BidType.FOOD, 0);
        this.assertBidRetrieved(2, BidType.FOOD, 0);
        this.assertBidRetrieved(3, BidType.FOOD, 0);

        this.assertBidRetrieved(1, BidType.FOOD, 1);
        this.assertBidRetrieved(2, BidType.FOOD, 1);
        this.assertBidRetrieved(3, BidType.FOOD, 1);

        this.assertBidRetrieved(1, BidType.FOOD, 2);
        this.assertBidRetrieved(2, BidType.FOOD, 2);
        this.assertBidRetrieved(3, BidType.FOOD, 2);

        this.assertImpsChanged(1, 2, 0);
        this.assertImpsChanged(2, 2, 0);
        this.assertImpsChanged(3, 2, 0);

        this.assertImpsChanged(1, 3, 1);
        this.assertImpsChanged(2, 3, 1);
        this.assertImpsChanged(3, 3, 1);

        this.assertImpsChanged(1, 3, 2);
        this.assertImpsChanged(2, 3, 2);
        this.assertImpsChanged(3, 3, 2);

        this.assertAdventurerArrived(1, 2, 0);
        this.assertAdventurerArrived(2, 2, 0);
        this.assertAdventurerArrived(3, 2, 0);

        this.assertAdventurerArrived(1, 29, 1);
        this.assertAdventurerArrived(2, 29, 1);
        this.assertAdventurerArrived(3, 29, 1);

        this.assertAdventurerArrived(1, 23, 2);
        this.assertAdventurerArrived(2, 23, 2);
        this.assertAdventurerArrived(3, 23, 2);
    }

    public void secondRoundDrawing() throws TimeoutException, AssertionError {
        this.assertNextRound(1, 2);
        this.assertNextRound(2, 2);
        this.assertNextRound(3, 2);

        this.assertAdventurerDrawn(1, 0);
        this.assertAdventurerDrawn(2, 0);
        this.assertAdventurerDrawn(3, 0);

        this.assertAdventurerDrawn(1, 18);
        this.assertAdventurerDrawn(2, 18);
        this.assertAdventurerDrawn(3, 18);

        this.assertAdventurerDrawn(1, 11);
        this.assertAdventurerDrawn(2, 11);
        this.assertAdventurerDrawn(3, 11);

        this.assertMonsterDrawn(1, 7);
        this.assertMonsterDrawn(2, 7);
        this.assertMonsterDrawn(3, 7);

        this.assertMonsterDrawn(1, 22);
        this.assertMonsterDrawn(2, 22);
        this.assertMonsterDrawn(3, 22);

        this.assertMonsterDrawn(1, 1);
        this.assertMonsterDrawn(2, 1);
        this.assertMonsterDrawn(3, 1);

        this.assertRoomDrawn(1, 8);
        this.assertRoomDrawn(2, 8);
        this.assertRoomDrawn(3, 8);

        this.assertRoomDrawn(1, 15);
        this.assertRoomDrawn(2, 15);
        this.assertRoomDrawn(3, 15);

        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);
        this.assertBiddingStarted(3);

        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);
    }

    public void biddingPhase2FirstBids() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.GOLD, 1);

        this.assertBidPlaced(1, BidType.GOLD, 0, 1);
        this.assertBidPlaced(2, BidType.GOLD, 0, 1);
        this.assertBidPlaced(3, BidType.GOLD, 0, 1);

        this.assertActNow(1);

        this.sendPlaceBid(2, BidType.MONSTER, 1);

        this.assertBidPlaced(1, BidType.MONSTER, 1, 1);
        this.assertBidPlaced(2, BidType.MONSTER, 1, 1);
        this.assertBidPlaced(3, BidType.MONSTER, 1, 1);

        this.assertActNow(2);

        this.sendPlaceBid(3, BidType.MONSTER, 1);

        this.assertBidPlaced(1, BidType.MONSTER, 2, 1);
        this.assertBidPlaced(2, BidType.MONSTER, 2, 1);
        this.assertBidPlaced(3, BidType.MONSTER, 2, 1);

        this.assertActNow(3);
    }

    public void biddingPhase2SecondBids() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.IMPS, 2);

        this.assertBidPlaced(1, BidType.IMPS, 0, 2);
        this.assertBidPlaced(2, BidType.IMPS, 0, 2);
        this.assertBidPlaced(3, BidType.IMPS, 0, 2);

        this.assertActNow(1);

        this.sendPlaceBid(2, BidType.ROOM, 2);

        this.assertBidPlaced(1, BidType.ROOM, 1, 2);
        this.assertBidPlaced(2, BidType.ROOM, 1, 2);
        this.assertBidPlaced(3, BidType.ROOM, 1, 2);

        this.assertActNow(2);

        this.sendPlaceBid(3, BidType.FOOD, 2);

        this.assertBidPlaced(1, BidType.FOOD, 2, 2);
        this.assertBidPlaced(2, BidType.FOOD, 2, 2);
        this.assertBidPlaced(3, BidType.FOOD, 2, 2);

        this.assertActNow(3);
    }

    public void biddingPhase2ThirdBids() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.TRAP, 3);

        this.assertBidPlaced(1, BidType.TRAP, 0, 3);
        this.assertBidPlaced(2, BidType.TRAP, 0, 3);
        this.assertBidPlaced(3, BidType.TRAP, 0, 3);

        this.sendPlaceBid(2, BidType.TRAP, 3);

        this.assertBidPlaced(1, BidType.TRAP, 1, 3);
        this.assertBidPlaced(2, BidType.TRAP, 1, 3);
        this.assertBidPlaced(3, BidType.TRAP, 1, 3);

        this.sendPlaceBid(3, BidType.GOLD, 3);

        this.assertBidPlaced(1, BidType.GOLD, 2, 3);
        this.assertBidPlaced(2, BidType.GOLD, 2, 3);
        this.assertBidPlaced(3, BidType.GOLD, 2, 3);
    }

    public void evalPhaseFoodBid2() throws TimeoutException, AssertionError {
        //player3
        this.assertGoldChanged(1, -1, 2);
        this.assertGoldChanged(2, -1, 2);
        this.assertGoldChanged(3, -1, 2);

        this.assertFoodChanged(1, 2, 2);
        this.assertFoodChanged(2, 2, 2);
        this.assertFoodChanged(3, 2, 2);
    }

    public void evalPhaseGoldBid2() throws TimeoutException, AssertionError {
        //player1
        this.assertImpsChanged(1, -2, 0);
        this.assertImpsChanged(2, -2, 0);
        this.assertImpsChanged(3, -2, 0);

        //player3
        this.assertImpsChanged(1, -3, 2);
        this.assertImpsChanged(2, -3, 2);
        this.assertImpsChanged(3, -3, 2);
    }

    public void evalPhaseImpsBid2() throws TimeoutException, AssertionError {
        //player1
        this.assertFoodChanged(1, -1, 0);
        this.assertFoodChanged(2, -1, 0);
        this.assertFoodChanged(3, -1, 0);

        this.assertImpsChanged(1, 1, 0);
        this.assertImpsChanged(2, 1, 0);
        this.assertImpsChanged(3, 1, 0);
    }

    public void evalPhaseTrapBid2() throws TimeoutException, AssertionError {
        //player2
        this.assertGoldChanged(1, -1, 1);
        this.assertGoldChanged(2, -1, 1);
        this.assertGoldChanged(3, -1, 1);

        this.assertTrapAcquired(1, 1, 26);
        this.assertTrapAcquired(2, 1, 26);
        this.assertTrapAcquired(3, 1, 26);

        //player1
        this.assertTrapAcquired(1, 0, 6);
        this.assertTrapAcquired(2, 0, 6);
        this.assertTrapAcquired(3, 0, 6);
    }

    public void evalPhaseMonsterBid2() throws TimeoutException, AssertionError {
        //player2
        this.assertSelectMonster(2);
        this.assertActNow(2);
        this.sendHireMonster(2, 22);

        this.assertFoodChanged(1, -1, 1);
        this.assertFoodChanged(2, -1, 1);
        this.assertFoodChanged(3, -1, 1);

        this.assertEvilnessChanged(1, 1, 1);
        this.assertEvilnessChanged(2, 1, 1);
        this.assertEvilnessChanged(3, 1, 1);

        this.assertMonsterHired(1, 22, 1);
        this.assertMonsterHired(2, 22, 1);
        this.assertMonsterHired(3, 22, 1);

        //player3
        this.assertSelectMonster(3);
        this.assertActNow(3);
        this.sendHireMonster(3, 7);

        this.assertMonsterHired(1, 7, 2);
        this.assertMonsterHired(2, 7, 2);
        this.assertMonsterHired(3, 7, 2);
    }

    public void evalPhaseRoomBid2() throws TimeoutException, AssertionError {
        //player2
        this.assertGoldChanged(1, -1, 1);
        this.assertGoldChanged(2, -1, 1);
        this.assertGoldChanged(3, -1, 1);

        this.assertPlaceRoom(2);
        this.assertActNow(2);
        this.sendBuildRoom(2, 2, 0, 8);
        this.assertActionFailed(2);
        this.assertActNow(2);
        this.sendEndTurn(2);
    }

    public void endOfRound2() throws TimeoutException, AssertionError {
        //player1
        this.assertBidRetrieved(1, BidType.NICENESS, 0);
        this.assertBidRetrieved(2, BidType.NICENESS, 0);
        this.assertBidRetrieved(3, BidType.NICENESS, 0);

        this.assertBidRetrieved(1, BidType.TUNNEL, 0);
        this.assertBidRetrieved(2, BidType.TUNNEL, 0);
        this.assertBidRetrieved(3, BidType.TUNNEL, 0);

        this.assertBidRetrieved(1, BidType.GOLD, 0);
        this.assertBidRetrieved(2, BidType.GOLD, 0);
        this.assertBidRetrieved(3, BidType.GOLD, 0);

        //player2
        this.assertBidRetrieved(1, BidType.NICENESS, 1);
        this.assertBidRetrieved(2, BidType.NICENESS, 1);
        this.assertBidRetrieved(3, BidType.NICENESS, 1);

        this.assertBidRetrieved(1, BidType.TUNNEL, 1);
        this.assertBidRetrieved(2, BidType.TUNNEL, 1);
        this.assertBidRetrieved(3, BidType.TUNNEL, 1);

        this.assertBidRetrieved(1, BidType.MONSTER, 1);
        this.assertBidRetrieved(2, BidType.MONSTER, 1);
        this.assertBidRetrieved(3, BidType.MONSTER, 1);

        //player3
        this.assertBidRetrieved(1, BidType.NICENESS, 2);
        this.assertBidRetrieved(2, BidType.NICENESS, 2);
        this.assertBidRetrieved(3, BidType.NICENESS, 2);

        this.assertBidRetrieved(1, BidType.TUNNEL, 2);
        this.assertBidRetrieved(2, BidType.TUNNEL, 2);
        this.assertBidRetrieved(3, BidType.TUNNEL, 2);

        this.assertBidRetrieved(1, BidType.MONSTER, 2);
        this.assertBidRetrieved(2, BidType.MONSTER, 2);
        this.assertBidRetrieved(3, BidType.MONSTER, 2);

        //retrieve Imps of player1
        this.assertImpsChanged(1, 2, 0);
        this.assertImpsChanged(2, 2, 0);
        this.assertImpsChanged(3, 2, 0);

        this.assertGoldChanged(1, 2, 0);
        this.assertGoldChanged(2, 2, 0);
        this.assertGoldChanged(3, 2, 0);

        //retrieve Imps of player3
        this.assertImpsChanged(1, 3, 2);
        this.assertImpsChanged(2, 3, 2);
        this.assertImpsChanged(3, 3, 2);

        this.assertGoldChanged(1, 3, 2);
        this.assertGoldChanged(2, 3, 2);
        this.assertGoldChanged(3, 3, 2);

        // adventurers arrived
        this.assertAdventurerArrived(1, 0, 0);
        this.assertAdventurerArrived(2, 0, 0);
        this.assertAdventurerArrived(3, 0, 0);

        this.assertAdventurerArrived(1, 18, 1);
        this.assertAdventurerArrived(2, 18, 1);
        this.assertAdventurerArrived(3, 18, 1);

        this.assertAdventurerArrived(1, 11, 2);
        this.assertAdventurerArrived(2, 11, 2);
        this.assertAdventurerArrived(3, 11, 2);
    }

    public void thirdRoundDrawing() throws TimeoutException, AssertionError {
        this.assertNextRound(1, 3);
        this.assertNextRound(2, 3);
        this.assertNextRound(3, 3);
        // draw
        this.assertAdventurerDrawn(1, 20);
        this.assertAdventurerDrawn(2, 20);
        this.assertAdventurerDrawn(3, 20);
        this.assertAdventurerDrawn(1, 6);
        this.assertAdventurerDrawn(2, 6);
        this.assertAdventurerDrawn(3, 6);
        this.assertAdventurerDrawn(1, 9);
        this.assertAdventurerDrawn(2, 9);
        this.assertAdventurerDrawn(3, 9);

        this.assertMonsterDrawn(1, 14);
        this.assertMonsterDrawn(2, 14);
        this.assertMonsterDrawn(3, 14);
        this.assertMonsterDrawn(1, 3);
        this.assertMonsterDrawn(2, 3);
        this.assertMonsterDrawn(3, 3);
        this.assertMonsterDrawn(1, 20);
        this.assertMonsterDrawn(2, 20);
        this.assertMonsterDrawn(3, 20);

        this.assertRoomDrawn(1, 0);
        this.assertRoomDrawn(2, 0);
        this.assertRoomDrawn(3, 0);
        this.assertRoomDrawn(1, 10);
        this.assertRoomDrawn(2, 10);
        this.assertRoomDrawn(3, 10);

        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);
        this.assertBiddingStarted(3);

        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        //empty
    }
}
