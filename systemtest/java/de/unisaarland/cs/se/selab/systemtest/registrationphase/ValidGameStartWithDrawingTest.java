package de.unisaarland.cs.se.selab.systemtest.registrationphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class ValidGameStartWithDrawingTest extends SystemTest {

    public ValidGameStartWithDrawingTest() {
        super(ValidGameStartWithDrawingTest.class, false);
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
        return Set.of(1, 2);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        this.sendRegister(1, "Niklas");
        this.assertConfig(1, config);

        this.sendRegister(2, "Maxi");
        this.assertConfig(2, config);

        this.sendStartGame(1);
        this.assertGameStarted(1);
        this.assertGameStarted(2);

        this.assertPlayer(1, "Niklas", 0);
        this.assertPlayer(2, "Niklas", 0);

        this.assertPlayer(1, "Maxi", 1);
        this.assertPlayer(2, "Maxi", 1);

        this.assertNextYear(1, 1);
        this.assertNextYear(2, 1);

        this.assertNextRound(1, 1);
        this.assertNextRound(2, 1);

        this.assertAdventurerDrawn(1, 29);
        this.assertAdventurerDrawn(2, 29);

        this.assertAdventurerDrawn(1, 23);
        this.assertAdventurerDrawn(2, 23);

        this.assertMonsterDrawn(1, 23);
        this.assertMonsterDrawn(2, 23);

        this.assertMonsterDrawn(1, 13);
        this.assertMonsterDrawn(2, 13);

        this.assertMonsterDrawn(1, 9);
        this.assertMonsterDrawn(2, 9);

        this.assertRoomDrawn(1, 5);
        this.assertRoomDrawn(2, 5);

        this.assertRoomDrawn(1, 4);
        this.assertRoomDrawn(2, 4);

        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);

        this.assertActNow(1);
        this.assertActNow(2);

        this.sendLeave(1);
        this.assertLeft(2, 0);

        this.sendLeave(2);
    }
}
