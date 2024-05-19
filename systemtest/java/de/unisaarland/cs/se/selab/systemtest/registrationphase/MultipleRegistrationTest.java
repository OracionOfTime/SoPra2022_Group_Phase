package de.unisaarland.cs.se.selab.systemtest.registrationphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class MultipleRegistrationTest extends SystemTest {

    public MultipleRegistrationTest() {
        super(MultipleRegistrationTest.class, false);
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
        return Set.of(1, 2, 3);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        this.sendRegister(1, "Player1");
        this.assertConfig(1, config);

        this.sendRegister(2, "Player2");
        this.assertConfig(2, config);

        this.sendRegister(2, "Player2");

        this.assertActionFailed(2);

        this.sendLeave(1);
        this.sendLeave(2);
    }
}
