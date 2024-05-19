package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;
import java.util.Set;

public abstract class CustomBuildingPhaseTests extends SystemTest {

    public CustomBuildingPhaseTests(final Class<?> subclass, final boolean res) {
        super(subclass, res);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(CustomBuildingPhaseTests.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3);
    }

    protected void registerAndStartGame() throws TimeoutException, AssertionError {
        final String config = createConfig();
        sendRegister(1, "player1");
        assertConfig(1, config);
        sendRegister(2, "player2");
        assertConfig(2, config);
        sendRegister(3, "player3");
        assertConfig(3, config);
        sendStartGame(3);
        assertGameStarted(1);
        assertGameStarted(2);
        assertGameStarted(3);

        assertPlayer(1, "player1", 0);
        assertPlayer(2, "player1", 0);
        assertPlayer(3, "player1", 0);
        assertPlayer(1, "player2", 1);
        assertPlayer(2, "player2", 1);
        assertPlayer(3, "player2", 1);
        assertPlayer(1, "player3", 2);
        assertPlayer(2, "player3", 2);
        assertPlayer(3, "player3", 2);

        assertNextYear(1, 1);
        assertNextYear(2, 1);
        assertNextYear(3, 1);
        assertNextRound(1, 1);
        assertNextRound(2, 1);
        assertNextRound(3, 1);
    }

    protected void draw(final List<Integer> adventurers, final List<Integer> monsters,
            final List<Integer> rooms, final int index, final int round)
            throws TimeoutException, AssertionError {
        if (round < 4) {
            for (int i = index; i < index + 3; i++) {
                for (int j = 1; j < 4; j++) {
                    assertAdventurerDrawn(j, adventurers.get(i));
                }

            }
        }

        for (int i = index; i < index + 3; i++) {
            for (int j = 1; j < 4; j++) {
                assertMonsterDrawn(j, monsters.get(i));
            }
        }

        for (int j = 1; j < 4; j++) {
            assertRoomDrawn(j, rooms.get(index - round + 1));
        }
        for (int i = 1; i < 4; i++) {
            assertRoomDrawn(i, rooms.get(index - round + 2));
        }
    }

    protected void spreadAdventurers(final int adventurer, final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertAdventurerArrived(i, adventurer, player);
        }
    }

    protected void returnImpsForGold(final int slot, final int player, final int sockets)
            throws TimeoutException, AssertionError {
        switch (slot) {
            case 1 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertImpsChanged(i, 2, player);
                }
                for (int i = 1; i <= sockets; i++) {
                    assertGoldChanged(i, 2, player);
                }
            }
            case 2 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertImpsChanged(i, 3, player);
                }
                for (int i = 1; i <= sockets; i++) {
                    assertGoldChanged(i, 3, player);
                }
            }
            case 3 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertImpsChanged(i, 5, player);
                }
                for (int i = 1; i <= sockets; i++) {
                    assertGoldChanged(i, 4, player);
                }
            }
            default -> {
            }
        }
    }

    protected void returnImpsForTunnel(final int slot, final int player, final int sockets)
            throws TimeoutException, AssertionError {
        switch (slot) {
            case 1 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertImpsChanged(i, 2, player);
                }
            }
            case 2 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertImpsChanged(i, 3, player);
                }
            }
            case 3 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertImpsChanged(i, 5, player);
                }
            }
            default -> {
            }
        }
    }

    protected void returnAllImps(final int amount, final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertImpsChanged(i, amount, player);
        }
    }

    protected void retrieveGold(final int amount, final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertGoldChanged(i, amount, player);
        }
    }

    protected void evalFoodSlotOne(final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertGoldChanged(i, -1, player);
        }
        for (int i = 1; i <= sockets; i++) {
            assertFoodChanged(i, 2, player);
        }
    }

    protected void evalFoodSlotTwo(final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertEvilnessChanged(i, 1, player);
        }
        for (int i = 1; i <= sockets; i++) {
            assertFoodChanged(i, 3, player);
        }
    }

    protected void evalFoodSlotThree(final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertEvilnessChanged(i, 2, player);
        }
        for (int i = 1; i <= sockets; i++) {
            assertFoodChanged(i, 3, player);
        }
        for (int i = 1; i <= sockets; i++) {
            assertGoldChanged(i, 1, player);
        }
    }

    protected void evalNiceness(final int slot, final int player, final int sockets)
            throws TimeoutException, AssertionError {
        switch (slot) {
            case 1 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertEvilnessChanged(i, -1, player);
                }
            }
            case 2 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertEvilnessChanged(i, -2, player);
                }
            }
            case 3 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertGoldChanged(i, -2, player);
                }
                for (int i = 1; i <= sockets; i++) {
                    assertEvilnessChanged(i, -2, player);
                }
            }
            default -> {
            }
        }
    }


    protected void evalGold(final int slot, final int player, final int sockets)
            throws TimeoutException, AssertionError {
        switch (slot) {
            case 1 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertImpsChanged(i, -2, player);
                }
            }
            case 2 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertImpsChanged(i, -3, player);
                }
            }
            case 3 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertImpsChanged(i, -5, player);
                }
            }
            default -> {
            }
        }
    }

    protected void evalImpsSlotOne(final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertFoodChanged(i, -1, player);
        }
        for (int i = 1; i <= sockets; i++) {
            assertImpsChanged(i, 1, player);
        }
    }

    protected void evalImpsSlotTwo(final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertFoodChanged(i, -2, player);
        }
        for (int i = 1; i <= sockets; i++) {
            assertImpsChanged(i, 2, player);
        }
    }

    protected void evalImpsSLotThree(final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertFoodChanged(i, -1, player);
        }
        for (int i = 1; i <= sockets; i++) {
            assertGoldChanged(i, -1, player);
        }
        for (int i = 1; i <= sockets; i++) {
            assertImpsChanged(i, 2, player);
        }
    }


    protected void evalTrap(final List<Integer> traps, final int player,
            final int slot, final int index, final int sockets)
            throws TimeoutException, AssertionError {
        switch (slot) {
            case 1 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertGoldChanged(i, -1, player);
                }
                for (int i = 1; i <= sockets; i++) {
                    assertTrapAcquired(i, player, traps.get(index));
                }
            }
            case 2 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertTrapAcquired(i, player, traps.get(index));
                }
            }
            case 3 -> {
                for (int i = 1; i <= sockets; i++) {
                    assertGoldChanged(i, -2, player);
                }
                for (int i = 1; i <= sockets; i++) {
                    assertTrapAcquired(i, player, traps.get(index));
                }
                for (int i = 1; i <= sockets; i++) {
                    assertTrapAcquired(i, player, traps.get(index + 1));
                }
            }
            default -> {
            }
        }
    }

    protected void evalMonster(final int slot, final int socket, final int id,
            final int hunger, final int evilness, final int players)
            throws TimeoutException, AssertionError {
        assertSelectMonster(socket);
        assertActNow(socket);
        sendHireMonster(socket, id);

        if (hunger > 0) {
            for (int i = 1; i <= players; i++) {
                assertFoodChanged(i, -hunger, socket - 1);
            }
        }
        if (evilness > 0) {
            for (int i = 1; i <= players; i++) {
                assertEvilnessChanged(i, evilness, socket - 1);
            }
        }

        for (int i = 1; i <= players; i++) {
            assertMonsterHired(i, id, socket - 1);
        }

        if (slot == 3) {
            for (int i = 1; i <= players; i++) {
                assertFoodChanged(i, -1, socket - 1);
            }
        }
    }

    protected void unlockBids(final BidType bid, final int player, final int sockets)
            throws TimeoutException, AssertionError {
        for (int i = 1; i <= sockets; i++) {
            assertBidRetrieved(i, bid, player);
        }
    }

    /**
     * sends events and actions for evaluation of 1st slot of tunnel bid (in this case, player digs
     * 2 tiles next to each other)
     *
     * @param socket - socket number
     * @param column - x coordinate of tunnel tile
     * @param row    - y coordinate of tunnel tile
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void evalTunnelSlotOne(final int socket, final int column, final int row,
            final int players) throws TimeoutException, AssertionError {
        assertDigTunnel(socket);
        assertActNow(socket);
        sendDigTunnel(socket, column, row);
        for (int i = 1; i <= players; i++) {
            assertImpsChanged(i, -1, socket - 1);
        }

        for (int i = 1; i <= players; i++) {
            assertTunnelDug(i, socket - 1, column, row);
        }

        assertActNow(socket);
        sendDigTunnel(socket, column + 1, row);
        for (int i = 1; i <= players; i++) {
            assertImpsChanged(i, -1, socket - 1);
        }
        for (int i = 1; i <= players; i++) {
            assertTunnelDug(i, socket - 1, column + 1, row);
        }
    }

    /**
     * evaluates 2nd slot of tunnel bid (in this case, player places tunnels on (x,y), (x,y+1) and
     * (x+1,y+1))
     *
     * @throws TimeoutException -
     * @throws AssertionError   -
     */
    protected void evalTunnelSlotTwo(final int socket, final int column, final int row,
            final int players) throws TimeoutException, AssertionError {
        evalTunnelSlotOne(socket, column, row, players);
        assertActNow(socket);
        sendDigTunnel(socket, column + 1, row + 1);
        for (int i = 1; i <= players; i++) {
            assertImpsChanged(i, -1, socket - 1);
        }

        for (int i = 1; i <= players; i++) {
            assertTunnelDug(i, socket - 1, column + 1, row + 1);
        }
    }
}
