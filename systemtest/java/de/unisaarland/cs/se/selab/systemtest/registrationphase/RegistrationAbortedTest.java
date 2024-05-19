package de.unisaarland.cs.se.selab.systemtest.registrationphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class RegistrationAbortedTest extends SystemTest {

    public RegistrationAbortedTest() {
        super(RegistrationAbortedTest.class, false);
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
        return Set.of(1);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        this.sendRegister(1, "Player1");
        this.assertConfig(1, config);

        this.sendStartGame(1);
        this.assertGameStarted(1);

        skipReg();

        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertActionFailed(1);
        this.assertActNow(1);

        this.sendLeave(1);
    }

    private void skipReg() throws TimeoutException {
        for (int i = 0; i < 10; i++) {
            this.assertEvent(1);
        }
    }
}
