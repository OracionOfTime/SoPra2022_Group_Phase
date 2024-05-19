package unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.Game;
import de.unisaarland.cs.se.selab.game.GameFactory;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class GameFactoryTest {


    @Test
    void validConfigTest() throws IOException {
        final GameFactory gameFactory = new GameFactory(42,
                "src/main/resources/configuration.json");
        assertTrue(gameFactory.parseConfig());
    }

    @Test
    void inValidConfigTest() throws IOException {
        final GameFactory gameFactory = new GameFactory(42,
                "src/main/resources/config_broken.json");
        assertFalse(gameFactory.parseConfig());
    }

    @Test
    void inValidConfigTest2() throws IOException {
        final GameFactory gameFactory = new GameFactory(42, "src/test/java/broken_config2.json");
        assertFalse(gameFactory.parseConfig());
    }

    @Test
    void inValidConfigTest3() throws IOException {
        final GameFactory gameFactory = new GameFactory(42,
                "src/test/java/broken_config_ids_not_unique.json");
        assertFalse(gameFactory.parseConfig());
    }


    @Test
    void completeConfigTest() throws IOException {
        final GameFactory gameFactory = new GameFactory(42,
                "src/main/resources/configuration.json");
        assertTrue(gameFactory.parseConfig());

        final Game game = gameFactory.initializeGame();
        assertEquals(4, game.getMaxPlayers());
        assertEquals(2, game.getMaxYear());
        assertEquals(3, game.getInitialFood());
        assertEquals(3, game.getInitialGold());
        assertEquals(5, game.getDungeonSideLength());
        assertEquals(3, game.getInitialImps());
        assertEquals(24, game.getMonsterPool().size());
        assertEquals(32, game.getAdventurerPool().size());
        assertEquals(51, game.getTrapPool().size());
        assertEquals(16, game.getRoomPool().size());


    }
}
