package de.unisaarland.cs.se.selab.systemtest.combatphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.evaluationphasetest.EvaluateMonsterBids;
import java.util.Set;

public class CombatPhaseSystemTest extends EvaluateMonsterBids {

    public CombatPhaseSystemTest() {
        super(CombatPhaseSystemTest.class, false);
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

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();
        final Set<Integer> socket = createSockets();
        this.registration(socket, config);
        this.firstBid(socket);
        this.firstEvaluation(socket);
        this.endOfFirstRound(socket);
        this.startOfRound2(socket);
        this.secondBidding(socket);
        this.secondEval(socket);
        this.secondEval2(socket);
        this.endOfRound2(socket);
        this.startOfRound3(socket);

        this.sendLeave(1);
        this.assertLeft(2, 0);
        this.assertLeft(3, 0);
        this.assertLeft(4, 0);

        this.sendLeave(2);
        this.assertLeft(3, 1);
        this.assertLeft(4, 1);

        this.round3(socket);
        this.endOfRound3(socket);
        this.startOfRound4(socket);
        this.endOfRound4(socket);
        // PLayer 2 Starts CP
        this.assertSetBattleGround(3);
        this.assertActNow(3);
        this.sendBattleGround(3, 0, 1);
        this.assertActionFailed(3);
        this.assertActNow(3);
        this.sendBattleGround(3, 0, 0);
        this.assertBattleGroundSet(3, 2, 0, 0);
        this.assertBattleGroundSet(4, 2, 0, 0);
        this.assertDefendYourself(3);
        this.assertActNow(3);
        this.sendTrap(3, 1);
        this.assertActionFailed(3);
        this.assertActNow(3);
        this.sendTrap(3, 19);  // trap activated
        this.assertTrapPlaced(3, 2, 19);
        this.assertTrapPlaced(4, 2, 19);
        this.assertActNow(3);
        this.sendMonsterTargeted(3, 14, 1);
        this.assertActionFailed(3);
        this.assertActNow(3);
        this.sendMonsterTargeted(3, 4, 1);
        this.assertActionFailed(3);
        this.assertActNow(3);
        this.sendMonster(3, 14);
        this.assertMonsterPlaced(3, 14, 2);
        this.assertMonsterPlaced(4, 14, 2);
        this.assertActNow(3);
        this.sendEndTurn(3);
        this.assertAdventurerDamaged(3, 0, 1);
        this.assertAdventurerDamaged(4, 0, 1);
        this.assertAdventurerDamaged(3, 18, 1);
        this.assertAdventurerDamaged(4, 18, 1);
        this.assertAdventurerImprisoned(3, 16);
        this.assertAdventurerImprisoned(4, 16);
        this.assertAdventurerImprisoned(3, 0);
        this.assertAdventurerImprisoned(4, 0);
        this.assertAdventurerDamaged(3, 18, 2);
        this.assertAdventurerDamaged(4, 18, 2);
        this.assertTunnelConquered(3, 18, 0, 0);
        this.assertTunnelConquered(4, 18, 0, 0);
        this.assertEvilnessChanged(3, -1, 2);
        this.assertEvilnessChanged(4, -1, 2);
        this.assertNextRound(3, 2);
        this.assertNextRound(4, 2);
        this.assertSetBattleGround(3);
        this.assertActNow(3);
        this.sendBattleGround(3, 0, 0);
        this.assertActionFailed(3);
        this.assertActNow(3);
        this.sendBattleGround(3, 0, 1);
        this.assertBattleGroundSet(3, 2, 0, 1);
        this.assertBattleGroundSet(4, 2, 0, 1);
        this.assertDefendYourself(3);
        this.assertActNow(3);
        //don't have anything to place
        //For the test
        this.sendLeave(3);
        this.assertLeft(4, 2);
        this.assertAdventurerFled(4, 16);
        this.assertAdventurerFled(4, 0);
        /* this.sendEndTurn(3);
        this.assertAdventurerImprisoned(3,18);
        this.assertAdventurerImprisoned(4,18);*/
        // game done, next player
        this.assertNextRound(4, 1);
        this.assertNextRound(4, 1);
        this.assertSetBattleGround(4);
        this.assertActNow(4);
        this.sendLeave(4);
        //Year=2
        //this.assertNextYear(3,2);
    }

}
