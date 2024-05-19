package de.unisaarland.cs.se.selab.actions;

import de.unisaarland.cs.se.selab.dungeon.Adventurer;
import de.unisaarland.cs.se.selab.dungeon.DungeonLord;
import de.unisaarland.cs.se.selab.phases.BuildingPhase;
import de.unisaarland.cs.se.selab.phases.CombatPhase;
import de.unisaarland.cs.se.selab.phases.EvaluationPhase;
import de.unisaarland.cs.se.selab.phases.Phase;
import de.unisaarland.cs.se.selab.phases.RegistrationPhase;
import java.io.IOException;
import java.util.Queue;

public class Leave extends Action {

    public Leave(final int commID) {
        super(commID);
    }

    @Override
    public boolean isLeave() {
        return true;
    }

    @Override
    public boolean exec(final RegistrationPhase rp) throws IOException {
        leave(rp);
        return false;
    }

    @Override
    public boolean exec(final BuildingPhase bp) throws IOException {
        leave(bp);
        return false;
    }

    @Override
    public boolean exec(final EvaluationPhase ep) throws IOException {
        leave(ep);
        return false;

    }

    @Override
    public boolean exec(final CombatPhase cp) throws IOException {
        leave(cp);
        return false;

    }

    private void leave(final Phase phase) throws IOException {
        final DungeonLord dl = phase.getDungeonLordMap().get(getCommID());

        phase.getDungeonLordMap().remove(getCommID());
        phase.getDungeonLordSortedList().remove(dl);

        phase.getSc().broadcastLeft(phase.getDungeonLordSortedList(), dl.getPlayerID());
        final Queue<Adventurer> tempAdvQ = dl.getDungeon().getQueueOfImprisonedAdventurers();

        final int tempAdvQLength = tempAdvQ.size();
        for (int i = 0; i < tempAdvQLength; i++) {
            final Adventurer tempAdv = tempAdvQ.poll();
            phase.getSc()
                    .broadcastAdventurerFled(phase.getDungeonLordSortedList(), tempAdv.getId());
        }

        if (phase.getDungeonLordSortedList().isEmpty()) {
            throw new IOException();
        }
    }
}
