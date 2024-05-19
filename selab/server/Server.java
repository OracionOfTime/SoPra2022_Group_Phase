package de.unisaarland.cs.se.selab.server;

import de.unisaarland.cs.se.selab.actions.Action;
import de.unisaarland.cs.se.selab.actions.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.biddingsquare.BiddingSquare;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.dungeon.Tile;
import de.unisaarland.cs.se.selab.game.Game;
import de.unisaarland.cs.se.selab.phases.BroadcastEvents;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Server {

    private BroadcastEvents serverConnection;
    private Game game;
    private ActionFactoryImplementation actionFactory;

    public Server(final BroadcastEvents serverConnection, final Game game,
            final ActionFactoryImplementation actionFactory) {
        this.serverConnection = serverConnection;
        this.game = game;
        this.actionFactory = actionFactory;
    }

    /**
     * assignPoints iterates over the players list, puts the players with max value of desired
     * entity (evilness, num. of rooms...) into * @param lords, then iterates over lords list and
     * changes points of the players accordingly
     */

    private static void assignPointsForTitles(final Iterable<DungeonLord> players,
            final List<DungeonLord> lords, final int maxValue) {
        // check if there is more than one player with max evilness
        for (final DungeonLord p : players) {
            if (p.getEvilness() == maxValue) {
                lords.add(p);
            }
        }
        // assign points
        if (lords.size() > 1) {
            for (final DungeonLord l : lords) {
                l.setPoints(l.getPoints() + 2);
            }
        } else {
            lords.get(0).setPoints(lords.get(0).getPoints() + 3);
        }
    }

    public BroadcastEvents getServerConnection() {
        return serverConnection;
    }

    public void setServerConnection(final BroadcastEvents serverConnection) {
        this.serverConnection = serverConnection;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(final Game game) {
        this.game = game;
    }

    public ActionFactoryImplementation getActionFactory() {
        return actionFactory;
    }

    public void setActionFactory(final ActionFactoryImplementation actionFactory) {
        this.actionFactory = actionFactory;
    }

    public void scoringAndTitles() {
        final List<DungeonLord> players = getGame().getDungeonLordsIndexed();
        for (final DungeonLord p : players) {
            final List<List<Optional<Tile>>> dungeon = p.getDungeon().getTiles();
            final int rooms = calculateRooms(dungeon);
            p.setPoints(p.getPoints() + 2 * rooms);
            final int conquered = getConqueredTiles(dungeon);
            p.setPoints(p.getPoints() - 2 * conquered);
            // add points for imprisoned adventurers
            final int imprisonedAdventurers =
                    p.getDungeon().getQueueOfImprisonedAdventurers().size();
            p.setPoints(p.getPoints() + 2 * imprisonedAdventurers);
            // add points for employed monsters
            p.setPoints(p.getPoints() + p.getDungeon().getActiveMonsters().size());
        }
        lordOfDarkDeeds(players);
        lordOfHalls(players);
        tunnelLord(players);
        monsterLord(players);
        lordOfImps(players);
        lordOfRiches(players);
        battleLord(players);

        // broadcast end game
        sendEndGame(players);
    }

    private int getMaxPoints(final List<DungeonLord> players) {
        int max = players.get(0).getPoints();
        for (final DungeonLord p : players) {
            if (p.getPoints() > max) {
                max = p.getPoints();
            }
        }
        return max;
    }

    private int getConqueredTiles(final Iterable<List<Optional<Tile>>> dungeon) {
        int count = 0;
        for (final List<Optional<Tile>> col : dungeon) {
            for (final Optional<Tile> t : col) {
                if (t.isPresent() && t.get().getIsConquered()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void sendEndGame(final List<DungeonLord> players) {
        final int maxPoints = getMaxPoints(players);
        final ArrayList<DungeonLord> playersWithMaxPoints = new ArrayList<>();
        for (final DungeonLord p : players) {
            if (p.getPoints() == maxPoints) {
                playersWithMaxPoints.add(p);
            }
        }
        for (final DungeonLord p : playersWithMaxPoints) {
            serverConnection.broadcastGameEnd(getGame().getDungeonLordsIndexed(),
                    p.getPlayerID(), maxPoints);
        }
    }

    private void lordOfDarkDeeds(final List<DungeonLord> players) {
        final List<DungeonLord> lords = new ArrayList<>();
        int maxEvilness = players.get(0).getEvilness();
        // determine max evilness
        for (final DungeonLord p : players) {
            if (p.getEvilness() > maxEvilness) {
                maxEvilness = p.getEvilness();
            }
        }
        assignPointsForTitles(players, lords, maxEvilness);
    }

    private void lordOfHalls(final List<DungeonLord> players) {
        int maxRooms = calculateRooms(players.get(0).getDungeon().getTiles());
        for (final DungeonLord p : players) {
            final int current = calculateRooms(p.getDungeon().getTiles());
            if (current > maxRooms) {
                maxRooms = current;
            }
        }
        assignPointsForTitles(players, new ArrayList<>(), maxRooms);
    }

    private int calculateRooms(final Iterable<List<Optional<Tile>>> dungeon) {
        // returns number of rooms in dungeon
        int count = 0;
        for (final List<Optional<Tile>> col : dungeon) {
            for (final Optional<Tile> t : col) {
                if (t.isPresent() && t.get().isRoom()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void tunnelLord(final List<DungeonLord> players) {
        // longest tunnel
        int maxTunnel = calculateTunnel(players.get(0).getDungeon().getTiles());
        for (final DungeonLord p : players) {
            final int current = calculateTunnel(p.getDungeon().getTiles());
            if (current > maxTunnel) {
                maxTunnel = current;
            }
        }
        assignPointsForTitles(players, new ArrayList<>(), maxTunnel);
    }

    private int calculateTunnel(final Iterable<List<Optional<Tile>>> dungeon) {
        int count = 0;
        for (final List<Optional<Tile>> col : dungeon) {
            for (final Optional<Tile> t : col) {
                if (t.isPresent() && t.get().isTunnel()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void monsterLord(final List<DungeonLord> players) {
        // most employed monsters
        int maxMonster = players.get(0).getDungeon().getActiveMonsters().size();
        for (final DungeonLord p : players) {
            final int current = p.getDungeon().getActiveMonsters().size();
            if (current > maxMonster) {
                maxMonster = current;
            }
        }
        assignPointsForTitles(players, new ArrayList<>(), maxMonster);
    }

    private void lordOfImps(final List<DungeonLord> players) {
        // most imps
        int maxImps = players.get(0).getImps().size();
        for (final DungeonLord p : players) {
            final int current = p.getImps().size();
            if (current > maxImps) {
                maxImps = current;
            }
        }
        assignPointsForTitles(players, new ArrayList<>(), maxImps);
    }

    private void lordOfRiches(final List<DungeonLord> players) {
        int max = players.get(0).getFood() + players.get(0).getGold();
        for (final DungeonLord p : players) {
            final int current = p.getGold() + p.getFood();
            if (current > max) {
                max = current;
            }
        }
        assignPointsForTitles(players, new ArrayList<>(), max);
    }

    private void battleLord(final List<DungeonLord> players) {
        // most unconquered tiles
        int max = calculateUnconqueredTiles(players.get(0).getDungeon().getTiles());
        for (final DungeonLord p : players) {
            final int current = calculateUnconqueredTiles(p.getDungeon().getTiles());
            if (current > max) {
                max = current;
            }
        }
        assignPointsForTitles(players, new ArrayList<>(), max);
    }

    private int calculateUnconqueredTiles(final Iterable<List<Optional<Tile>>> dungeon) {
        int count = 0;
        for (final List<Optional<Tile>> col : dungeon) {
            for (final Optional<Tile> t : col) {
                if (t.isPresent() && t.get().getIsConquered()) {
                    count++;
                }
            }
        }
        return count;
    }

    public void registerMe() throws IOException {

        for (int i = 0; i < this.game.getMaxPlayers(); i++) {
            try {
                final Action registerAction = serverConnection.nextAction();
                if (registerAction.isStartGame()
                        || this.game.getDungeonLordMap().size() == this.game.getMaxPlayers()) {
                    return;
                } else {
                    registerAction.exec(this.game.getRegistrationPhase());

                }
            } catch (TimeoutException e) {
                return;
            }
        }
    }

    private void retrieveBidsAndClear() {
        final List<DungeonLord> dungeonLordList = game.getDungeonLordsIndexed();

        for (final DungeonLord dl : dungeonLordList) {
            for (int bidNum = 0; bidNum < 2; bidNum++) {
                final Optional<BidType> bidType = dl.getLockedBids().get(bidNum);
                this.serverConnection.broadcastBidRetrieved(game.getDungeonLordsIndexed(),
                        bidType.get(),
                        dl.getPlayerID());
            }

            dl.getLockedBids().set(0, Optional.empty());
            dl.getLockedBids().set(1, Optional.empty());
        }
    }


    public void executeGame() throws IOException {
        game.createRegistrationPhase(serverConnection);
        registerMe();
        game.createOtherPhases(serverConnection);
        serverConnection.broadcastGameStarted(this.game.getDungeonLordsIndexed());
        serverConnection.broadcastAllPlayer(this.game.getDungeonLordsIndexed(), game);
        for (int i = 1; i <= this.game.getMaxYear(); i++) {
            this.game.setCurrYear(i);
            serverConnection.broadcastNextYear(this.game.getDungeonLordsIndexed(), i);

            // retrieve all locked from the previous year
            if (i > 1) {
                retrieveBidsAndClear();
            }

            for (int j = 1; j < 5; j++) {
                executeOneRoundOfBuildingPhase(j);
            }

            this.game.setCurrPhase(this.game.getCombatPhase());

            this.game.getCombatPhase().execute();

        }
        scoringAndTitles();
    }

    private void executeOneRoundOfBuildingPhase(final int round) throws IOException {
        this.game.setCurrPhase(this.game.getBuildingPhase());

        this.game.setRounds(round);

        serverConnection.broadcastNextRound(this.game.getDungeonLordsIndexed(), round);

        this.game.setCurrPhase(this.game.getEvaluationPhase());
        final BiddingSquare bs = this.game.getBuildingPhase().execute(this.game);

        this.game.getEvaluationPhase().setPoolAdventurers(game.getBuildingPhase()
                .getAvailableAdventurers());

        this.game.getEvaluationPhase().execute(bs, this.game);
    }

}
