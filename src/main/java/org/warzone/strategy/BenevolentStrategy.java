package org.warzone.strategy;

import org.warzone.GameEngine;
import org.warzone.entities.Country;
import org.warzone.states.commands.Order;

import java.util.ArrayList;
import java.util.List;
/**
 * The BenevolentStrategy class represents a specific strategy for a player in a Warzone game, focusing on a benevolent approach.
 * It extends the PlayerStrategy class and provides methods for creating orders based on the benevolent strategy.
 */
public class BenevolentStrategy extends PlayerStrategy {
    /**
     * Constructor for the BenevolentStrategy class.
     *
     * @param d_map    The list of countries in the game.
     * @param d_player The player using this benevolent strategy.
     */
    public BenevolentStrategy(List<Country> d_map, Player d_player) {
        super(d_map, d_player);
    }
    /**
     * Creates an order based on the benevolent strategy.
     *
     * @return The order created for the benevolent strategy.
     */
    @Override
    public Order createOrder() {
        GameEngine.getGamePhase().deploy(super.getD_player(), this.toDefend().getIndex(), getD_player().getD_unDeployedArmies());
        if (toMoveTo() != null) {
            GameEngine.getGamePhase().advance(this.toMoveFrom().getIndex(), this.toDefend().getIndex(), Math.abs(toMoveFrom().getD_noOfArmies() / 2));
        }
        return null;
    }
    /**
     * Determines the country to attack during the benevolent strategy.
     * In the benevolent strategy, there is no specific country chosen for attack, so this method returns null.
     *
     * @return Null since the benevolent strategy does not involve specific attacking.
     */
    @Override
    protected Country toAttack() {
        return null;
    }
    /**
     * Determines the country to attack from during the benevolent strategy.
     * In the benevolent strategy, there is no specific country chosen for attack, so this method returns null.
     *
     * @return Null since the benevolent strategy does not involve specific attacking.
     */
    @Override
    protected Country toAttackFrom() {
        return null;
    }
    /**
     * Determines the country to move armies from during the benevolent strategy.
     *
     * @return The country chosen for moving armies.
     */
    @Override
    protected Country toMoveFrom() {
        return getD_map().stream().max((o1, o2) -> o1.getD_noOfArmies() - o2.getD_noOfArmies()).get();
    }
    /**
     * Determines the country to move armies to during the benevolent strategy.
     *
     * @return The country chosen for moving armies to, or null if there is no valid move-to country.
     */
    private Country toMoveTo() {
        Country moveFrom = toMoveFrom();
        List<Country> ownedNeighbors = new ArrayList<>();
        moveFrom.getAdjacentCountries().forEach((s, country) -> {
            if (country.getCountryOwner().getD_name().equals(getD_player().getD_name())) {
                ownedNeighbors.add(country);
            }
        });
        return ownedNeighbors.stream().min((o1, o2) -> o1.getD_noOfArmies() - o2.getD_noOfArmies()).orElse(null);
    }
    /**
     * Determines the country to defend during the benevolent strategy.
     *
     * @return The country chosen for defense.
     */
    @Override
    protected Country toDefend() {
        return getD_map().stream().min((o1, o2) -> o1.getD_noOfArmies() - o2.getD_noOfArmies()).get();
    }
}
