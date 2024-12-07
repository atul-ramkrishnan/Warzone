package org.warzone.strategy;

import org.warzone.entities.Continent;
import org.warzone.entities.Country;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class CheaterTest {
    Player player = new Player("CheaterPlayer");
    Player enemy = new Player("EnemyPlayer");
    List<Country> countryList = new ArrayList<Country>();
    Country country3 = new Country();
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
        country3.setIndex(1)
                .setName("TestCountry3")
                .setContinent(new Continent())
                .setX(25)
                .setY(30)
                .setD_noOfArmies(10);
        country3.setCountryOwner(enemy);
        adjacentCountries2.put("Neighbour 3", country3);
        adjacentCountries2.put("Neighbour 2", new Country());
        country2.setAdjacentCountries(adjacentCountries2);
        country2.setCountryOwner(player);

        countryList.add(country1);
        countryList.add(country2);
        return countryList;
    }
}
