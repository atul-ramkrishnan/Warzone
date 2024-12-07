package org.warzone.strategy;

import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.states.commands.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * The CheaterStrategy class represents a specific strategy for a player in a Warzone game, focusing on a cheating approach.
 * It extends the PlayerStrategy class and provides methods for creating orders based on the cheater strategy.
 */
public class CheaterStrategy extends PlayerStrategy {
    /**
     * Constructor for the CheaterStrategy class.
     *
     * @param d_map    The list of countries in the game.
     * @param d_player The player using this cheater strategy.
     */
    public CheaterStrategy(List<Country> d_map, Player d_player) {
        super(d_map, d_player);
    }
    /**
     * Creates an order based on the cheater strategy.
     *
     * @return The order created for the cheater strategy.
     */
    @Override
    public Order createOrder() {
        GameMap gameMap = GameMap.getInstance();
        List<Country> ownedCountries = new ArrayList<>();
        super.getD_player().getD_listCountries().forEach((s, country) ->
                ownedCountries.add(country));
        for (Country country : ownedCountries) {
            List<Country> neighbours = new ArrayList<>();

            country.getAdjacentCountries().forEach((s, country1) ->
                    neighbours.add(country1));

            for (Country c : neighbours) {
                if(super.getD_player().getD_listCountries().containsKey(String.valueOf(c.getIndex()))){

                }else{
                    Player ownerNeighbor = c.getCountryOwner();
                    ownerNeighbor.getD_listCountries().remove(String.valueOf(c.getIndex()));
                    super.getD_player().getD_listCountries().put(String.valueOf(c.getIndex()), c);
                    gameMap.getCountryMap().get(String.valueOf(c.getIndex())).setCountryOwner(super.getD_player());
                    c.setD_noOfArmies(c.getD_noOfArmies() * 2);
                }
            }
        }

        return null;
    }
    /**
     * Determines the country to attack during the cheater strategy.
     * In the cheater strategy, there is no specific country chosen for attack, so this method returns null.
     *
     * @return Null since the cheater strategy does not involve specific attacking.
     */
    @Override
    protected Country toAttack() {
        return null;
    }
    /**
     * Determines the country to attack from during the cheater strategy.
     * In the cheater strategy, there is no specific country chosen for attack, so this method returns null.
     *
     * @return Null since the cheater strategy does not involve specific attacking.
     */
    @Override
    protected Country toAttackFrom() {
        return null;
    }
    /**
     * Determines the country to move armies from during the cheater strategy.
     * In the cheater strategy, there is no specific country chosen for moving armies, so this method returns null.
     *
     * @return Null since the cheater strategy does not involve specific army movement.
     */
    @Override
    protected Country toMoveFrom() {
        return null;
    }
    /**
     * Determines the country to defend during the cheater strategy.
     * In the cheater strategy, there is no specific country chosen for defense, so this method returns null.
     *
     * @return Null since the cheater strategy does not involve specific defending.
     */
    @Override
    protected Country toDefend() {
        return null;
    }
}
