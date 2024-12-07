package org.warzone.states;

import org.warzone.strategy.Player;

import java.util.ArrayList;

/**
 * The {@code StartUpPhase} abstract class represents a phase in the game lifecycle.
 * It defines a set of abstract methods that are to be implemented by concrete phase classes
 * to perform various actions appropriate to the current phase of the game.
 */
public abstract class StartUpPhase extends Phase {

    /**
     * {@inheritDoc}
     */
    @Override
    public void gamePlayer(String[] setUpCommands, ArrayList<Player> l_PlayerList) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignCountries(ArrayList<Player> l_ListPlayer) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deploy(Player p_player, int p_countryIndex, int p_armyAmount) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void advance(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bomb(int p_countryID) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void blockade(int p_countryID) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void airlift(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void negotiate(Player p_initiator, Player p_receiver) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinforce() {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fortify() {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame() {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeOrdersRoundRobin(ArrayList<Player> p_playerList) {
        printInvalidCommandMessage();
    }
}
