package org.warzone.states.commands;

import org.warzone.strategy.Player;

/**
 * The {@code DiplomacyDetails} class holds information about the diplomacy card.
 * It tracks the rounds during which the agreement
 * is active, as well as the receiving player of the diplomacy.
 */
public class DiplomacyDetails {

    /**
     * The player with whom diplomacy is established.
     */
    private Player d_receiver;

    /**
     * The game round on which the diplomacy starts.
     */
    private int d_startRound;

    /**
     * The game round on which the diplomacy ends.
     */
    private int d_endRound;

    /**
     * Constructs a new {@code DiplomacyDetails} instance with the provided player and round details.
     *
     * @param p_receiver  The player with whom diplomacy is established.
     * @param p_startRound The starting round for the diplomacy.
     * @param p_endRound   The ending round for the diplomacy.
     */
    public DiplomacyDetails(Player p_receiver, int p_startRound, int p_endRound) {
        this.d_receiver = p_receiver;
        this.d_startRound = p_startRound;
        this.d_endRound = p_endRound;
    }

    /**
     * Returns the receiving player of the diplomacy.
     *
     * @return The player with whom diplomacy is established.
     */
    public Player getReceiver() {
        return d_receiver;
    }

    /**
     * Returns the starting round number of the diplomacy.
     *
     * @return The game round on which the diplomacy starts.
     */
    public int getStartRound() {
        return d_startRound;
    }

    /**
     * Returns the ending round number of the diplomacy.
     *
     * @return The game round on which the diplomacy ends.
     */
    public int getEndRound() {
        return d_endRound;
    }
}

