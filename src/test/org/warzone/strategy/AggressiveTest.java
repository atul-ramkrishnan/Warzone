package org.warzone.strategy;
import org.junit.jupiter.api.Test;
import org.warzone.entities.Continent;
import org.warzone.entities.Country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class AggressiveTest {
    Player player = new Player("RandomPlayer");
    List<Country> countryList = new ArrayList<Country>();
    /**
     * Helper method to create a list of test countries.
     *
     * @return The list of test countries.
     */
    private List<Country> createCountry(){

        Country country1 = new Country();
        // Set values using setter methods
        country1.setIndex(1)
                .setName("TestCountry")
                .setContinent(new Continent())
                .setX(10)
                .setY(20)
                .setD_noOfArmies(5);
        HashMap<String, Country> adjacentCountries = new HashMap<>();
        adjacentCountries.put("Neighbour 1", new Country());
        adjacentCountries.put("Neighbour 2", new Country());
        country1.setAdjacentCountries(adjacentCountries);
        country1.setCountryOwner(player);

        Country country2 = new Country();
        country2.setIndex(1)
                .setName("TestCountry2")
                .setContinent(new Continent())
                .setX(12)
                .setY(25)
                .setD_noOfArmies(10);
        HashMap<String, Country> adjacentCountries2 = new HashMap<>();
        adjacentCountries2.put("Neighbour 3", new Country());
        adjacentCountries2.put("Neighbour 2", new Country());
        country2.setAdjacentCountries(adjacentCountries2);
        country2.setCountryOwner(player);
        countryList.add(country1);
        countryList.add(country2);
        Map<String, Country> countryMap = new HashMap<>();
        countryList.forEach(country -> countryMap.put(String.valueOf(country.getIndex()), country));
        player.setD_listCountries(countryMap);
        return countryList;
    }
    /**
     * Test the {@link AggressiveStrategy#toAttackFrom()} method.
     */
    @Test
    void testToAttackFrom() {
        AggressiveStrategy aggressiveStrategy = new AggressiveStrategy(createCountry(),player);
        assertEquals(countryList.get(1),aggressiveStrategy.toAttackFrom());
    }
    /**
     * Test the {@link AggressiveStrategy#toMoveFrom()} method.
     */
    @Test
    void testToMoveFrom() {
        AggressiveStrategy aggressiveStrategy = new AggressiveStrategy(createCountry(),player);
        assertEquals(countryList.get(0),aggressiveStrategy.toMoveFrom());
    }

    @Test
    void createOrder() {
        AggressiveStrategy aggressiveStrategy = new AggressiveStrategy(createCountry(),player);
        assertEquals(countryList.get(0),aggressiveStrategy.toMoveFrom());
    }

}
