package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

/**
 * Register 2 Players and Leave
 */
public class RegistrationTest extends SystemTest {

    RegistrationTest() {
        super(RegistrationTest.class, false);
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
        return Set.of(1, 2, 3, 4);
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        this.sendRegister(1, "Niklas");
        this.assertConfig(1, config);

        this.sendRegister(2, "Maxi");
        this.assertConfig(2, config);

        this.sendRegister(3, "Jonas");
        this.assertConfig(3, config);

        this.sendRegister(4, "Jana");
        this.assertConfig(4, config);

        this.assertGameStarted(1);
        this.assertGameStarted(2);
        this.assertGameStarted(3);
        this.assertGameStarted(4);

        leaveAll();

    }

    public void leaveAll() {
        for (int socket = 1; socket < 5; socket++) {
            this.sendLeave(socket);
        }
    }
}
