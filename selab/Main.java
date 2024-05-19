package de.unisaarland.cs.se.selab;


import de.unisaarland.cs.se.selab.actions.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.game.Game;
import de.unisaarland.cs.se.selab.game.GameFactory;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import de.unisaarland.cs.se.selab.server.Server;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    public static void main(final String[] args) throws IOException, ParseException {

        //final Logger logger = Logger.getLogger("printError");

        final Options options = new Options();
        final CommandLineParser parser = new DefaultParser();

        options.addOption(Option.builder()
                .required(true)
                .longOpt("config")
                .desc("path to the config file")
                .hasArg()
                .argName("config")
                .build());

        options.addOption(Option.builder()
                .required(true)
                .longOpt("port")
                .desc("port number of tcp")
                .hasArg()
                .argName("port")
                .build());

        options.addOption(Option.builder()
                .required(true)
                .longOpt("seed")
                .desc("seed to generate randomness")
                .hasArg()
                .argName("seed")
                .build());

        options.addOption(Option.builder()
                .required(true)
                .longOpt("timeout")
                .desc("timeout for server")
                .hasArg()
                .argName("timeout")
                .build());

        //parse the options passed as command line arguments

        final CommandLine cmd = parser.parse(options, args);

        final String configPath = cmd.getOptionValue("config");
        final int port = Integer.parseInt(cmd.getOptionValue("port"));
        final long seed = Long.parseLong(cmd.getOptionValue("seed"));
        final int timeout = Integer.parseInt(cmd.getOptionValue("timeout"));

        final GameFactory gameFactory = new GameFactory(seed, configPath);

        if (gameFactory.parseConfig()) {
            final Game game = gameFactory.initializeGame();

            if (game != null) {

                final ActionFactoryImplementation actionFactory =
                        new ActionFactoryImplementation();
                try (final BroadcastEvents serverConnection = new BroadcastEvents(port,
                        timeout * 1000,
                        actionFactory)) {

                    final Server server = new Server(serverConnection, game, actionFactory);
                    server.executeGame();
                } catch (IOException e) {
                    return;
                }
            }
        } else {
            throw new IOException();
        }
    }
}
