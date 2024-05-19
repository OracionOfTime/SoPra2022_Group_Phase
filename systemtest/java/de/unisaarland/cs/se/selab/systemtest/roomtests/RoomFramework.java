package de.unisaarland.cs.se.selab.systemtest.roomtests;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public abstract class RoomFramework extends SystemTest {


    public RoomFramework(final Class<?> subclass, final boolean fail) {
        super(subclass, fail);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(RoomFramework.class, "configuration.json");
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
    protected abstract void run() throws TimeoutException, AssertionError;

    public void round1() throws TimeoutException {
        //Registration already happened

        assertGameStarted(1);
        assertGameStarted(2);
        assertGameStarted(3);
        assertGameStarted(4);

        broadcastAssertPlayer();


    }

    private void broadcastAssertPlayer() throws TimeoutException {
        assertPlayer(1, "Player1", 0);
        assertPlayer(2, "Player1", 0);
        assertPlayer(3, "Player1", 0);
        assertPlayer(4, "Player1", 0);

        assertPlayer(1, "Player2", 1);
        assertPlayer(2, "Player2", 1);
        assertPlayer(3, "Player2", 1);
        assertPlayer(4, "Player2", 1);

        assertPlayer(1, "Player3", 2);
        assertPlayer(2, "Player3", 2);
        assertPlayer(3, "Player3", 2);
        assertPlayer(4, "Player3", 2);

        assertPlayer(1, "Player4", 3);
        assertPlayer(2, "Player4", 3);
        assertPlayer(3, "Player4", 3);
        assertPlayer(4, "Player4", 3);
    }


}
