/*import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.actions.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.actions.Register;
import de.unisaarland.cs.se.selab.actions.StartGame;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.game.Game;
import de.unisaarland.cs.se.selab.game.GameFactory;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.phases.RegistrationPhase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

public class RegisterTests {

    private unittests.UnitTestFactory unitTestFactory = new unittests.UnitTestFactory();

    /*
    @Test
    public void testRegisterNewPlayer() {
        BroadcastEvents broadcastEvents = new BroadcastEvents(5030, -1,
                new ActionFactoryImplementation());
        RegistrationPhase regPhase = new RegistrationPhase(broadcastEvents, new ArrayList<>(4),
                new HashMap<>(4), 4, "config", 5);
        Register registerFirst = new Register(1, "player1");
        boolean res = registerFirst.exec(regPhase);
        assertTrue(res);
    }

    @Test
    public void testRegisterMaxPlayer() {

        final DungeonLord player1 = unitTestFactory.createDungeonLord(0, 1);
        final DungeonLord player2 = unitTestFactory.createDungeonLord(1, 2);
        final DungeonLord player3 = unitTestFactory.createDungeonLord(2, 3);
        final DungeonLord player4 = unitTestFactory.createDungeonLord(3, 4);

        final Map<Integer, DungeonLord> playerMap = new HashMap<>();
        playerMap.put(1, player1);
        playerMap.put(2, player2);
        playerMap.put(3, player3);
        playerMap.put(4, player4);

        final List<DungeonLord> playerList = new ArrayList<>(4);
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);

        BroadcastEvents broadcastEvents = new BroadcastEvents(5030, -1,
                new ActionFactoryImplementation());
        Game game = createGame();
        RegistrationPhase regPhase = new RegistrationPhase(broadcastEvents, playerList,
                playerMap, 4, "config", 4);
        game.setRegistrationPhase(regPhase);

        Register register = new Register(5, "player5");
        boolean res = register.exec(game.getRegistrationPhase());
        assertFalse(res);
    }

    private Game createGame() {
        final Logger logger = Logger.getLogger("print IOEException");
        final GameFactory gameFactory = new GameFactory(42,
                "src/main/resources/configuration.json");
        try {
            if (!gameFactory.parseConfig()) {
                return null;
            }
        } catch (IOException ioe) {
            logger.log(Level.FINE, ioe.toString());
        }
        final Game game = gameFactory.initializeGame();
        return game;
    }

    @Test
    public void testRegisterGameStarted() {
        Game game = unitTestFactory.createDefaultGame();
        StartGame startGame = new StartGame(3);
        boolean result = startGame.exec(game.getRegistrationPhase());
        assertTrue(result);
        Register register = new Register(4, "player4");
        boolean res = register.exec(game.getRegistrationPhase());
        assertFalse(res);
    }


}

 */
