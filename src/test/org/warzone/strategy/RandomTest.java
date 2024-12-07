package org.warzone.strategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.warzone.entities.Continent;
import org.warzone.entities.Country;

import java.sql.Array;
import java.util.HashMap;
import java.util.*;

public class RandomTest {
    Player player = new Player("RandomPlayer");
    /**
     * Helper method to create a list of test countries.
     *
     * @return The list of test countries.
     */
    private List<Country> createCountry(){
        List<Country> countryList = new ArrayList<Country>();
        Country country1 = new Country();
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
                .setD_noOfArmies(5);
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
     * Test the {@link RandomStrategy#toAttack()} method.
     */
    @Test
    void testToAttack() {

        RandomStrategy randomStrategy = new RandomStrategy(createCountry(),player);
        assertNotNull(randomStrategy.toAttack());
    }
    /**
     * Test the {@link RandomStrategy#toAttackFrom()} method.
     */
    @Test
    void testToAttackFrom() {
        RandomStrategy randomStrategy = new RandomStrategy(createCountry(),player);
        assertNotNull(randomStrategy.toAttackFrom());
    }

    @Test
    void createOrder() {
        RandomStrategy randomStrategy = new RandomStrategy(createCountry(),player);
        assertNotNull(randomStrategy.toAttackFrom());
    }
    /**
     * Test the {@link RandomStrategy#toMoveFrom()} method.
     */
    @Test
    void testToMoveFrom() {
        RandomStrategy randomStrategy = new RandomStrategy(createCountry(),player);
        assertNotNull(randomStrategy.toMoveFrom());
    }
    /**
     * Test the {@link RandomStrategy#toDefend()} method.
     */
    @Test
    void testToDefend() {
        RandomStrategy randomStrategy = new RandomStrategy(createCountry(),player);
        assertNotNull(randomStrategy.toDefend());
    }
}
