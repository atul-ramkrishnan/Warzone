package org.warzone.strategy;

import org.warzone.GameEngine;
import org.warzone.entities.Country;
import org.warzone.states.commands.Order;

import java.util.ArrayList;
import java.util.Random;

import java.util.List;

/**
 * The RandomStrategy class represents a specific strategy for a player in a Warzone game, focusing on random actions.
 * It extends the PlayerStrategy class and provides methods for creating orders based on random decisions.
 */
public class RandomStrategy extends PlayerStrategy {

    /**
     * Constructor for the RandomStrategy class.
     *
     * @param d_map    The list of countries in the game.
     * @param d_player The player using this random strategy.
     */
    public RandomStrategy(List<Country> d_map, Player d_player) {
        super(d_map, d_player);
    }

    /** The random number generator for making random decisions. */
    Random random = new Random();

    /**
     * Creates an order based on the random strategy.
     *
     * @return The order created for the random strategy.
     */
    @Override
    public Order createOrder() {
        int armies = super.getD_player().getD_unDeployedArmies();
        int order = random.nextInt(3);

        GameEngine.getGamePhase().deploy(super.getD_player(), this.toDefend().getIndex(), armies);
        GameEngine.getGamePhase().advance(this.toAttackFrom().getIndex(), this.toAttack().getIndex(), this.toAttackFrom().getD_noOfArmies());


        return null;
    }

    /**
     * Determines the country to attack during the random strategy.
     *
     * @return The country chosen for attack.
     */
    @Override
    protected Country toAttack() {
        List<Country> ownedCountries = new ArrayList<>();
        List<Country> neighbours = new ArrayList<>();
        super.getD_player().getD_listCountries().forEach((s, country) ->
                ownedCountries.add(country));
        for (Country country : ownedCountries) {
            country.getAdjacentCountries().forEach((s, country1) -> {
                if (!super.getD_map().contains(country1)) {
                    neighbours.add(country1);
                }
            });

        }
        int randomIndex = random.nextInt(neighbours.size());
        return neighbours.get(randomIndex);
    }

    /**
     * Determines the country to attack from during the random strategy.
     *
     * @return The country chosen as the starting point for an attack.
     */
    @Override
    protected Country toAttackFrom() {
        List<Country> ownedCountries = new ArrayList<>();
        List<Country> countriesWithArmies = new ArrayList<>();
        super.getD_player().getD_listCountries().forEach((s, country) ->
                ownedCountries.add(country));
        for (Country c : ownedCountries) {
            if (c.getD_noOfArmies() > 0) {
                countriesWithArmies.add(c);
            }
        }
        if (!countriesWithArmies.isEmpty()) {
            int randomIndex = random.nextInt(countriesWithArmies.size());
            return countriesWithArmies.get(randomIndex);
        }
        int randomIndex = random.nextInt(ownedCountries.size());
        return ownedCountries.get(randomIndex);
    }

    /**
     * Determines the country to move armies from during the random strategy.
     *
     * @return The country chosen for moving armies.
     */
    @Override
    protected Country toMoveFrom() {
        return super.getD_map().get(random.nextInt(super.getD_map().size()));
    }

    /**
     * Determines the country to defend during the random strategy.
     *
     * @return The country chosen for defense.
     */
    @Override
    protected Country toDefend() {
        return super.getD_map().get(random.nextInt(super.getD_map().size()));
    }
}
