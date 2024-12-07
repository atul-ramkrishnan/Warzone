package org.warzone.strategy;

import org.warzone.entities.Country;
import org.warzone.states.commands.Order;

import java.util.List;
/**
 * The PlayerStrategy class is an abstract class representing a generic strategy for a player in a Warzone game.
 * Concrete strategy classes should extend this class and provide implementations for the abstract methods.
 * It encapsulates the map and player information relevant to the strategy.
 */
public abstract class PlayerStrategy {
    /** The list of countries in the game. */
    private List<Country> d_map;
    /** The player using this strategy. */
    private Player d_player;
    /**
     * Constructor for the PlayerStrategy class.
     *
     * @param d_map    The list of countries in the game.
     * @param d_player The player using this strategy.
     */
    public PlayerStrategy(List<Country> d_map, Player d_player) {
        this.d_map = d_map;
        this.d_player = d_player;
    }

    /**
     * Constructor for the PlayerStrategy class.
     */
    public List<Country> getD_map() {
        return d_map;
    }

    /**
     * Sets the list of countries in the game.
     *
     * @param d_map The list of countries to set.
     */
    public void setD_map(List<Country> d_map) {
        this.d_map = d_map;
    }

    /**
     * Gets the player using this strategy.
     *
     * @return The player using this strategy.
     */
    public Player getD_player() {
        return d_player;
    }

    /**
     * Sets the player using this strategy.
     *
     * @param d_player The player to set.
     */
    public void setD_player(Player d_player) {
        this.d_player = d_player;
    }

    /**
     * Abstract method to create an order based on the strategy.
     *
     * @return The order created by the strategy.
     */
    public abstract Order createOrder();

    /**
     * Abstract method to determine the country to attack based on the strategy.
     *
     * @return The country chosen for attack.
     */
    protected abstract Country toAttack();

    /**
     * Abstract method to determine the country to attack from based on the strategy.
     *
     * @return The country chosen as the starting point for an attack.
     */
    protected abstract Country toAttackFrom();

    /**
     * Abstract method to determine the country to move armies from based on the strategy.
     *
     * @return The country chosen for moving armies.
     */
    protected abstract Country toMoveFrom();

    /**
     * Abstract method to determine the country to defend based on the strategy.
     *
     * @return The country chosen for defense.
     */
    protected abstract Country toDefend();
}
