package de.unisaarland.cs.se.selab.systemtest.registrationphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class TooManyRegistrationsTest extends SystemTest {

    public TooManyRegistrationsTest() {
        super(TooManyRegistrationsTest.class, false);
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
        return Set.of(1, 2, 3, 4, 5);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        this.sendRegister(1, "Player1");
        this.assertConfig(1, config);

        this.sendRegister(2, "Player2");
        this.assertConfig(2, config);

        this.sendRegister(3, "Player3");
        this.assertConfig(3, config);

        this.sendRegister(4, "Player4");
        this.assertConfig(4, config);

        this.assertGameStarted(1);
        this.assertGameStarted(2);
        this.assertGameStarted(3);
        this.assertGameStarted(4);

        skipReg();

        this.sendRegister(5, "Player5");

        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
        this.sendLeave(4);
    }

    private void skipReg() throws TimeoutException {
        for (int i = 0; i < 17; i++) {
            this.assertEvent(1);
            this.assertEvent(2);
            this.assertEvent(3);
            this.assertEvent(4);
        }
    }
}
