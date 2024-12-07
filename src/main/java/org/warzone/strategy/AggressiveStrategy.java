package org.warzone.strategy;

import org.warzone.GameEngine;
import org.warzone.entities.Country;
import org.warzone.states.commands.Order;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * The AggressiveStrategy class represents a specific strategy for a player in a Warzone game, focusing on an aggressive approach.
 * It extends the PlayerStrategy class and provides methods for creating orders based on the aggressive strategy.
 */
public class AggressiveStrategy extends PlayerStrategy{
    /**
     * A Random object for generating random values.
     */
    Random random = new Random();
    /**
     * Constructor for the AggressiveStrategy class.
     *
     * @param d_map    The list of countries in the game.
     * @param d_player The player using this aggressive strategy.
     */
    public AggressiveStrategy(List<Country> d_map, Player d_player) {
        super(d_map, d_player);
    }
    /**
     * Creates an order based on the aggressive strategy.
     *
     * @return The order created for the aggressive strategy.
     */
    @Override
    public Order createOrder() {


        int armies = super.getD_player().getD_unDeployedArmies();
        GameEngine.getGamePhase().deploy(super.getD_player(), this.toAttackFrom().getIndex(), armies);
        GameEngine.getGamePhase().advance(this.toAttackFrom().getIndex(), this.toAttack().getIndex(), this.toAttackFrom().getD_noOfArmies() + armies);
        return null;

    }
    /**
     * Determines the country to attack during the aggressive strategy.
     *
     * @return The country chosen for the attack.
     */
    @Override
    protected Country toAttack() {
        List<Country> neighbourList = new ArrayList<>();
        List<Country> countryListCopy = new ArrayList<>(getD_map());
        List<Country> ownedCountries = new ArrayList<>();
        super.getD_player().getD_listCountries().forEach((s, country) ->
                ownedCountries.add(country));
        while (neighbourList.isEmpty() && !countryListCopy.isEmpty()) {
            Country countryStrongest = countryListCopy.get(0);
            for (Country country : countryListCopy) {
                if (country.getD_noOfArmies() > countryStrongest.getD_noOfArmies()) {
                    countryStrongest = country;
                }
            }
            Map<String, Country> neighbours = countryStrongest.getAdjacentCountries();
            neighbourList = new ArrayList<>(neighbours.values())
                    .stream()
                    .filter(country -> !country.getCountryOwner().getD_name().equals(getD_player().getD_name())).toList();
            if (neighbourList.isEmpty()) {
                countryListCopy.remove(countryStrongest);
            }
        }
        if(neighbourList.isEmpty()){
            int randomIndex = random.nextInt(ownedCountries.size());
            return ownedCountries.get(randomIndex);
        }else{
            int randomIndex = random.nextInt(neighbourList.size());
            return neighbourList.get(randomIndex);
        }
    }
    /**
     * Determines the country to attack from during the aggressive strategy.
     *
     * @return The country chosen as the starting point for the attack.
     */
    @Override
    protected Country toAttackFrom() {
        Country countryStrongest = super.getD_map().get(0);
        for (Country country : super.getD_map()) {
            if (country.getD_noOfArmies() > countryStrongest.getD_noOfArmies()) {
                countryStrongest = country;
            }
        }
        return countryStrongest;
    }
    /**
     * Determines the country to move armies from during the aggressive strategy.
     *
     * @return The country chosen for moving armies.
     */
    @Override
    protected Country toMoveFrom() {
        Country countryStrongest = super.getD_map().get(0);
        for (Country country : super.getD_map()) {
            if (country.getD_noOfArmies() >= countryStrongest.getD_noOfArmies()) {
                countryStrongest = country;
            }
        }
        Country finalCountryStrongest = countryStrongest;
        List<Country> countryValidToMoveFrom = super.getD_map().stream().filter(c -> !c.equals(finalCountryStrongest)).toList();
        int randomIndex = random.nextInt(countryValidToMoveFrom.size());
        return countryValidToMoveFrom.get(randomIndex);
    }
    /**
     * Determines the country to defend during the aggressive strategy.
     * In the aggressive strategy, there is no specific country chosen for defense, so this method returns null.
     *
     * @return Null since the aggressive strategy does not involve specific defending.
     */
    @Override
    protected Country toDefend() {
        return null;
    }
}
