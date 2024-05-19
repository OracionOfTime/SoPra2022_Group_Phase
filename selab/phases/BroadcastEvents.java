package de.unisaarland.cs.se.selab.phases;

import de.unisaarland.cs.se.selab.actions.Action;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.game.Game;

public class BroadcastEvents extends ServerConnection<Action> {

    public BroadcastEvents(final int port, final int timeout,
            final ActionFactory<? extends Action> actionFactory) {
        super(port, timeout, actionFactory);
    }

    public final void broadcastActNow(final Iterable<DungeonLord> dungeonLordSortedList) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendActNow(dl.getCommID());
        }
    }

    public final void broadcastAdventurerArrived(final Iterable<DungeonLord> dungeonLordSortedList,
            final int adventurer, final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendAdventurerArrived(dl.getCommID(), adventurer, player);
        }
    }

    public final void broadcastAdventurerDamaged(final Iterable<DungeonLord> dungeonLordSortedList,
            final int adventurer, final int amount) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendAdventurerDamaged(dl.getCommID(), adventurer, amount);
        }
    }

    public final void broadcastAdventurerDrawn(final Iterable<DungeonLord> dungeonLordSortedList,
            final int adventurer) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendAdventurerDrawn(dl.getCommID(), adventurer);
        }
    }

    public final void broadcastAdventurerFled(final Iterable<DungeonLord> dungeonLordSortedList,
            final int adventurer) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendAdventurerFled(dl.getCommID(), adventurer);
        }
    }

    public final void broadcastAdventurerHealed(final Iterable<DungeonLord> dungeonLordSortedList,
            final int amount, final int priest, final int target) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendAdventurerHealed(dl.getCommID(), amount, priest, target);
        }
    }

    public final void broadcastAdventurerImprisoned(
            final Iterable<DungeonLord> dungeonLordSortedList, final int adventurer) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendAdventurerImprisoned(dl.getCommID(), adventurer);
        }
    }

    public final void broadcastBattleGroundSet(final Iterable<DungeonLord> dungeonLordSortedList,
            final int player, final int x, final int y) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendBattleGroundSet(dl.getCommID(), player, x, y);
        }
    }

    public final void broadcastBiddingStarted(final Iterable<DungeonLord> dungeonLordSortedList) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendBiddingStarted(dl.getCommID());
        }
    }

    public final void broadcastEvilnessChanged(final Iterable<DungeonLord> dungeonLordSortedList,
            final int amount, final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendEvilnessChanged(dl.getCommID(), amount, player);
        }
    }

    public final void broadcastFoodChanged(final Iterable<DungeonLord> dungeonLordSortedList,
            final int amount, final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendFoodChanged(dl.getCommID(), amount, player);
        }
    }

    public final void broadcastGameEnd(final Iterable<DungeonLord> dungeonLordSortedList,
            final int player, final int points) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendGameEnd(dl.getCommID(), player, points);
        }
    }

    public final void broadcastGameStarted(final Iterable<DungeonLord> dungeonLordSortedList) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendGameStarted(dl.getCommID());
        }
    }

    public final void broadcastGoldChanged(final Iterable<DungeonLord> dungeonLordSortedList,
            final int amount, final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendGoldChanged(dl.getCommID(), amount, player);
        }
    }

    public final void broadcastImpsChanged(final Iterable<DungeonLord> dungeonLordSortedList,
            final int amount, final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendImpsChanged(dl.getCommID(), amount, player);
        }
    }

    public final void broadcastLeft(final Iterable<DungeonLord> dungeonLordSortedList,
            final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendLeft(dl.getCommID(), player);
        }
    }

    public final void broadcastMonsterDrawn(final Iterable<DungeonLord> dungeonLordSortedList,
            final int monster) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendMonsterDrawn(dl.getCommID(), monster);
        }
    }

    public final void broadcastMonsterHired(final Iterable<DungeonLord> dungeonLordSortedList,
            final int monster, final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendMonsterHired(dl.getCommID(), monster, player);
        }
    }

    public final void broadcastMonsterPlaced(final Iterable<DungeonLord> dungeonLordSortedList,
            final int monster, final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendMonsterPlaced(dl.getCommID(), monster, player);
        }
    }

    public final void broadcastNextRound(final Iterable<DungeonLord> dungeonLordSortedList,
            final int round) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendNextRound(dl.getCommID(), round);
        }
    }

    public final void broadcastNextYear(final Iterable<DungeonLord> dungeonLordSortedList,
            final int year) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendNextYear(dl.getCommID(), year);
        }
    }

    public final void broadcastBidPlaced(final Iterable<DungeonLord> dungeonLordSortedList,
            final BidType bid, final int player, final int slot) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendBidPlaced(dl.getCommID(), bid, player, slot);
        }
    }

    public final void broadcastBidRetrieved(final Iterable<DungeonLord> dungeonLordSortedList,
            final BidType bid, final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendBidRetrieved(dl.getCommID(), bid, player);
        }
    }

    public final void broadcastPlayer(final Iterable<DungeonLord> dungeonLordSortedList,
            final String name, final int player) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendPlayer(dl.getCommID(), name, player);
        }
    }

    public final void broadcastRegistrationAborted(
            final Iterable<DungeonLord> dungeonLordSortedList) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendRegistrationAborted(dl.getCommID());
        }
    }

    public final void broadcastRoomActivated(final Iterable<DungeonLord> dungeonLordSortedList,
            final int player, final int room) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendRoomActivated(dl.getCommID(), player, room);
        }
    }

    public final void broadcastRoomBuilt(final Iterable<DungeonLord> dungeonLordSortedList,
            final int player, final int room, final int x, final int y) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendRoomBuilt(dl.getCommID(), player, room, x, y);
        }
    }

    public final void broadcastRoomDrawn(final Iterable<DungeonLord> dungeonLordSortedList,
            final int room) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendRoomDrawn(dl.getCommID(), room);
        }
    }

    public final void broadcastTrapAcquired(final Iterable<DungeonLord> dungeonLordSortedList,
            final int player, final int trap) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendTrapAcquired(dl.getCommID(), player, trap);
        }
    }

    public final void broadcastTrapPlaced(final Iterable<DungeonLord> dungeonLordSortedList,
            final int player, final int trap) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendTrapPlaced(dl.getCommID(), player, trap);
        }
    }

    public final void broadcastTunnelConquered(final Iterable<DungeonLord> dungeonLordSortedList,
            final int adventurer, final int x, final int y) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendTunnelConquered(dl.getCommID(), adventurer, x, y);
        }
    }

    public final void broadcastTunnelDug(final Iterable<DungeonLord> dungeonLordSortedList,
            final int player, final int x, final int y) {
        for (final DungeonLord dl : dungeonLordSortedList) {
            super.sendTunnelDug(dl.getCommID(), player, x, y);
        }
    }

    public final void broadcastAllPlayer(final Iterable<DungeonLord> dungeonLordSortedList,
            final Game game) {
        for (final DungeonLord tempDl : game.getDungeonLordsIndexed()) {
            broadcastPlayer(dungeonLordSortedList, tempDl.getName(), tempDl.getPlayerID());
        }
    }


}
