package de.unisaarland.cs.se.selab.systemtest.evaluationphasetest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import java.util.Set;

public class CombatPhasePlayer1 extends EvalAllBidsInOrder9 {

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3);
    }

    @Override
    public void run() throws TimeoutException {
        registrationPhase();
        firstRoundDrawing();
        biddingPhase1FirstBids();
        biddingPhase1SecondBids();
        biddingPhase1ThirdBids();
        evalPhaseFoodBid1();
        evalPhaseNicenessBid1();
        evalPhaseTunnelBid1();
        endOfRound1();
        secondRoundDrawing();
        biddingPhase2FirstBids();
        biddingPhase2SecondBids();
        biddingPhase2ThirdBids();
        evalPhaseFoodBid2();
        evalPhaseGoldBid2();
        evalPhaseImpsBid2();
        evalPhaseTrapBid2();
        evalPhaseMonsterBid2();
        evalPhaseRoomBid2();
        endOfRound2();
        thirdRoundDrawing();
        biddingPhase3FirstBids();
        biddingPhase3SecondBids();
        biddingPhase3ThirdBids();
        evalPhaseNicenessBid3();
        evalPhaseTunnelBid3();
        evalPhaseGoldBid3();
        evalPhaseImpBid3();
        evalPhaseMonsterBid3();
        evalPhaseRoomBid3();
        endOfRound3();
        fourthRoundDrawing();
        biddingPhase4FirstBids();
        biddingPhase4SecondBids();
        biddingPhase4ThirdBids();
        evalPhaseGoldBid4();
        evalPhaseImpBid4();
        evalPhaseTrapBid4();
        evalPhaseMonsterBid4();
        evalPhaseRoomBid4();
        endOfRound4();
        firstCombatPhaseRound();
        secondCombatPhaseRound();
        endOfCombatPhase1();
    }

    public void firstCombatPhaseRound() throws TimeoutException, AssertionError {
        this.sendBattleGround(1, 0, 0);
        this.assertBattleGroundSet(1, 0, 0, 0);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendEndTurn(1);
        this.assertAdventurerDamaged(1, 0, 2);
        this.assertAdventurerDamaged(1, 2, 2);
        this.assertAdventurerDamaged(1, 9, 2);

        this.assertTunnelConquered(1, 0, 0, 0);
        this.assertEvilnessChanged(1, -1, 0);
        this.assertAdventurerHealed(1, 1, 9, 0);
    }

    public void secondCombatPhaseRound() throws TimeoutException, AssertionError {
        this.assertNextRound(1, 2);
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 0, 1);
        this.assertBattleGroundSet(1, 0, 0, 1);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.assertTrapPlaced(1, 0, 6);
        this.sendMonster(1, 5);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendEndTurn(1);
        this.assertAdventurerImprisoned(1, 0);
        this.assertAdventurerImprisoned(1, 2);
        this.assertAdventurerImprisoned(1, 9);
    }

    public void endOfCombatPhase1() throws TimeoutException, AssertionError {

        this.assertNextYear(1, 2);
        this.assertBidRetrieved(1, BidType.MONSTER, 0);
        this.assertBidRetrieved(1, BidType.GOLD, 0);

        this.assertNextRound(1, 1);
        this.assertAdventurerDrawn(1, 15);

        this.assertMonsterDrawn(1, 0);
        this.assertMonsterDrawn(1, 18);
        this.assertMonsterDrawn(1, 21);

        this.assertRoomDrawn(1, 12);
        this.assertRoomDrawn(1, 6);

        this.assertBiddingStarted(1);

        this.assertActNow(1);
        this.sendLeave(1);
    }
}
