package org.warzone.states.commands;

import org.warzone.GameEngine;
import org.warzone.strategy.Player;

import java.util.ArrayList;

/**
 * The {@code Order} interface defines the contract for game orders in the system.
 * Implementations of this interface are expected to encapsulate all the information
 * necessary for an order's execution, validation, and identity within the game.
 */
public interface Order {
    /**
     * Executes the order.
     * @param gameEngine The game engine.
     */
    public void execute(GameEngine gameEngine);

    /**
     * Checks if the order is valid.
     * @param gameEngine The gameEngine.
     * @return true if the GameEngine is valid, false otherwise
     */
    public boolean valid(GameEngine gameEngine);

    /**
     * Returns the order type.
     * @return orderType which is a string.
     */
    public String getOrdertype();

    /**
     * Sets the player who issues the order.
     * @param player The player issuing the order.
     */
    void setD_player(Player player);

    /**
     * Used to set parameters that are needed for the order.
     * @param lOrderParams The list of order parameters.
     */
    void setD_orderParameters(ArrayList<String> lOrderParams);

    /**
     * Method to get order parameters
     * @return orders that are string
     */
    String getD_orderParameters();
}